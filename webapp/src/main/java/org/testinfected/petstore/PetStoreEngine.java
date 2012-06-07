package org.testinfected.petstore;

import org.simpleframework.http.Address;
import org.simpleframework.http.resource.Resource;
import org.simpleframework.http.resource.ResourceEngine;

import java.nio.charset.Charset;

public class PetStoreEngine implements ResourceEngine {

    private final ResourceLoader resourceLoader;
    private final String charset;
    private final Renderer renderer;

    public PetStoreEngine(final ResourceLoader resourceLoader, final Charset charset) {
        this.resourceLoader = resourceLoader;
        this.charset = charset.name();
        this.renderer = new MustacheRendering(resourceLoader, charset.name());
    }

    public Resource resolve(Address target) {
        if (target.getPath().getPath().startsWith("/images")) {
            return new StaticResource(resourceLoader, renderer, charset);
        }
        if (target.getPath().getPath().startsWith("/stylesheets")) {
            return new StaticResource(resourceLoader, renderer, charset);
        }
        return new ApplicationResource(renderer, charset);
    }
}
