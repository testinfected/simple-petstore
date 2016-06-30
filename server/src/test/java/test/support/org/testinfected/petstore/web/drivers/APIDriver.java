package test.support.org.testinfected.petstore.web.drivers;

import com.vtence.molecule.testing.http.Form;
import com.vtence.molecule.testing.http.HttpRequest;
import com.vtence.molecule.testing.http.HttpResponse;

import java.io.IOException;

import static com.vtence.molecule.testing.http.HttpResponseAssert.assertThat;

public class APIDriver {

    public static final String CREATED = "Created";

    private final HttpRequest request;

    public APIDriver(HttpRequest request) {
        this.request = request;
    }

    public void addProduct(String number, String name, String description, String photo) throws IOException {
        HttpRequest post = request.but()
                                  .method("POST")
                                  .path("/products")
                                  .content(Form.urlEncoded().addField("number", number)
                                               .addField("name", name)
                                               .addField("description", description)
                                               .addField("photo", photo));

        HttpResponse response = post.send();
        assertThat(response).hasStatusMessage(CREATED);
    }

    public void addItem(String productNumber, String itemNumber, String itemDescription, String itemPrice) throws IOException {
        HttpRequest post = request.but()
                                  .method("POST")
                                  .path("/products/" + productNumber + "/items")
                                  .content(Form.urlEncoded().addField("number", itemNumber)
                                               .addField("description", itemDescription)
                                               .addField("price", itemPrice));

        HttpResponse response = post.send();
        assertThat(response).hasStatusMessage(CREATED);
    }
}