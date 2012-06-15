package org.testinfected.petstore;

import org.testinfected.petstore.util.MimeTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class ClassPathResource implements Resource {
    private final URL url;
    private final Charset defaultCharset;

    public ClassPathResource(URL url, Charset defaultCharset) {
        this.url = url;
        this.defaultCharset = defaultCharset;
    }

    public InputStream open() throws IOException {
        return openConnection().getInputStream();
    }

    public Reader read() throws IOException {
        return new InputStreamReader(open(), defaultCharset);
    }

    public String mimeType() {
        return MimeTypes.guessFrom(url.getPath());
    }

    public long lastModified() {
        try {
            return openConnection().getLastModified();
        } catch (IOException e) {
            return 0;
        }
    }

    public int contentLength() {
        try {
            return openConnection().getContentLength();
        } catch (Exception e) {
            return 0;
        }
    }

    private URLConnection openConnection() throws IOException {
        return url.openConnection();
    }
}
