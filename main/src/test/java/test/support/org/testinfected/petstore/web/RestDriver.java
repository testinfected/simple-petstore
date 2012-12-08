package test.support.org.testinfected.petstore.web;

import com.gargoylesoftware.htmlunit.HttpMethod;
import test.support.org.testinfected.molecule.web.HttpRequest;
import test.support.org.testinfected.molecule.web.HttpResponse;

import java.io.IOException;

public class RestDriver {

    public static final String CREATED = "Created";

    private final HttpRequest request;

    public RestDriver(HttpRequest request) {
        this.request = request;
    }

    public void addProduct(String number, String name, String description, String photo) throws IOException {
        HttpRequest post = request.but()
                .withMethod(HttpMethod.POST)
                .withPath("/products")
                .withParameter("number", number)
                .withParameter("name", name)
                .withParameter("description", description)
                .withParameter("photo", photo);

        HttpResponse response = post.send();
        response.assertHasStatusMessage(CREATED);
    }

    public void addItem(String productNumber, String itemNumber, String itemDescription, String itemPrice) throws IOException {
        HttpRequest post = request.but()
                .withMethod(HttpMethod.POST)
                .withPath("/products/" + productNumber + "/items")
                .withParameter("number", itemNumber)
                .withParameter("description", itemDescription)
                .withParameter("price", itemPrice);

        HttpResponse response = post.send();
        response.assertHasStatusMessage(CREATED);
    }
}