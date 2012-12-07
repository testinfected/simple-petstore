package test.support.org.testinfected.support.web;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import org.testinfected.support.Server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private final Map<String, String> parameters = new HashMap<String, String>();

    private HttpMethod method = HttpMethod.GET;
    private String path = "/";
    private int port;
    private int timeoutInMillis = 5000;

    public static HttpRequest aRequest() {
        return new HttpRequest();
    }

    public HttpRequest but() {
        HttpRequest other = aRequest().withTimeOut(timeoutInMillis).onPort(port).withMethod(method).withPath(path);
        for (String name : parameters.keySet()) {
            other.withParameter(name, parameters.get(name));
        }
        return other;
    }

    public HttpRequest onPort(int port) {
        this.port = port;
        return this;
    }

    public HttpRequest withPath(String path) {
        this.path = path;
        return this;
    }

    public HttpRequest to(Server server) {
        onPort(server.port());
        return this;
    }

    public HttpRequest withParameter(String name, String value) {
        parameters.put(name, value);
        return this;
    }

    public HttpResponse send() throws IOException {
        WebClient client = new WebClient();
        client.setTimeout(timeoutInMillis);
        WebRequest request = new WebRequest(requestUrl(), method);
        request.setRequestParameters(requestParameters());

        return new HttpResponse(client.loadWebResponse(request));
    }

    public HttpResponse get(String path) throws IOException {
        return withMethod(HttpMethod.GET).withPath(path).send();
    }

    public HttpResponse post(String path) throws IOException {
        return withMethod(HttpMethod.POST).withPath(path).send();
    }

    public HttpResponse delete(String path) throws IOException {
        return withMethod(HttpMethod.DELETE).withPath(path).send();
    }

    public HttpRequest withTimeOut(int timeoutInMillis) {
        this.timeoutInMillis = timeoutInMillis;
        return this;
    }

    public HttpRequest withMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    private List<NameValuePair> requestParameters() {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (String name : parameters.keySet()) {
            pairs.add(new NameValuePair(name, parameters.get(name)));
        }
        return pairs;
    }

    private URL requestUrl() throws MalformedURLException {
        return new URL("http://localhost:" + port + path);
    }
}
