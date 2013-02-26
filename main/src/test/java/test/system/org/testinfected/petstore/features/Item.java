package test.system.org.testinfected.petstore.features;

public class Item {

    public static Item item(String number, String description, String price) {
        return new Item(number, description, price, 1, price);
    }

    public static Item item(String number, String description, String price, int quantity, String totalPrice) {
        return new Item(number, description, price, quantity, totalPrice);
    }

    public final String number;
    public final String description;
    public final String price;
    public final int quantity;
    public final String totalPrice;

    public Item(String number, String description, String price, int quantity, String totalPrice) {
        this.number = number;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
