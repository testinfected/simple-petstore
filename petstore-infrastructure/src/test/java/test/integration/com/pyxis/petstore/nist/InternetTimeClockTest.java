package test.integration.com.pyxis.petstore.nist;

import com.pyxis.petstore.nist.InternetTimeClock;
import com.pyxis.petstore.nist.InternetTimeServer;
import com.pyxis.petstore.nist.TimeServerDialect;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.com.pyxis.petstore.domain.time.BrokenClock.clockedStoppedAt;
import static test.support.com.pyxis.petstore.builders.DateBuilder.aDate;

@RunWith(JMock.class)
public class InternetTimeClockTest {

    Mockery context = new JUnit4Mockery();
    TimeServerDialect timeServerDialect = context.mock(TimeServerDialect.class);
    int serverPort = 10013;
    InternetTimeServer server = InternetTimeServer.listeningOnPort(serverPort);
    Date serverTime = aDate().build();

    InternetTimeClock clock = new InternetTimeClock("localhost", serverPort);

    @Before
    public void startTimeServer() throws IOException {
        server.setInternalClock(clockedStoppedAt(serverTime));
        server.start();
    }

    @After
    public void stopTimeServer() throws IOException {
        server.stop();
    }

    @Test public void
    obtainsCurrentTimeFromRemoteServer() throws Exception {
        context.checking(new Expectations() {{
            oneOf(timeServerDialect).translate(server.timeCode()); will(returnValue(serverTime));
        }});
        clock.speak(timeServerDialect);
        assertThat("internet time", clock.now(), equalTo(serverTime));
    }

}

