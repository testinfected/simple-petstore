package org.testinfected.molecule.session;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PeriodicChores {

    private static final int EVERY_MINUTE = 60;

    private final ScheduledExecutorService scheduler;
    private final SessionHouse sessions;
    private final SessionHouse.Work houseWork;

    private long choresInterval;
    private ScheduledFuture chores;

    public PeriodicChores(ScheduledExecutorService scheduler, SessionHouse sessions, SessionHouse.Work houseWork) {
        this(scheduler, sessions, houseWork, EVERY_MINUTE, TimeUnit.SECONDS);
    }

    public PeriodicChores(ScheduledExecutorService scheduler, SessionHouse sessions, SessionHouse.Work houseWork, long choresInterval, TimeUnit timeUnit) {
        this.scheduler = scheduler;
        this.sessions = sessions;
        this.houseWork = houseWork;
        this.choresInterval = TimeUnit.MILLISECONDS.convert(choresInterval, timeUnit);
    }

    public void start() {
        chores = scheduler.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                sessions.houseKeeping(houseWork);
            }
        }, choresInterval, choresInterval, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        chores.cancel(false);
    }
}
