package com.vtence.molecule.routing;

import com.vtence.molecule.Application;

public interface ToClause extends EndClause {

    EndClause to(Application application);
}
