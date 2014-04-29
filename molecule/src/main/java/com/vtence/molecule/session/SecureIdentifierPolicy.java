package com.vtence.molecule.session;

import java.util.UUID;

public class SecureIdentifierPolicy implements SessionIdentifierPolicy {

    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
