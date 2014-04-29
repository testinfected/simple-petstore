package com.vtence.molecule.routing;

import com.vtence.molecule.HttpMethod;

public interface ViaClause extends ToClause {

    ToClause via(HttpMethod method);
}
