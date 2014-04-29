package com.vtence.molecule.lib;

import com.vtence.molecule.support.MockResponse;
import com.vtence.molecule.helpers.Charsets;
import org.junit.Test;

import java.io.IOException;

import static com.vtence.molecule.helpers.Charsets.ISO_8859_1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TextBodyTest {

    TextBody body = new TextBody();
    MockResponse response = new MockResponse();

    @Test public void
    appendsAndRendersText() throws IOException {
        body.append("The entire");
        body.append(" text");
        body.append(" body");
        response.body(body);
        assertThat("text", body.text(), equalTo("The entire text body"));
        response.assertBody(body.text());
        response.assertContentSize(body.size(ISO_8859_1));
    }

    @Test public void
    usesSpecifiedTextEncoding() throws IOException {
        body.append("De drôles d'œufs abîmés");
        response.contentType("text/plain; charset=UTF-8");
        response.body(body);
        response.assertContentEncodedAs("UTF-8");
        response.assertContentSize(body.size(Charsets.UTF_8));
    }
}
