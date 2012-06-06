package test.support.org.testinfected.petstore.templating;

import com.gargoylesoftware.htmlunit.WebRequest;
import org.testinfected.petstore.ExceptionImposter;
import test.support.com.pyxis.petstore.views.Routes;

import java.net.MalformedURLException;
import java.net.URL;

public class WebRequestBuilder {

    private int port;
    private String path;
    private Routes routes = new Routes();

    public static WebRequestBuilder aRequest() {
        return new WebRequestBuilder();
    }

    public WebRequestBuilder onPort(int port) {
        this.port = port;
        return this;
    }

    public WebRequestBuilder forPath(String path) {
        this.path = path;
        return this;
    }

    public WebRequest build() {
        try {
            return new WebRequest(new URL("http://localhost:" + port + routes.pathFor(path)));
        } catch (MalformedURLException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public WebRequestBuilder but() {
        return aRequest().onPort(port).forPath(path);
    }
}
