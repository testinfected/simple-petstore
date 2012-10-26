package test.support.org.testinfected.petstore.web;

import org.cyberneko.html.parsers.DOMParser;
import org.testinfected.petstore.ExceptionImposter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.StringReader;

public final class HTMLDocument {

    private HTMLDocument() {}

    public static Document from(String dom) {
        try {
            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(new StringReader(dom)));
            return parser.getDocument();
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public static Element toElement(String dom) {
        return from(dom).getDocumentElement();
    }
}
