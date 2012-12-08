package org.testinfected.molecule.util;

import java.util.HashMap;
import java.util.Map;

public class MimeTypes {

    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_CSS = "text/css";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_X_ICON = "image/x-icon";

    private static final String FALLBACK = "application/octet-stream";
    private static final Map<String, String> knownTypes = new HashMap<String, String>();

    static {
        knownTypes.put(".txt", TEXT_PLAIN);
        knownTypes.put(".html", TEXT_HTML);
        knownTypes.put(".css", TEXT_CSS);
        knownTypes.put(".png", IMAGE_PNG);
        knownTypes.put(".ico", IMAGE_X_ICON);
    }

    public static String guessFrom(String filename) {
        for (String ext : knownTypes.keySet()) {
            if (filename.endsWith(ext)) return knownTypes.get(ext);
        }
        return FALLBACK;
    }

    private MimeTypes() {}
}
