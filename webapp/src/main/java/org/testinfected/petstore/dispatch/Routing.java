package org.testinfected.petstore.dispatch;

// todo consider dispatching to Applications instead
// and having having adapters for endpoints
// In that scenario, Route would be nothing more than an application + a request matcher
// StaticRoute would do nothing more than delegating to a request matcher and an application
public interface Routing {

    void dispatch(Dispatch.Request request, Dispatch.Response response) throws Exception;
}
