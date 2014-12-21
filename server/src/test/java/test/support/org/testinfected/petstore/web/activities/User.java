package test.support.org.testinfected.petstore.web.activities;

import test.support.org.testinfected.petstore.web.page.PetStoreDriver;

public class User {
    private final PetStoreDriver petstore;
    private final CatalogBrowsing catalog;
    private final CartManagement cart;
    private final StoreManagement store;
    private final Ordering order;

    public User(PetStoreDriver petstore) {
        this.petstore = petstore;
        this.catalog = new CatalogBrowsing(petstore);
        this.cart = new CartManagement(petstore);
        this.store = new StoreManagement(petstore);
        this.order = new Ordering(petstore);
    }

    public User loginAs(String username) {
        petstore.goToHomePage();
        if (!petstore.isLoggedIn(username)) {
            petstore.logout();
            petstore.loginAs(username);
        }
        return this;
    }

    public StoreManagement manageStore() {
        return store;
    }

    public CatalogBrowsing catalog() {
        return catalog;
    }

    public CartManagement cart() {
        return cart;
    }

    public Ordering order() {
        return order;
    }

    public CatalogBrowsing forProduct(String product) {
        catalog.forProduct(product);
        return catalog;
    }

    public void cartIsEmpty() {
        petstore.displaysCartItemCount(0);
    }

    public void cartItemCountIs(int count) {
        petstore.displaysCartItemCount(count);
    }
}
