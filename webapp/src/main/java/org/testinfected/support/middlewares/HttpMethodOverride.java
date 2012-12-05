package org.testinfected.support.middlewares;

import org.testinfected.support.*;

import java.io.IOException;
import java.nio.charset.Charset;

public class HttpMethodOverride extends AbstractMiddleware {

    public static String METHOD_OVERRIDE_PARAMETER = "_method";

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        if (overrideDetected(request)) {
            request = overrideMethod(request);
        }
        forward(request, response);
    }

    // this will eventually go away
    protected void forward(final Request rq, Response response) throws Exception {
        super.forward(new SimpleRequest(new org.simpleframework.http.RequestWrapper(rq.unwrap(org.simpleframework.http.Request.class)) {
            public String getMethod() {
                return rq.method();
            }
        }), response);
    }

    private Request overrideMethod(final Request request) throws IOException {
        if (HttpMethod.valid(methodOverride(request))) {
            return new RequestWrapper(request) {
                public String method() {
                    return methodOverride(request).toUpperCase();
                }
            };
        }
        return request;
    }

    private boolean overrideDetected(Request request) throws IOException {
        return methodOverride(request) != null;
    }

    private String methodOverride(Request request) {
        return request.parameter(METHOD_OVERRIDE_PARAMETER);
    }
}
