package com.vtence.molecule.support;

import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private final WebClient client;
    private final String domain = "localhost";

    private final Map<String, List<String>> parameters = new HashMap<String, List<String>>();
    private final Map<String, String> headers = new HashMap<String, String>();
    private final Map<String, String> cookies = new HashMap<String, String>();

    private HttpMethod method = HttpMethod.GET;
    private String path = "/";
    private int port;
    private int timeoutInMillis = 5000;
    private boolean followRedirects = true;
    private boolean applyCookies = true;
    private String body;
    private String encodingType;

    public HttpRequest(int port) {
        this(new WebClient(), port);
    }

    public HttpRequest(WebClient client, int port) {
        this.client = client;
        this.port = port;
    }

    public HttpRequest but() {
        HttpRequest other = new HttpRequest(client, port).
                withTimeout(timeoutInMillis).
                usingMethod(method).
                on(path).
                applyCookies(applyCookies).
                followRedirects(followRedirects).
                withEncodingType(encodingType).
                withBody(body);

        for (String header: headers.keySet()) {
            other.withHeader(header, headers.get(header));
        }
        for (String cookie: cookies.keySet()) {
            other.withCookie(cookie, cookies.get(cookie));
        }
        for (String name: parameters.keySet()) {
            other.withParameters(name, parameters(name));
        }

        return other;
    }

    private String[] parameters(String name) {
        List<String> values = parameters.get(name);
        return values.toArray(new String[values.size()]);
    }

    public HttpRequest applyCookies(boolean apply) {
        this.applyCookies = apply;
        return this;
    }

    public HttpRequest onPort(int port) {
        this.port = port;
        return this;
    }

    public HttpRequest on(String path) {
        this.path = path;
        return this;
    }

    public HttpRequest followRedirects(boolean follow) {
        this.followRedirects = follow;
        return this;
    }

    public HttpRequest withHeader(String header, String value) {
        this.headers.put(header, value);
        return this;
    }

    public HttpRequest withParameter(String name, String value) {
        return withParameters(name, value);
    }

    public HttpRequest withParameters(String name, String... value) {
        parameters.put(name, Arrays.asList(value));
        return this;
    }

    public HttpRequest withBody(String body) {
        this.body = body;
        return this;
    }

    public HttpRequest withEncodingType(String type) {
        this.encodingType = type;
        return this;
    }

    public HttpRequest withCookie(String name, String value) {
        this.cookies.put(name, value);
        return this;
    }

    public HttpResponse send() throws IOException {
        client.getOptions().setTimeout(timeoutInMillis);
        for (String cookie: cookies.keySet()) {
            client.getCookieManager().addCookie(new Cookie(domain, cookie, cookies.get(cookie)));
        }
        client.getCookieManager().setCookiesEnabled(applyCookies);
        client.getOptions().setRedirectEnabled(followRedirects);
        WebRequest request = new WebRequest(requestUrl(), method);
        request.setRequestParameters(requestParameters());
        if (body != null) request.setRequestBody(body);
        if (encodingType != null) request.setEncodingType(FormEncodingType.getInstance(encodingType));
        request.setAdditionalHeaders(headers);

        return new HttpResponse(client.loadWebResponse(request));
    }

    public HttpResponse get(String path) throws IOException {
        return usingMethod(HttpMethod.GET).on(path).send();
    }

    public HttpResponse post(String path) throws IOException {
        return usingMethod(HttpMethod.POST).on(path).send();
    }

    public HttpResponse put(String path) throws IOException {
        return usingMethod(HttpMethod.PUT).on(path).send();
    }

    public HttpResponse delete(String path) throws IOException {
        return usingMethod(HttpMethod.DELETE).on(path).send();
    }

    public HttpRequest withTimeout(int timeoutInMillis) {
        this.timeoutInMillis = timeoutInMillis;
        return this;
    }

    public HttpRequest usingMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public HttpRequest removeParameters() {
        parameters.clear();
        return this;
    }

    private List<NameValuePair> requestParameters() {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (String name: parameters.keySet()) {
            for (String value : parameters(name)) {
                pairs.add(new NameValuePair(name, value));
            }
        }
        return pairs;
    }

    private URL requestUrl() throws MalformedURLException {
        return new URL("http://" + domain + ":" + port + path);
    }
}