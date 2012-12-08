package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
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
