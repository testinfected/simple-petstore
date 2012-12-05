package org.testinfected.support.middlewares;

import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.support.SimpleRequest;
import org.testinfected.support.SimpleResponse;
import org.testinfected.time.Clock;

import java.io.Serializable;
import java.nio.charset.Charset;
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

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        forward(request, response);
        String msg = String.format(COMMON_LOG_FORMAT,
                request.ip(),
                "-",
                currentTime(),
                request.method(),
                request.uri(),
                request.protocol(),
                response.statusCode(),
                contentLengthOrHyphen(response));
        logger.info(msg);
    }

    private String currentTime() {
        DATE_FORMAT.setTimeZone(TimeZone.getDefault());
        return DATE_FORMAT.format(clock.now());
    }

    private Serializable contentLengthOrHyphen(Response response) {
        return response.contentLength() > 0 ? response.contentLength() : "-";
    }
}
