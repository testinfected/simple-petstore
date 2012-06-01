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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
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

        public static final String UTF_8 = "utf-8";

        public void handle(Request request, Response response) {
            try {
                long time = System.currentTimeMillis();
                response.set("Content-Type", "text/html; charset=" + charset());
                response.set("Server", "JPetStore/1.0 (Simple 4.1.21)");
                response.setDate("Date", time);
                response.setDate("Last-Modified", time);
                OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream(), charset());

                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Reader source = new InputStreamReader(classLoader.getResourceAsStream("templates/main.html"), charset());
                Template template = Mustache.compiler().compile(source);
                Map<String, String> data = new HashMap<String, String>();
                data.put("title", "PetStore");
                template.execute(data, out);
                out.flush();
                response.close();
            } catch (Exception e) {
                response.setText(stackTraceOf(e));
                response.setCode(Status.INTERNAL_SERVER_ERROR.getCode());
            }
        }

        private String charset() {
            return UTF_8;
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