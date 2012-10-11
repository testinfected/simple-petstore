package test.support.com.pyxis.petstore.web;

import com.gargoylesoftware.htmlunit.HttpMethod;
import test.support.org.testinfected.petstore.web.HttpRequest;
import test.support.org.testinfected.petstore.web.HttpResponse;

import java.io.IOException;

public class RestDriver {

    public static final String CREATED = "Created";

    private final HttpRequest request;

    public RestDriver(HttpRequest request) {
        this.request = request;
    }

    public void addProduct(String number, String name, String description) throws IOException {
        HttpRequest post = request.but()
                .withMethod(HttpMethod.POST)
                .withPath("/products")
                .withParameter("number", number)
                .withParameter("name", name)
                .withParameter("description", description);

        HttpResponse response = post.send();
        response.assertHasStatusMessage(CREATED);
    }
}