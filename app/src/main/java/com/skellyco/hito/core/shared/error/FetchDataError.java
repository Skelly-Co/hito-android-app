package com.skellyco.hito.core.shared.error;

public class FetchDataError implements IError {

    public enum Type {
        UNKNOWN
    }

    private Type type;

    public FetchDataError()
    {
        type = Type.UNKNOWN;
    }

    public FetchDataError(Type type)
    {
        this.type = type;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
}
