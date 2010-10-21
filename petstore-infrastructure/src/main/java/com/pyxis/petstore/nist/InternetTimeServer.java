package com.pyxis.petstore.nist;

import com.pyxis.petstore.domain.time.Clock;
import com.pyxis.petstore.domain.time.SystemClock;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.lang.String.format;

public class InternetTimeServer {
    private final int port;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Time code format is
     * JJJJJ YR-MO-DA HH:MM:SS TT L H msADV UTC(NIST) OTM
     * see http://www.nist.gov/pml/div688/grp40/its.cfm
     **/
    private static final TimeZone utc = TimeZone.getTimeZone("UTC");
    private static final String DATE_FORMAT = "yy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String TIME_CODE_FORMAT = "\nJJJJJ %s %s TT L H msADV UTC(NIST) *\n";

    private ServerSocket server;
    private Clock internalClock = new SystemClock();

    public InternetTimeServer(int port) {
        this.port = port;
    }

    public void setInternalClock(Clock clock) {
        this.internalClock = clock;
    }

    public void start() throws IOException {
        startServer();
        startResponding();
    }

    private void startServer() throws IOException {
        server = new ServerSocket(port);
    }

    private void startResponding() {
        executor.execute(new Runnable() {
            public void run() {
                serveClients();
            }
        });
    }

    public void stop() throws IOException {
        server.close();
    }

    private void exceptionOccured(Exception e) {
        // plug-in monitor to externalize how exception are handled
    }

    private void serveClients() {
        while (shouldContinue()) {
            try {
                respondTo(server.accept());
            } catch (IOException e) {
                exceptionOccured(e);
            }
        }
    }

    private void respondTo(Socket client) {
        try {
            Writer writer = new OutputStreamWriter(client.getOutputStream());
            writer.write(timeCode());
            writer.flush();
        } catch (IOException e) {
            exceptionOccured(e);
        } finally {
            closeSocket(client);
        }
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    private boolean shouldContinue() {
        return !serverClosed();
    }

    private boolean serverClosed() {
        return server.isClosed();
    }

    public String timeCode() {
        return formatTimeCode(internalClock.now());
    }

    private static String formatTimeCode(Date pointInTime) {
        return format(TIME_CODE_FORMAT, date(pointInTime), time(pointInTime));
    }

    private static String time(Date pointInTime) {
        return formatter(TIME_FORMAT).format(pointInTime);
    }
                                    
    private static String date(Date pointInTime) {
        return formatter(DATE_FORMAT).format(pointInTime);
    }

    private static DateFormat formatter(String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(utc);
        return format;
    }

    public static InternetTimeServer listeningOnPort(int port) {
        return new InternetTimeServer(port);
    }

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        System.out.println("Listening on port " + port + "...");
        final InternetTimeServer server = InternetTimeServer.listeningOnPort(port);
        closeServerOnShutdown(server);
        server.start();
    }

    private static void closeServerOnShutdown(final InternetTimeServer server) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    System.out.println("\nShutting down...");
                    server.stop();
                    System.out.println("Bye.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
