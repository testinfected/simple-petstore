package com.vtence.molecule;

import com.vtence.molecule.http.Cookie;
import com.vtence.molecule.http.HeaderNames;
import com.vtence.molecule.http.HttpStatus;
import com.vtence.molecule.helpers.Charsets;
import com.vtence.molecule.http.ContentType;
import com.vtence.molecule.helpers.Headers;
import com.vtence.molecule.http.HttpDate;
import com.vtence.molecule.lib.BinaryBody;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.vtence.molecule.http.HeaderNames.CONTENT_LENGTH;
import static com.vtence.molecule.http.HeaderNames.CONTENT_TYPE;
import static com.vtence.molecule.lib.TextBody.text;
import static java.lang.Long.parseLong;

public class Response {
    private final Headers headers = new Headers();
    private final Map<String, Cookie> cookies = new LinkedHashMap<String, Cookie>();

    private int statusCode = HttpStatus.OK.code;
    private String statusText = HttpStatus.OK.text;
    private Body body = BinaryBody.empty();

    public Response() {}

    public Response status(HttpStatus status) {
        statusCode(status.code);
        statusText(status.text);
        return this;
    }

    public Response statusCode(int code) {
        statusCode = code;
        return this;
    }

    public int statusCode() {
        return statusCode;
    }

    public Response statusText(String text) {
        statusText = text;
        return this;
    }

    public String statusText() {
        return statusText;
    }

    public Response redirectTo(String location) {
        status(HttpStatus.SEE_OTHER);
        set(HeaderNames.LOCATION, location);
        return this;
    }

    public boolean has(String name) {
        return headers.has(name);
    }

    public String get(String name) {
        return headers.get(name);
    }

    public long getLong(String name) {
        String value = get(name);
        return value != null ? parseLong(value) : -1;
    }

    public Response set(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public Response setLong(String name, long value) {
        return set(name, String.valueOf(value));
    }

    public Response set(String name, Date date) {
        return set(name, HttpDate.format(date));
    }

    public Response setDate(String name, long date) {
        return set(name, new Date(date));
    }

    public Response remove(String name) {
        headers.remove(name);
        return this;
    }

    public Iterable<String> names() {
        return headers.names();
    }

    public Map<String, String> headers() {
        return headers.all();
    }

    public String contentType() {
        return get(CONTENT_TYPE);
    }

    public Response contentType(String mediaType) {
        set(CONTENT_TYPE, mediaType);
        return this;
    }

    public long contentLength() {
        return getLong(CONTENT_LENGTH);
    }

    public Response contentLength(long length) {
        setLong(CONTENT_LENGTH, length);
        return this;
    }

    public Response add(Cookie cookie) {
        cookies.put(cookie.name(), cookie);
        return this;
    }

    public Cookie cookie(String name) {
        return cookies.get(name);
    }

    public Iterable<Cookie> cookies() {
        return new ArrayList<Cookie>(cookies.values());
    }

    public Charset charset() {
        ContentType contentType = ContentType.of(this);
        if (contentType == null || contentType.charset() == null) {
            return Charsets.ISO_8859_1;
        }
        return contentType.charset();
    }

    public Response body(String text) throws IOException {
        return body(text(text));
    }

    public Response body(Body body) throws IOException {
        this.body = body;
        return this;
    }

    public Body body() {
        return body;
    }

    public long size() {
        return body.size(charset());
    }

    public boolean empty() {
        return size() == 0;
    }
}