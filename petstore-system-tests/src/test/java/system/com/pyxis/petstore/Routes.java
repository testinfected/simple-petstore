package system.com.pyxis.petstore;

import java.util.HashMap;
import java.util.Map;

public final class Routes {

	private static final String SERVER_URL = "http://localhost:" + serverPort() + "/petstore";
	private static Map<Class<?>, String> urlMappings = new HashMap<Class<?>, String>();

	private Routes() {
	}

	static {
		urlMappings.put(SearchPage.class, "/search");
	}

	public static String urlFor(Class<?> pageClass) {
		return SERVER_URL + urlMappings.get(pageClass);
	}

	public static String serverPort() {
		return System.getProperty("server.port", "8080");
	}
}
