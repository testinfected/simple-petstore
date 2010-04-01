package test.support.com.pyxis.petstore.views;

public final class PathFor {

    public static final String BASE_URL = "/petstore";

    private PathFor() {}

    public static String pathFor(String path) {
        return BASE_URL + path;
    }

    public static String homePath() {
        return pathFor("");
    }

    public static String itemsPath(String produtNumber) {
        return pathFor("/products/" + produtNumber + "/items");
    }

    public static String cartItemsPath() {
        return pathFor("/cartitems");
    }

    public static String cartPath() {
        return pathFor("/cart");
    }

    public static String checkoutPath() {
        return pathFor("/checkout");
    }

    public static String purchasesPath() {
        return pathFor("/purchases");
    }
}
