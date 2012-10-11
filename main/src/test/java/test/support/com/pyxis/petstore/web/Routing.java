package test.support.com.pyxis.petstore.web;

@Deprecated
public final class Routing {

    private final String baseUrl;

    public Routing(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String toHome() {
        return urlFor("/");
    }

    public String urlFor(String path) {
        return baseUrl + path;
    }
}
