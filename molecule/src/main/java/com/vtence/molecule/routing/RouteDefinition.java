package com.vtence.molecule.routing;

import com.vtence.molecule.lib.Matcher;

public interface RouteDefinition {

    ViaClause map(String path);

    ViaClause map(Matcher<? super String> path);
}
