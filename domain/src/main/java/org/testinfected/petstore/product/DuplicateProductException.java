package org.testinfected.petstore.product;

public class DuplicateProductException extends Exception {

    private final Product product;

    public DuplicateProductException(Product product, Throwable cause) {
        super(cause);
        this.product = product;
    }

    public DuplicateProductException(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
