package com.skellyco.hito.core.error;

public class LoginError implements IError {

    public enum Type {
        WRONG_PASSWORD, USER_NOT_FOUND, TOO_MANY_ATTEMPTS, NETWORK_ERROR, UNKNOWN
    }

    private Type type;

    public LoginError()
    {
        type = Type.UNKNOWN;
    }

    public LoginError(Type type)
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
