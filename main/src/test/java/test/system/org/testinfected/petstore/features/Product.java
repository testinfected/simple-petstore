package test.system.org.testinfected.petstore.features;

public class Product {

    public static Product product(String number, String name) {
        return new Product(number, name);
    }

    public final String number;
    public final String name;

    public Product(String number, String name) {
        this.number = number;
        this.name = name;
    }
}
