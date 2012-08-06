package org.testinfected.petstore;

import org.testinfected.petstore.util.Charsets;

import java.io.File;
import java.nio.charset.Charset;

public class WebLayout {

    public static final String STANDARD_TEMPLATE_DIRECTORY = "templates";
    public static final String STANDARD_ASSET_DIRECTORY = "assets";
    public static final String STANDARD_PAGE_DIRECTORY = "pages";
    public static final String STANDARD_LAYOUT_DIRECTORY = "layout";

    public static WebLayout standard(String root) {
        return standard(new File(root));
    }

    public static WebLayout standard(File root) {
        return new WebLayout(
                new File(root, STANDARD_ASSET_DIRECTORY),
                new File(root, STANDARD_TEMPLATE_DIRECTORY),
                new File(new File(root, STANDARD_TEMPLATE_DIRECTORY), STANDARD_PAGE_DIRECTORY),
                new File(new File(root, STANDARD_TEMPLATE_DIRECTORY), STANDARD_LAYOUT_DIRECTORY),
                Charsets.UTF_8);
    }
    public final File assets;
    public final File templates;
    public final File pages;
    public final File layouts;
    public final Charset encoding;

    public WebLayout(File assets, File templates, File pages, File layouts, Charset encoding) {
        this.assets = assets;
        this.templates = templates;
        this.pages = pages;
        this.layouts = layouts;
        this.encoding = encoding;
    }
}
