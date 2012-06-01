package org.testinfected.petstore;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.simpleframework.http.Address;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.resource.Resource;
import org.simpleframework.http.resource.ResourceContainer;
import org.simpleframework.http.resource.ResourceEngine;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public class PetStore {

    private final int port;

    private Connection connection;

    public PetStore(int port) {
        this.port = port;
    }

    public class MainHandler implements Resource {

        public void handle(Request request, Response response) {
            PrintStream body;
            try {
                body = response.getPrintStream();
                long time = System.currentTimeMillis();

                response.set("Content-Type", "text/html; charset=utf-8");
                response.set("Server", "JPetStore/1.0 (Simple 4.1.21)");
                response.setDate("Date", time);
                response.setDate("Last-Modified", time);

                String text = "<html>\n" +
                        "<head>\n" +
                        "    <meta content=\"text/html;charset=UTF-8\" http-equiv=\"Content-Type\"/>\n" +
                        "    <title>{{title}}</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<form action=\"/logout\" method=\"post\">\n" +
                        "    <input type=\"hidden\" name=\"_method\" value=\"delete\"/>\n" +
                        "    <button id=\"logout\"></button>\n" +
                        "</form>\n" +
                        "</body>\n" +
                        "</html>";
                Template template = Mustache.compiler().compile(text);
                Map<String, String> data = new HashMap<String, String>();
                data.put("title", "PetStore");
                body.println(template.execute(data));
                body.println();
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

    public void start() throws Exception {
        connection = new SocketConnection(new ResourceContainer(new ResourceEngine() {
            public Resource resolve(Address target) {
                return new MainHandler();
            }
        }));
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
    }

    public void stop() throws Exception {
        connection.close();
    }
}