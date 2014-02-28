package org.testinfected.molecule.routing;

import org.testinfected.molecule.HttpMethod;

public interface ViaClause extends ToClause {

    ToClause via(HttpMethod method);
}
