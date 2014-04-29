package com.vtence.molecule.http;

import java.util.HashMap;
import java.util.Map;

public final class MimeTypes {

    public static final String HTML = "text/html";
    public static final String TEXT = "text/plain";
    public static final String CSS = "text/css";
    public static final String JAVASCRIPT = "application/javascript";
    public static final String JSON = "application/json";
    public static final String PNG = "image/png";
    public static final String GIF = "image/gif";
    public static final String JPEG = "image/jpeg";
    public static final String ICON = "image/x-icon";
    public static final String BINARY_DATA = "application/octet-stream";

    private final Map<String, String> knownTypes = new HashMap<String, String>();

    public static MimeTypes defaults() {
        MimeTypes mimeTypes = new MimeTypes();
        mimeTypes.register("txt", TEXT);
        mimeTypes.register("html", HTML);
        mimeTypes.register("css", CSS);
        mimeTypes.register("js", JAVASCRIPT);
        mimeTypes.register("png", PNG);
        mimeTypes.register("gif", GIF);
        mimeTypes.register("jpg", JPEG);
        mimeTypes.register("jpeg", JPEG);
        mimeTypes.register("ico", ICON);
        return mimeTypes;
    }

    public void register(String extension, String mimeType) {
        knownTypes.put(extension, mimeType);
    }

    public String guessFrom(String filename) {
        for (String ext : knownTypes.keySet()) {
            if (filename.endsWith("." + ext)) return knownTypes.get(ext);
        }
        return BINARY_DATA;
    }
}
