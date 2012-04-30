package test.support.com.pyxis.petstore.web.browser;

import java.util.HashMap;
import java.util.Map;

public class BrowserLifeCycles {

    public static enum Option { passing, lasting, remote }

    private final Map<Option, BrowserLifeCycle> available = new HashMap<Option, BrowserLifeCycle>();

    public BrowserLifeCycles(BrowserProperties properties) {
        available.put(Option.passing, new PassingBrowser());
        available.put(Option.lasting, new LastingBrowser());
        available.put(Option.remote, new RemoteBrowser(properties));
    }

    public BrowserLifeCycle select(String lifeCycle) {
        return available.get(Option.valueOf(lifeCycle));
    }

    public BrowserLifeCycle select(Option lifeCycle) {
        return available.get(lifeCycle);
    }
}
