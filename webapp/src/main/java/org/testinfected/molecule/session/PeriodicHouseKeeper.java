package org.testinfected.molecule.session;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PeriodicHouseKeeper {

    private final ScheduledExecutorService scheduler;
    private final HouseKeeping houseKeeping;

    private long choresInterval;
    private ScheduledFuture chores;

    public PeriodicHouseKeeper(ScheduledExecutorService scheduler, HouseKeeping houseKeeping) {
        this(scheduler, houseKeeping, 60, TimeUnit.SECONDS);
    }

    public PeriodicHouseKeeper(ScheduledExecutorService scheduler, HouseKeeping houseKeeping, long choresInterval, TimeUnit timeUnit) {
        this.scheduler = scheduler;
        this.houseKeeping = houseKeeping;
        this.choresInterval = TimeUnit.MILLISECONDS.convert(choresInterval, timeUnit);
    }

    public void start() {
        chores = scheduler.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                houseKeeping.perform();
            }
        }, choresInterval, choresInterval, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        chores.cancel(false);
    }
}
