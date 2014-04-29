package com.vtence.molecule.http;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

import java.nio.charset.Charset;

import static com.vtence.molecule.http.HeaderNames.CONTENT_TYPE;

public class ContentType {

    private static final int TYPE = 0;
    private static final int SUB_TYPE = 1;

    private final String type;
    private final String subType;
    private final String charset;

    public ContentType(String type, String subType, String charset) {
        this.type = type;
        this.subType = subType;
        this.charset = charset;
    }

    public static ContentType of(Response response) {
        return parse(response.get(CONTENT_TYPE));
    }

    public static ContentType of(Request request) {
        return parse(request.header(CONTENT_TYPE));
    }

    public static ContentType parse(String header) {
        return header != null ? from(new Header(header)) : null;
    }

    public static ContentType from(Header header) {
        Header.Value contentType = header.first();
        String[] tokens = contentType.value().split("/");
        return new ContentType(type(tokens), subType(tokens), charset(contentType));
    }

    private static String type(String[] tokens) {
        return tokens[TYPE];
    }

    private static String subType(String[] tokens) {
        return tokens.length > 1 ? tokens[SUB_TYPE] : null;
    }

    private static String charset(Header.Value header) {
        return header.parameter("charset");
    }

    public String mediaType() {
        return type + "/" + subType;
    }

    public String type() {
        return type;
    }

    public String subType() {
        return subType;
    }

    public Charset charset() {
        return charset != null ? Charset.forName(charset) : null;
    }

    public String charsetName() {
        return charset;
    }

    public String toString() {
        return mediaType() + (charset != null ? "; charset=" + charset : "");
    }
}
