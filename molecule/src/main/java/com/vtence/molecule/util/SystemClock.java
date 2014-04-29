package com.vtence.molecule.util;

import java.util.Date;

public class SystemClock implements Clock {
    public Date now() {
        return new Date();
    }
}
