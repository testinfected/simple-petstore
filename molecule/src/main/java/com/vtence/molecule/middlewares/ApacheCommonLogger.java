package com.vtence.molecule.middlewares;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.lib.AbstractMiddleware;
import com.vtence.molecule.lib.Clock;
import com.vtence.molecule.lib.SystemClock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.logging.Logger;

public class ApacheCommonLogger extends AbstractMiddleware {

    private static final String COMMON_LOG_FORMAT = "%s - %s [%s] \"%s %s %s\" %s %s";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");

    private final Logger logger;
    private final Clock clock;
    private final TimeZone timeZone;

    public ApacheCommonLogger(Logger logger) {
        this(logger, new SystemClock());
    }

    public ApacheCommonLogger(Logger logger, Clock clock) {
        this(logger, clock, TimeZone.getDefault());
    }

    public ApacheCommonLogger(Logger logger, Clock clock, TimeZone timeZone) {
        this.logger = logger;
        this.clock = clock;
        this.timeZone = timeZone;
    }

    public void handle(Request request, Response response) throws Exception {
        forward(request, response);
        String msg = String.format(COMMON_LOG_FORMAT,
                request.remoteIp(),
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
        DATE_FORMAT.setTimeZone(timeZone);
        return DATE_FORMAT.format(clock.now());
    }

    private Object contentLengthOrHyphen(Response response) {
        return response.size() > 0 ? response.size() : "-";
    }
}
