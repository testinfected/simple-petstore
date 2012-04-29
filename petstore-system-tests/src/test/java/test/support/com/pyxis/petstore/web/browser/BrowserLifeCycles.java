package test.support.com.pyxis.petstore.web.browser;

import java.util.HashMap;
import java.util.Map;

public class BrowserLifeCycles {

    public static enum Option { passing, lasting }

    private final Map<Option, BrowserLifeCycle> available = new HashMap<Option, BrowserLifeCycle>();

    public BrowserLifeCycles() {
        populate();
    }

    private void populate() {
        available.put(Option.passing, new PassingBrowser());
        available.put(Option.lasting, new LastingBrowser());
    }

    public BrowserLifeCycle select(String lifeCycle) {
        return available.get(Option.valueOf(lifeCycle));
    }

    public BrowserLifeCycle select(Option lifeCycle) {
        return available.get(lifeCycle);
    }
}
