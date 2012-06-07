package org.testinfected.petstore;

import java.util.HashMap;
import java.util.Map;

public class MimeType {
    
    private static final Map<String, String> knownTypes = new HashMap<String, String>();

    static {
        knownTypes.put(".txt", "text/plain");
        knownTypes.put(".html", "text/html");
        knownTypes.put(".css", "text/css");
        knownTypes.put(".png", "image/png");
        knownTypes.put(".ico", "image/x-icon");
    }

    public static String guessFrom(String filename) {
        for (String ext : knownTypes.keySet()) {
            if (filename.endsWith(ext)) return knownTypes.get(ext);
        }
        return "text/plain";
    }
}
