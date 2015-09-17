package com.vtence.molecule.templating;

import com.vtence.molecule.Body;

import java.io.IOException;

public interface Template<T> {

    Body render(T context) throws IOException;
}
