package com.vtence.molecule.session;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PeriodicSessionHouseKeeping {

    private static final long EVERY_HOUR = TimeUnit.HOURS.toSeconds(1);

    private final ScheduledExecutorService scheduler;
    private final SessionHouse sessions;

    private long choresInterval;
    private ScheduledFuture chores;

    public PeriodicSessionHouseKeeping(ScheduledExecutorService scheduler, SessionHouse sessions) {
        this(scheduler, sessions, EVERY_HOUR, TimeUnit.SECONDS);
    }

    public PeriodicSessionHouseKeeping(ScheduledExecutorService scheduler, SessionHouse sessions, long choresInterval, TimeUnit timeUnit) {
        this.scheduler = scheduler;
        this.sessions = sessions;
        this.choresInterval = TimeUnit.MILLISECONDS.convert(choresInterval, timeUnit);
    }

    public void start() {
        chores = scheduler.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                sessions.houseKeeping();
            }
        }, choresInterval, choresInterval, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        chores.cancel(false);
    }
}
