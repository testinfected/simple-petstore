package com.vtence.molecule.decoration;

import com.vtence.molecule.Body;
import com.vtence.molecule.Request;

import java.io.IOException;
import java.util.Map;

public interface Decorator {

    Body merge(Request request, Map<String, String> content) throws IOException;
}
