package test.support.org.testinfected.petstore.web;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

public final class HTMLDocument {

    private HTMLDocument() {}

    public static Document from(String dom) {
        try {
            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(new StringReader(dom)));
            return parser.getDocument();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Element toElement(String dom) {
        return from(dom).getDocumentElement();
    }
}
