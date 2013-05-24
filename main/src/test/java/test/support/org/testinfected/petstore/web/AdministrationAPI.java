package test.support.org.testinfected.petstore.web;

import com.gargoylesoftware.htmlunit.HttpMethod;
import test.support.org.testinfected.molecule.integration.HttpRequest;
import test.support.org.testinfected.molecule.integration.HttpResponse;

import java.io.IOException;

public class AdministrationAPI {

    public static final String CREATED = "Created";

    private final HttpRequest request;

    public AdministrationAPI(HttpRequest request) {
        this.request = request;
    }

    public void addProduct(String number, String name, String description, String photo) throws IOException {
        HttpRequest post = request.but()
                .usingMethod(HttpMethod.POST)
                .on("/products")
                .withParameter("number", number)
                .withParameter("name", name)
                .withParameter("description", description)
                .withParameter("photo", photo);

        HttpResponse response = post.send();
        response.assertHasStatusMessage(CREATED);
    }

    public void addItem(String productNumber, String itemNumber, String itemDescription, String itemPrice) throws IOException {
        HttpRequest post = request.but()
                .usingMethod(HttpMethod.POST)
                .on("/products/" + productNumber + "/items")
                .withParameter("number", itemNumber)
                .withParameter("description", itemDescription)
                .withParameter("price", itemPrice);

        HttpResponse response = post.send();
        response.assertHasStatusMessage(CREATED);
    }
}