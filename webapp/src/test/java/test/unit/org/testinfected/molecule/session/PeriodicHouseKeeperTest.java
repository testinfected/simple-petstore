package test.unit.org.testinfected.molecule.session;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.DeterministicScheduler;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.molecule.session.HouseKeeping;
import org.testinfected.molecule.session.PeriodicHouseKeeper;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PeriodicHouseKeeperTest {

    static final int CHORES_INTERVAL = 50;

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    CountChores count = new CountChores();
    DeterministicScheduler scheduler = new DeterministicScheduler();

    PeriodicHouseKeeper houseKeeper = new PeriodicHouseKeeper(scheduler, count, CHORES_INTERVAL, TimeUnit.MILLISECONDS);

    @Before public void
    startScavenger() {
        houseKeeper.start();
    }

    @After public void
    stopScavenger() {
        houseKeeper.stop();
    }

    @Test public void
    scheduleHouseKeepingPeriodicallyAtFixedDelays() throws Exception {
        assertHouseKeepingChores(0);
        tick(CHORES_INTERVAL);
        assertHouseKeepingChores(1);
        tick(CHORES_INTERVAL / 2);
        assertHouseKeepingChores(1);
        tick(CHORES_INTERVAL / 2);
        assertHouseKeepingChores(2);
        tick(CHORES_INTERVAL);
        assertHouseKeepingChores(3);
    }

    private void tick(int millis) {
        scheduler.tick(millis, TimeUnit.MILLISECONDS);
    }

    private void assertHouseKeepingChores(int operand) {
        assertThat("housekeeping chores", count.chores, equalTo(operand));
    }

    private class CountChores implements HouseKeeping {
        private int chores;

        public void perform() {
            chores++;
        }
    }
}
