package com.vtence.molecule.servers;

import com.vtence.molecule.Application;
import com.vtence.molecule.http.Cookie;
import com.vtence.molecule.http.HttpStatus;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.support.HttpRequest;
import com.vtence.molecule.support.HttpResponse;
import com.vtence.molecule.support.StackTrace;
import com.vtence.molecule.FailureReporter;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.vtence.molecule.http.HttpStatus.CREATED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SimpleServerTest {

    SimpleServer server = new SimpleServer("localhost", 9999);
    HttpRequest request = new HttpRequest(server.port());
    HttpResponse response;

    Throwable error;

    @Before public void
    configureServer() {
        server.reportErrorsTo(new FailureReporter() {
            public void errorOccurred(Throwable error) {
                SimpleServerTest.this.error = error;
            }
        });
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    respondsToRequests() throws IOException {
        server.run(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.status(CREATED);
            }
        });

        response = request.send();
        assertNoError();
        response.assertHasStatus(CREATED);
    }

    @Test public void
    chunksResponseWhenContentLengthUnknown() throws IOException {
        server.run(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body("<html>...</html>");
            }
        });

        response = request.send();
        assertNoError();
        response.assertHasContent("<html>...</html>");
        response.assertChunked();
    }

    @Test public void
    doesNoChunkResponsesWithContentLengthHeader() throws IOException {
        server.run(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.contentLength(16);
                response.body("<html>...</html>");
            }
        });

        response = request.send();
        assertNoError();
        response.assertHasContent("<html>...</html>");
        response.assertHasHeader("Content-Length", "16");
        response.assertNotChunked();
    }

    @Test public void
    encodesResponsesAccordingToContentType() throws IOException {
        server.run(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.contentType("text/plain; charset=utf-16");
                response.body("This content requires encoding &âçüè!");
                response.status(HttpStatus.OK);
            }
        });

        response = request.send();
        assertNoError();
        response.assertOK();
        response.assertContentIsEncodedAs("UTF-16");
    }

    @Test public void
    supportsArrayParameters() throws IOException {
        server.run(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(request.parameters("names").toString());
            }
        });

        response = request.withParameters("names", "Alice", "Bob", "Charles").send();
        assertNoError();
        response.assertHasContent("[Alice, Bob, Charles]");
    }

    @SuppressWarnings("unchecked")
    @Test public void
    providesGeneralRequestInformation() throws IOException {
        final Map<String, String> info = new HashMap<String, String>();
        server.run(new Application() {
            public void handle(Request request, Response response) throws Exception {
                info.put("uri", request.uri());
                info.put("path", request.path());
                info.put("ip", request.remoteIp());
                info.put("hostname", request.remoteHost());
                info.put("port", String.valueOf(request.remotePort()));
                info.put("protocol", request.protocol());
            }
        });

        request.get("/path?query");
        assertNoError();

        assertThat("request information", info, allOf(
                hasEntry("uri", "/path?query"),
                hasEntry("path", "/path"),
                hasEntry("ip", "127.0.0.1"),
//                hasEntry("hostname", "localhost"),
                hasEntry(equalTo("port"), notNullValue()),
                hasEntry("protocol", "HTTP/1.1")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    supportsRequestHeaders() throws IOException {
        final Map<String, Iterable<String>> headers = new HashMap<String, Iterable<String>>();
        server.run(new Application() {
            public void handle(Request request, Response response) throws Exception {
                headers.put("names", request.headerNames());
                headers.put("accept", Arrays.asList(request.header("Accept")));
                headers.put("encoding", request.headers("Accept-Encoding"));
            }
        });

        request.withHeader("Accept", "text/html").
                withHeader("Accept-Encoding", "gzip, identity; q=0.5, deflate;q=1.0, *;q=0").
                send();
        assertNoError();

        assertThat("header names", headers.get("names"), hasItems("Accept", "Accept-Encoding"));
        assertThat("accept", headers.get("accept"), contains("text/html"));
        assertThat("accept-encoding", headers.get("encoding"), contains("gzip", "deflate", "identity"));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    detailsRequestContent() throws IOException {
        final Map<String, String> content = new HashMap<String, String>();
        server.run(new Application() {
            public void handle(Request request, Response response) throws Exception {
                content.put("contentType", String.valueOf(request.contentType()));
                content.put("contentLength", String.valueOf(request.contentLength()));
                content.put("body", request.body());
            }
        });

        request.withHeader("Accept", "text/html")
                .withEncodingType("application/x-www-form-urlencoded")
                .withBody("name=value")
                .post("/uri");
        assertNoError();

        assertThat("request content", content, allOf(hasEntry("contentType", "application/x-www-form-urlencoded"),
                hasEntry("contentLength", "10"),
                hasEntry("body", "name=value")));
    }

    @Test public void
    readsRequestCookies() throws IOException {
        final Map<String, String> cookies = new HashMap<String, String>();
        server.run(new Application() {
            public void handle(Request request, Response response) throws Exception {
                for (Cookie cookie : request.cookies()) {
                    cookies.put(cookie.name(), cookie.value());
                }
            }
        });

        request.withCookie("cookie1", "value1").withCookie("cookie2", "value2").send();
        assertNoError();

        assertThat("request cookies", cookies, allOf(hasEntry("cookie1", "value1"),
                hasEntry("cookie2", "value2")));
    }

    @Test public void
    setsResponseCookies() throws IOException {
        server.run(new Application() {
            public void handle(Request request, Response response) throws Exception {
                Cookie cookie = new Cookie("cookie", "value");
                cookie.httpOnly(true);
                cookie.maxAge(1800);
                response.add(cookie);
            }
        });

        response = request.send();
        assertNoError();
        response.assertHasCookie(containsString("cookie=value"));
        response.assertHasCookie(containsString("max-age=1800"));
        response.assertHasCookie(containsString("httponly"));
    }

    @Test public void
    supportsRequestAttributes() throws IOException {
        final Map<Object, Object> attributes = new HashMap<Object, Object>();
        server.run(new Application() {
            public void handle(Request request, Response response) throws Exception {
                request.attribute("name", "Velociraptor");
                request.attribute("family", "Dromaeosauridae");
                request.attribute("clade", "Dinosauria");
                request.removeAttribute("family");
                attributes.putAll(request.attributes());
            }
        });

        request.send();
        assertNoError();

        assertThat("attributes", attributes, allOf(containsEntry("name", "Velociraptor"),
                                                   not(containsKey("family")),
                                                   containsEntry("clade", "Dinosauria")));
    }

    private Matcher<Map<?, ?>> containsKey(Object key) {
        return Matchers.hasKey(equalTo(key));
    }

    private Matcher<Map<?, ?>> containsEntry(Object key, Object value) {
        return Matchers.hasEntry(equalTo(key), equalTo(value));
    }

    private void assertNoError() {
        if (error != null) fail(StackTrace.of(error));
    }
}
