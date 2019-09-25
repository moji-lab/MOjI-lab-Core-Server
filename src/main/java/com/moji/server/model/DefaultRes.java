package com.moji.server.model;

/**
 * Created By ds on 2019-08-16.
 */

public class DefaultRes<T> {

    private int status;

    private String message;

    private T data;

    public DefaultRes(final int status, final String message, final T t) {
        this.status = status;
        this.message = message;
        this.data = t;
    }

    public DefaultRes(final int status, final String message) {
        this(status, message, null);
    }

    public static <T> DefaultRes<T> res(final int status, final String message) {
        return res(status, message, null);
    }

    public static <T> DefaultRes<T> res(final int status, final String message, final T t) {
        return new DefaultRes<T>(status, message, t);
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static final DefaultRes BAD_REQUEST = new DefaultRes(400, "BAD_REQUEST");
    public static final DefaultRes UNAUTHORIZED = new DefaultRes(401, "UNAUTHORIZED");
    public static final DefaultRes FORBIDDEN = new DefaultRes(403, "FORBIDDEN");
    public static final DefaultRes NOT_FOUNT = new DefaultRes(404, "NOT_FOUNT");
    public static final DefaultRes FAIL_DEFAULT_RES = new DefaultRes(500, "INTERNAL_SERVER_ERROR");
    public static final DefaultRes SERVICE_UNAVAILABLE = new DefaultRes(503, "SERVICE_UNAVAILABLE");

}