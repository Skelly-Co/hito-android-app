package com.skellyco.hito.core.application.service.validator;

import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.error.LoginError;

import org.apache.commons.validator.routines.EmailValidator;

public class DTOValidator {

    public static ValidationResult<LoginError> validateLoginDTO(LoginDTO loginDTO)
    {
        if(loginDTO.getEmail() == null || loginDTO.getEmail().isEmpty())
        {
            LoginError error = new LoginError(LoginError.Type.EMPTY_EMAIL);
            return new ValidationResult<>(false, error);
        }
        if(!validateEmail(loginDTO.getEmail()))
        {
            LoginError error = new LoginError(LoginError.Type.INVALID_EMAIL);
            return new ValidationResult<>(false, error);
        }
        if(loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty())
        {
            LoginError error = new LoginError(LoginError.Type.EMPTY_PASSWORD);
            return new ValidationResult<>(false, error);
        }
        return new ValidationResult<>(true, null);
    }

    private static boolean validateEmail(String email)
    {
        return EmailValidator.getInstance().isValid(email);
    }

}
