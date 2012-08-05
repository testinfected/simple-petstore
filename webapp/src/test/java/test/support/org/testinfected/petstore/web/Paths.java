package test.support.org.testinfected.petstore.web;

public final class Paths {

    private final String contextPath;

    public static Paths root() {
        return relativeTo("");
    }

    public static Paths relativeTo(String contextPath) {
        return new Paths(contextPath);
    }

    public Paths(String contextPath) {
        this.contextPath = contextPath;
    }

    public String itemsPath(String productNumber) {
        return pathFor("/products/" + productNumber + "/items");
    }

    public String productsPath() {
        return pathFor("/products");
    }

    public String pathFor(String relativePath) {
        return contextPath + relativePath;
    }
}
