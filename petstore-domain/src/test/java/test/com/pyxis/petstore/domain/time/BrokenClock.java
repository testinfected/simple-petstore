package test.com.pyxis.petstore.domain.time;

import com.pyxis.petstore.domain.time.Clock;

import java.util.Date;

public class BrokenClock implements Clock {
    private final Date pointInTime;

    public BrokenClock(Date pointInTime) {
        this.pointInTime = pointInTime;
    }

    public static Clock clockStoppedAt(Date pointInTime) {
        return new BrokenClock(pointInTime);
    }

    public Date now() {
        return pointInTime;
    }
}
