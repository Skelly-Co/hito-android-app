package com.skellyco.hito.core.application.service.validator;

import com.skellyco.hito.core.shared.error.IError;

public class ValidationResult<T extends IError> {

    private boolean valid;
    private T error;

    public ValidationResult(boolean valid, T error)
    {
        this.valid = valid;
        this.error = error;
    }

    public boolean isValid()
    {
        return valid;
    }

    public T getError()
    {
        return error;
    }
}
