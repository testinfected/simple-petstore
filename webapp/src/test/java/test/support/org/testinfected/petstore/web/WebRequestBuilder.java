package test.support.org.testinfected.petstore.web;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.testinfected.petstore.util.ExceptionImposter;
import test.support.com.pyxis.petstore.views.Routes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WebRequestBuilder {

    private int timeout = 5000;
    private int port = OfflineContext.TEST_PORT;
    private HttpMethod method = HttpMethod.GET;
    private String path = "/";
    private Routes routes = Routes.root();

    public static WebRequestBuilder aRequest() {
        return new WebRequestBuilder();
    }

    // todo use this instead of aRequest whenever possible
    public static WebRequestBuilder get(String path) {
        return aRequest().forPath(path);
    }

    public WebRequestBuilder onPort(int port) {
        this.port = port;
        return this;
    }

    public WebRequestBuilder forPath(String path) {
        this.path = path;
        return this;
    }

    public WebRequestBuilder but() {
        return aRequest().onPort(port).forPath(path);
    }

    public WebResponse send() throws IOException {
        WebClient client = new WebClient();
        client.setTimeout(timeout);
        WebRequest request = new WebRequest(requestUrl(), method);

        return client.loadWebResponse(request);
    }

    private URL requestUrl() throws MalformedURLException {
        try {
            return new URL("http://localhost:" + port + routes.pathFor(path));
        } catch (MalformedURLException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }
}
