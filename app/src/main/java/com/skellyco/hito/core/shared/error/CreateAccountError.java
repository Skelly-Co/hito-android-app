package com.skellyco.hito.core.shared.error;

public class CreateAccountError implements IError{

    public enum Type {
        EMPTY_EMAIL, INVALID_EMAIL, EMPTY_USERNAME, EMPTY_PASSWORD, WEAK_PASSWORD,
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
