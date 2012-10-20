package org.testinfected.petstore.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.time.Clock;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.logging.Logger;

public class ApacheCommonLogger extends AbstractMiddleware {

    private static final String COMMON_LOG_FORMAT = "%s - %s [%s] \"%s %s %s\" %s %s";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");

    private final Logger logger;
    private final Clock clock;

    public ApacheCommonLogger(Logger logger, Clock clock) {
        this.logger = logger;
        this.clock = clock;
    }

    public void handle(Request request, Response response) throws Exception {
        forward(request, response);
        String msg = String.format(COMMON_LOG_FORMAT,
                ipAddressOfClient(request),
                userId(request),
                currentTime(),
                httpMethod(request),
                pathInfo(request),
                httpVersion(request),
                statusCode(response),
                contentLengthOrHyphen(response));
        logger.info(msg);
    }

    private String ipAddressOfClient(Request request) {
        return request.getClientAddress().getAddress().getHostAddress();
    }

    private String userId(Request request) {
        return "-";
    }

    private String currentTime() {
        DATE_FORMAT.setTimeZone(TimeZone.getDefault());
        return DATE_FORMAT.format(clock.now());
    }

    private String httpMethod(Request request) {
        return request.getMethod();
    }

    private String pathInfo(Request request) {
        return request.getTarget();
    }

    private String httpVersion(Request request) {
        return String.format("HTTP/%s.%s", request.getMajor(), request.getMinor());
    }

    private int statusCode(Response response) {
        return response.getCode();
    }

    private Serializable contentLengthOrHyphen(Response response) {
        return response.getContentLength() > 0 ? response.getContentLength() : "-";
    }
}
