package test.support.org.testinfected.petstore.templating;

import org.mozilla.universalchardet.CharsetListener;
import org.mozilla.universalchardet.UniversalDetector;
import org.testinfected.petstore.Streams;

import java.io.IOException;
import java.io.InputStream;

public class CharsetDetector {

    public static String detectCharset(InputStream data) throws IOException {
        UniversalDetector charsetDetector = new UniversalDetector(new CharsetListener() {
            public void report(String charset) {
            }
        });
        byte[] body = Streams.toByteArray(data);
        charsetDetector.handleData(body, 0, body.length);
        charsetDetector.dataEnd();
        return charsetDetector.getDetectedCharset();
    }

    private CharsetDetector() {
    }
}
