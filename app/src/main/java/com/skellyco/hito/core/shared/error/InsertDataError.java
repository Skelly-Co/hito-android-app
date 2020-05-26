package com.skellyco.hito.core.shared.error;

public class InsertDataError implements IError {

    public enum Type {
        UNKNOWN
    }

    private Type type;

    public InsertDataError()
    {
        this.type = Type.UNKNOWN;
    }

    public InsertDataError(Type type)
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
