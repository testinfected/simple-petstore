package org.testinfected.molecule.routing;

import org.testinfected.molecule.Application;

public interface ToClause extends EndClause {

    EndClause to(Application application);
}
