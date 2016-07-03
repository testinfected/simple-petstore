package org.testinfected.petstore;

import com.vtence.molecule.templating.JMustacheRenderer;
import com.vtence.molecule.templating.Template;
import com.vtence.molecule.templating.Templates;
import org.testinfected.petstore.views.PlainPage;

import java.io.File;

public class PageStyles {

    private final Templates templates;

    public PageStyles(File inDir) {
        this.templates = new Templates(new JMustacheRenderer().encoding("utf-8")
                                                              .fromDir(inDir)
                                                              .extension("html")
                                                              .defaultValue(""));
    }

    public Template<PlainPage> main() {
        return templates.named("main");
    }
}
