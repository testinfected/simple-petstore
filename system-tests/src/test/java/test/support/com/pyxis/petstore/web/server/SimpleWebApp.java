package test.support.com.pyxis.petstore.web.server;

import org.simpleframework.http.Address;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.resource.Resource;
import org.simpleframework.http.resource.ResourceContainer;
import org.simpleframework.http.resource.ResourceEngine;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.testinfected.hamcrest.ExceptionImposter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SimpleWebApp implements ServerDriver {

    private final int port;

    private Connection connection;

    public SimpleWebApp(int port) {
        this.port = port;
    }
    
    public class HelloWorldHandler implements Resource {

        public void handle(Request request, Response response) {
            PrintStream body;
            try {
                body = response.getPrintStream();
                long time = System.currentTimeMillis();

                response.set("Content-Type", "text/html; charset=utf-8");
                response.set("Server", "JPetStore/1.0 (Simple 4.1.21)");
                response.setDate("Date", time);
                response.setDate("Last-Modified", time);

                body.println(
                        "<html>\n" +
                        "<body>\n" +
                        "<form action=\"/logout\" method=\"post\">\n" +
                        "    <input type=\"hidden\" name=\"_method\" value=\"delete\"/>\n" +
                        "    <button id=\"logout\"></button>\n" +
                        "</form>\n" +
                        "</body>\n" +
                        "</html");
                body.close();
            } catch (IOException e) {
                response.setText(stackTraceOf(e));
                response.setCode(Status.INTERNAL_SERVER_ERROR.getCode());
            }
        }
    }

    private String stackTraceOf(Exception exception) {
        StringWriter capture = new StringWriter();
        exception.printStackTrace(new PrintWriter(capture));
        capture.flush();
        return capture.toString();
    }

    public void start() {
        try {
            connection = new SocketConnection(new ResourceContainer(new ResourceEngine() {
                public Resource resolve(Address target) {
                    return new HelloWorldHandler();
                }
            }));
            SocketAddress address = new InetSocketAddress(port);
            connection.connect(address);
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public void stop() {
        try {
            connection.close();
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }
}