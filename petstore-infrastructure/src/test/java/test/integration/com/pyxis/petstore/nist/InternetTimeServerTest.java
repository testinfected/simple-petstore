package test.integration.com.pyxis.petstore.nist;

import com.pyxis.petstore.nist.InternetTimeServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.integration.com.pyxis.petstore.nist.BrokenClock.clockedStoppedAt;
import static test.support.com.pyxis.petstore.builders.DateBuilder.aDate;

public class InternetTimeServerTest {

    int serverPort = 10013;
    InternetTimeServer server = InternetTimeServer.listeningOnPort(serverPort);
    Date serverTime = aDate().onCalendar(2010, 10, 23).atTime(15, 15, 20).build();

    int clientCount = 25;
    ExecutorService clients = Executors.newCachedThreadPool();

    @Before public void
    startServer() throws IOException {
        server.setInternalClock(clockedStoppedAt(serverTime));
        server.start();
    }

    @After public void
    stopServer() throws IOException {
        server.stop();
        clients.shutdownNow();
    }

    @Test public void
    providesCurrentTimeToClientBasedOnInternalClockTime() throws Exception {
        Future<String> timeCode = clients.submit(new TimeRequest());
        String response = waitFor(timeCode, 100);
        assertThat("server response", response, equalTo("JJJJJ 10-10-23 15:15:20 TT L H msADV UTC(NIST) *"));
    }

    private String waitFor(Future<String> timeCode, int timeout) throws Exception {
        return timeCode.get(timeout, TimeUnit.MILLISECONDS);
    }

    @Test public void
    supportsSeveralClientsAtTheSameTime() throws Exception {
        Collection<Future<String>> timeCodes = new ArrayList<Future<String>>();
        for (int i = 1; i <= clientCount; i++) {
            timeCodes.add(clients.submit(new TimeRequest()));
        }

        assertThat("clients served", clientsServed(timeCodes), equalTo(clientCount));
    }

    private int clientsServed(Collection<Future<String>> timeCodes) throws Exception {
        int requestsServed = 0;
        for (Future<String> timeCode : timeCodes) {
            try {
                waitFor(timeCode, 100 * clientCount);
                requestsServed++;
            } catch (TimeoutException skip) {
            }
        }
        return requestsServed;
    }

    private class TimeRequest implements Callable<String> {

        public String call() throws Exception {
            Socket socket = null;
            try {
                socket = new Socket("localhost", serverPort);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                nextLine(reader);
                return nextLine(reader);
            } finally {
                close(socket);
            }
        }

        private String nextLine(BufferedReader reader) throws IOException {
            return reader.readLine();
        }

        private void close(Socket socket) throws IOException {
            if (socket == null) return;
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
