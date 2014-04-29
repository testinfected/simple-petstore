package com.vtence.molecule.support;

import com.vtence.molecule.lib.Clock;

import java.util.Date;

public class Delorean implements Clock {

    private long timeTravel = 0;
    private Date frozenAt;

    public Date now() {
        return frozen() ? frozenAt : new Date(currentTime());
    }

    public Date freeze() {
        frozenAt = now();
        return frozenAt;
    }

    public void unfreeze() {
        frozenAt = null;
    }

    public void travelInTime(long offsetInMillis) {
        this.timeTravel = offsetInMillis;
    }

    public void back() {
        travelInTime(0);
    }

    private boolean frozen() {
        return frozenAt != null;
    }

    private long currentTime() {
        return System.currentTimeMillis() + timeTravel;
    }
}
