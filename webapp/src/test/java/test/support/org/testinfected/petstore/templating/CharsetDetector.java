package test.support.org.testinfected.petstore.templating;

import org.apache.commons.io.IOUtils;
import org.mozilla.universalchardet.CharsetListener;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.IOException;
import java.io.InputStream;

public class CharsetDetector {
    
    public static String detectCharset(InputStream data) throws IOException {
        UniversalDetector charsetDetector = new UniversalDetector(new CharsetListener() {
            public void report(String charset) {
            }
        });
        byte[] body = IOUtils.toByteArray(data);
        charsetDetector.handleData(body, 0, body.length);
        charsetDetector.dataEnd();
        return charsetDetector.getDetectedCharset();
    }
    
    private CharsetDetector() {}
}
