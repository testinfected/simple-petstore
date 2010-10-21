package com.pyxis.petstore.domain.time;

import java.util.Date;

public class SystemClock implements Clock {

    public Date now() {
        return new Date();
    }
}
