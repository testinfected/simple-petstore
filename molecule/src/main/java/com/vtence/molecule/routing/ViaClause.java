package com.vtence.molecule.routing;

import com.vtence.molecule.http.HttpMethod;
import com.vtence.molecule.lib.Matcher;

public interface ViaClause extends ToClause {

    ToClause via(HttpMethod... methods);

    ToClause via(Matcher<? super HttpMethod> method);
}
