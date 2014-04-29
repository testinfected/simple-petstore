package com.vtence.molecule.middlewares;

import com.vtence.molecule.HttpMethod;
import com.vtence.molecule.Request;
import com.vtence.molecule.util.RequestWrapper;
import com.vtence.molecule.Response;

import java.io.IOException;

public class HttpMethodOverride extends AbstractMiddleware {

    public static final String METHOD_OVERRIDE_PARAMETER = "_method";

    public void handle(Request request, Response response) throws Exception {
        if (overrideDetected(request)) {
            request = overrideMethod(request);
        }
        forward(request, response);
    }

    private Request overrideMethod(final Request request) throws IOException {
        if (HttpMethod.valid(methodOverride(request))) {
            return new RequestWrapper(request) {
                public HttpMethod method() {
                    return HttpMethod.valueOf(methodOverride(request).toUpperCase());
                }
            };
        }
        return request;
    }

    private boolean overrideDetected(Request request) throws IOException {
        return methodOverride(request) != null && request.method() == HttpMethod.POST;
    }

    private String methodOverride(Request request) {
        return request.parameter(METHOD_OVERRIDE_PARAMETER);
    }
}
