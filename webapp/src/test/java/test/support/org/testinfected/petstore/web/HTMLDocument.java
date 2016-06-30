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

    public static Document from(String dom) throws IOException, SAXException {
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader(dom)));
        return parser.getDocument();
    }

    public static Element toElement(String dom) throws IOException, SAXException {
        return from(dom).getDocumentElement();
    }
}
