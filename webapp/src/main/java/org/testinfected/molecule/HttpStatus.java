package org.testinfected.molecule;

/**
 * Shamelessly copied from {@link org.simpleframework.http.Status}
 */
public enum HttpStatus {

    /**
     * This represents a successful response of a targeted request.
     */
    OK(200, "OK"),

    /**
     * This is used to signify that a resource was created successfully.
     */
    CREATED(201, "Created"),

    /**
     * This is used to signify that the request has been accepted.
     */
    ACCEPTED(202, "Accepted"),

    /**
     * This represents a response that contains no response content.
     */
    NO_CONTENT(204, "No Content"),

    /**
     * This is used to represent a response that resets the content.
     */
    RESET_CONTENT(205, "Reset Content"),

    /**
     * This is used to represent a response that has partial content.
     */
    PARTIAL_CONTENT(206, "Partial Content"),

    /**
     * This is used to represent a response where there are choices.
     */
    MULTIPLE_CHOICES(300, "Multiple Choices"),

    /**
     * This is used to represent a target resource that has moved.
     */
    MOVED_PERMANENTLY(301, "Moved Permanently"),

    /**
     * This is used to represent a resource that has been found.
     */
    FOUND(302, "Found"),

    /**
     * This is used to tell the client to see another HTTP resource.
     */
    SEE_OTHER(303, "See Other"),

    /**
     * This is used in response to a target that has not been modified.
     */
    NOT_MODIFIED(304, "Not Modified"),

    /**
     * This is used to tell the client that it should use a proxy.
     */
    USE_PROXY(305, "Use Proxy"),

    /**
     * This is used to redirect the client to a resource that has moved.
     */
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),

    /**
     * This is used to tell the client they have send an invalid request.
     */
    BAD_REQUEST(400, "Bad Request"),

    /**
     * This is used to tell the client that authorization is required.
     */
    UNAUTHORIZED(401, "Unauthorized"),

    /**
     * This is used to tell the client that payment is required.
     */
    PAYMENT_REQUIRED(402, "Payment Required"),

    /**
     * This is used to tell the client that the resource is forbidden.
     */
    FORBIDDEN(403, "Forbidden"),

    /**
     * This is used to tell the client that the resource is not found.
     */
    NOT_FOUND(404, "Not Found"),

    /**
     * This is used to tell the client that the method is not allowed.
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    /**
     * This is used to tell the client the request is not acceptable.
     */
    NOT_ACCEPTABLE(406, "Not Acceptable"),

    /**
     * This is used to tell the client that authentication is required.
     */
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),

    /**
     * This is used to tell the client that the request has timed out.
     */
    REQUEST_TIMEOUT(408, "Request Timeout"),

    /**
     * This is used to tell the client that there has been a conflict.
     */
    CONFLICT(409, "Conflict"),

    /**
     * This is used to tell the client that the resource has gone.
     */
    GONE(410, "Gone"),

    /**
     * This is used to tell the client that a request length is needed.
     */
    LENGTH_REQUIRED(411, "Length Required"),

    /**
     * This is used to tell the client that a precondition has failed.
     */
    PRECONDITION_FAILED(412, "Precondition Failed"),

    /**
     * This is used to tell the client that the request body is too big.
     */
    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),

    /**
     * This is used to tell the client that the request URI is too long.
     */
    REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),

    /**
     * This is used to tell the client that the content type is invalid.
     */
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),

    /**
     * This is used to tell the client that the range is invalid.
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),

    /**
     * This is used to tell the client that the expectation has failed.
     */
    EXPECTATION_FAILED(417, "Expectation Failed"),

    /**
     * This is sent when the request has caused an internal server error.
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    /**
     * This is used to tell the client the resource is not implemented.
     */
    NOT_IMPLEMENTED(501, "Not Implemented"),

    /**
     * This is used to tell the client that the gateway is invalid.
     */
    BAD_GATEWAY(502, "Bad Gateway"),

    /**
     * This is used to tell the client the resource is unavailable.
     */
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    /**
     * This is used to tell the client there was a gateway timeout.
     */
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),

    /**
     * This is used to tell the client the request version is invalid.
     */
    VERSION_NOT_SUPPORTED(505, "Version Not Supported");

    /**
     * This is the the textual description of the response state.
     */
    public final String text;

    /**
     * This is the code used in the HTTP response
     * message to tell the client what kind of response this represents.
     */
    public final int code;

    /**
     * Constructor for the <code>HttpStatus</code> object. This will create
     * a status object that is used to represent a response state. It
     * contains a status code and a description of that code.
     *
     * @param code this is the code that is used for this status
     * @param reason this is the description used for the status
     */
    private HttpStatus(int code, String reason) {
        this.text = reason;
        this.code = code;
    }

    public static HttpStatus forCode(int code) {
        for (HttpStatus status : values()) {
            if (status.code == code)
                return status;
        }
        throw new IllegalArgumentException("No status with code " + code);
    }
}
