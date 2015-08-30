package test.support.org.testinfected.petstore.web.drivers;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.vtence.molecule.support.HttpRequest;
import com.vtence.molecule.support.HttpResponse;

import java.io.IOException;

public class APIDriver {

    public static final String CREATED = "Created";

    private final HttpRequest request;

    public APIDriver(HttpRequest request) {
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