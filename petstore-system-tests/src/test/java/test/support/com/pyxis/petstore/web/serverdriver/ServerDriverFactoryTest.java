package test.support.com.pyxis.petstore.web.serverdriver;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static test.support.com.pyxis.petstore.web.serverdriver.AbstractServerDriverFactory.serverDriverFactory;

public class ServerDriverFactoryTest {

    @Test
    public void
    startsServerWhenCreatingTheDriver() throws Exception {
        ServerDriver serverDriver = serverDriverFactory().newServerDriver();
        assertThat(serverDriver.isServerStarted(), is(true));
    }

}
