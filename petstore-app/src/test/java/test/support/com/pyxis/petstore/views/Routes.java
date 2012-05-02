package test.support.com.pyxis.petstore.views;

public final class Routes {

    private static final String PETSTORE_CONTEXT_PATH = "/petstore";

    private final String contextPath;

    public static Routes petstore() {
        return new Routes(PETSTORE_CONTEXT_PATH);
    }

    public Routes(String contextPath) {
        this.contextPath = contextPath;
    }

    public String contextPath() {
        return contextPath;
    }

    public String homePath() {
        return contextPath();
    }

    public String pathFor(String relativePath) {
        return contextPath + "/" + relativePath;
    }

    public String itemsPath(String productNumber) {
        return pathFor("products/" + productNumber + "/items");
    }

    public String cartItemsPath() {
        return pathFor("cartitems");
    }

    public String cartPath() {
        return pathFor("cart");
    }

    public String checkoutPath() {
        return pathFor("checkout");
    }

    public String purchasesPath() {
        return pathFor("purchases");
    }
}
