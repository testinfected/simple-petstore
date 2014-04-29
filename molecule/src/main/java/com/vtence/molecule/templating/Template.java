package com.vtence.molecule.templating;

import com.vtence.molecule.Body;

import java.io.IOException;

public interface Template {

    Body render(Object context) throws IOException;
}
