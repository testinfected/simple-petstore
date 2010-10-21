package com.pyxis.petstore.nist;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;

public class FrozenTimeServer {

    /**
     * Time code format is
     * JJJJJ YR-MO-DA HH:MM:SS TT L H msADV UTC(NIST) OTM
     * see http://www.nist.gov/pml/div688/grp40/its.cfm
     **/
    private static final TimeZone utc = TimeZone.getTimeZone("UTC");
    private static final String DATE_FORMAT = "yy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:SS";
    private static final String TIME_CODE_FORMAT = "JJJJJ %s %s TT L H msADV UTC(NIST) *";

    private final String output;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private ServerSocket server;

    public FrozenTimeServer(String output) {
        this.output = output;
    }

    public void start(int port) throws IOException {
        server = new ServerSocket(port);
//        while (!server.isClosed()) {
//             Socket socket = server.accept();
        
        executor.submit(new Callable<Object>() {
            public Object call() throws Exception {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write("\n");
                    writer.write(output);
                    writer.flush();
                    socket.close();
                }
                return null;
            }
        });
    }

    public void stop() throws IOException {
        server.close();
        executor.shutdown();
    }

    public String output() {
        return output;
    }

    public static FrozenTimeServer at(String serverOutput) {
        return new FrozenTimeServer(serverOutput);
    }

    public static FrozenTimeServer atPointInTime(Date pointInTime) {
        return at(response(pointInTime));
    }

    private static String response(Date pointInTime) {
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
}
