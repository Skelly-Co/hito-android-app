package com.skellyco.hito.core.domain;

public class Resource<T, S> {

    public enum Status {
        SUCCESS, ERROR
    }

    private Status status;
    private T data;
    private S error;

    public Resource(Status status, T data, S error)
    {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public Status getStatus()
    {
        return status;
    }

    public T getData()
    {
        return data;
    }

    public S getError()
    {
        return error;
    }
}
