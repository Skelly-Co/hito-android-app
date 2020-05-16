package com.skellyco.hito.core.error;

public class CreateAccountError {

    public enum Type {
        UNKNOWN
    }

    private Type type;

    public CreateAccountError()
    {
        type = Type.UNKNOWN;
    }

    public CreateAccountError(Type type)
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
