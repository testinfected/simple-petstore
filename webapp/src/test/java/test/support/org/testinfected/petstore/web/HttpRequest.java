package test.support.org.testinfected.petstore.web;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import org.testinfected.petstore.util.ExceptionImposter;
import test.support.com.pyxis.petstore.views.Routes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest {

    private int timeout = 5000;
    private int port = OfflineContext.TEST_PORT;
    private HttpMethod method = HttpMethod.GET;
    private String path = "/";
    private Routes routes = Routes.root();

    public static HttpResponse get(String path) throws IOException {
        return aRequest().forPath(path).send();
    }

    public static HttpRequest aRequest() {
        return new HttpRequest();
    }

    public HttpRequest onPort(int port) {
        this.port = port;
        return this;
    }

    public HttpRequest forPath(String path) {
        this.path = path;
        return this;
    }

    public HttpResponse send() throws IOException {
        WebClient client = new WebClient();
        client.setTimeout(timeout);
        WebRequest request = new WebRequest(requestUrl(), method);

        return new HttpResponse(client.loadWebResponse(request));
    }

    private URL requestUrl() throws MalformedURLException {
        try {
            return new URL("http://localhost:" + port + routes.pathFor(path));
        } catch (MalformedURLException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }
}
