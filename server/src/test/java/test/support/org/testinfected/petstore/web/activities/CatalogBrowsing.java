package test.support.org.testinfected.petstore.web.activities;

import test.support.org.testinfected.petstore.web.page.PetStore;
import test.system.org.testinfected.petstore.features.Product;

public class CatalogBrowsing {

    private final PetStore petstore;

    private String product;

    public CatalogBrowsing(PetStore petstore) {
        this.petstore = petstore;
    }

    public CatalogBrowsing forProduct(String product) {
        this.product = product;
        return this;
    }

    public void containsProduct(String number, String name) {
        petstore.search(name).displaysProduct(number, name);
    }

    public void containsNoProductNamed(String name) {
        petstore.search(name).showsNoResult();
    }

    public void matchesProducts(String searchTerm, Product... products) {
        for (Product product : products) {
            petstore.search(searchTerm).displaysMatchCount(products.length).
                    displaysProduct(product.number, product.name);
        }
    }

    public void noItemIsAvailable() {
        petstore.search(product).selectProduct(product).showsNoItemAvailable();
    }

    public void itemIsInStock(String number, String description, String price) {
        petstore.search(product).
                selectProduct(product).displaysItem(number, description, price).
                returnToCatalog();
    }

    public CatalogBrowsing addItemToCart(String itemNumber) {
        petstore.search(product).selectProduct(product).addToCart(itemNumber).continueShopping();
        return this;
    }
}
