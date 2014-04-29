package com.vtence.molecule.middlewares;

import com.vtence.molecule.Application;
import com.vtence.molecule.lib.ChunkedBody;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static com.vtence.molecule.http.HeaderNames.TRANSFER_ENCODING;

public class ContentLengthHeaderTest {

    ContentLengthHeader contentLengthHeader = new ContentLengthHeader();

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Test public void
    setsContentLengthOnFixedLengthBodiesIfNoneSet() throws Exception {
        contentLengthHeader.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body("This body has a size of 32 bytes");
            }
        });

        contentLengthHeader.handle(request, response);
        response.assertHeader("Content-Length", "32");
    }

    @Test public void
    doesNotSetContentLengthOnVariableLengthBodies() throws Exception {
        contentLengthHeader.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(new ChunkedBody() {
                    public void writeTo(OutputStream out, Charset charset) throws IOException {
                        out.write("A variable length body".getBytes(charset));
                    }

                    public void close() throws IOException {}
                });
            }
        });

        contentLengthHeader.handle(request, response);
        response.assertNoHeader("Content-Length");
    }

    @Test public void
    doesNotSetContentLengthOnEmptyBodies() throws Exception {
        contentLengthHeader.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
            }
        });

        contentLengthHeader.handle(request, response);
        response.assertNoHeader("Content-Length");
    }

    @Test public void
    doesNotSetContentLengthIfAlreadySet() throws Exception {
        contentLengthHeader.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.contentLength(1);
                response.body("This body is definitely larger than 1 byte");
            }
        });

        contentLengthHeader.handle(request, response);
        response.assertHeader("Content-Length", "1");
    }

    @Test public void
    doesNotSetContentLengthForChunkedTransferEncoding() throws Exception {
        contentLengthHeader.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.set(TRANSFER_ENCODING, "chunked");
                response.body("This body is chunked encoded");
            }
        });

        contentLengthHeader.handle(request, response);
        response.assertNoHeader("Content-Length");
    }
}
