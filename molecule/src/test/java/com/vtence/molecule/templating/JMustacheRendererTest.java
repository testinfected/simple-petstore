package com.vtence.molecule.templating;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static com.vtence.molecule.support.ResourceLocator.locateOnClasspath;
import static com.vtence.molecule.support.TemplateRenderer.render;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class JMustacheRendererTest {

    JMustacheRenderer mustache = new JMustacheRenderer().fromDir(locateOnClasspath(("views")));

    @Test public void
    rendersFromTemplatesFolder() throws IOException, SAXException {
        String view = render("hello").with(new Object() { String name = "World"; })
                .asString(mustache);
        assertThat("view", view, containsString("Hello World"));
    }

    @Test public void
    makesTemplateExtensionConfigurable() throws IOException, SAXException {
        mustache.extension("html");
        String view = render("cheers").asString(mustache);
        assertThat("view", view, containsString("#{"));
    }

    @Test public void
    makesNullValueConfigurable() throws IOException, SAXException {
        mustache.nullValue("World");
        String view = render("hello").with(new Object() { String name = null; }).asString(mustache);
        assertThat("view", view, containsString("Hello World"));
    }

    @Test public void
    makesDefaultValueConfigurable() throws IOException, SAXException {
        mustache.defaultValue("World");
        String view = render("hello").with(new Object()).asString(mustache);
        assertThat("view", view, containsString("Hello World"));
    }

    @Test public void
    assumesUtf8EncodingByDefault() throws IOException,SAXException {
        String view = render("utf-8").asString(mustache);
        assertThat("view", view, containsString("ægithales"));
    }

    @Test public void
    makesTemplateEncodingConfigurable() throws IOException, SAXException {
        mustache.encoding("utf-16be");
        String view = render("utf-16be").asString(mustache);
        assertThat("view", view, containsString("ægithales"));
    }

    @Test public void
    supportsPartialTemplates() throws IOException, SAXException {
        String view = render("full").with(new Object() { String name = "World"; })
                .asString(mustache);
        assertThat("view", view, containsString("Hello World"));
    }
}