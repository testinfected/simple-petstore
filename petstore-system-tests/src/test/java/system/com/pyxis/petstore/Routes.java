package system.com.pyxis.petstore;

import java.util.HashMap;
import java.util.Map;


public final class Routes {

    private static final String SERVER_URL = "http://localhost:8280/petstore";
    private static Map<Class, String> urlMappings = new HashMap<Class, String>();

    private Routes() {}

    static {
        urlMappings.put(SearchPage.class, "");
    }

    public static String urlFor(Class pageClass) {
        return SERVER_URL + urlMappings.get(pageClass);
    }
}

