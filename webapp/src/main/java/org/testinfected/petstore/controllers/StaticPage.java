package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.Page;

public class StaticPage implements Application {
    private final Page page;

    public StaticPage(Page page) {
        this.page = page;
    }

    public void handle(Request request, Response response) throws Exception {
        page.render(response, null);
    }
}
