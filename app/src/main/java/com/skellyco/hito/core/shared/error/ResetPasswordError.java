package com.skellyco.hito.core.shared.error;

public class ResetPasswordError implements IError {

    public enum Type {
        EMPTY_EMAIL, INVALID_EMAIL,
        EMAIL_NOT_FOUND, NETWORK_ERROR, UNKNOWN
    }

    private Type type;

    public ResetPasswordError()
    {
        type = Type.UNKNOWN;
    }

    public ResetPasswordError(Type type)
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
