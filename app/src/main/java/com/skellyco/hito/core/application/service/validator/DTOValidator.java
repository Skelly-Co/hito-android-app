package com.skellyco.hito.core.application.service.validator;

import com.skellyco.hito.core.entity.dto.CreateAccountDTO;
import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.shared.error.CreateAccountError;
import com.skellyco.hito.core.shared.error.LoginError;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

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

    public static ValidationResult<CreateAccountError> validateCreateAccountDTO(CreateAccountDTO createAccountDTO)
    {
        if(createAccountDTO.getEmail() == null || createAccountDTO.getEmail().isEmpty())
        {
            CreateAccountError error = new CreateAccountError(CreateAccountError.Type.EMPTY_EMAIL);
            return new ValidationResult<>(false, error);
        }
        if(!validateEmail(createAccountDTO.getEmail()))
        {
            CreateAccountError error = new CreateAccountError(CreateAccountError.Type.INVALID_EMAIL);
            return new ValidationResult<>(false, error);
        }
        if(createAccountDTO.getUsername() == null || createAccountDTO.getUsername().isEmpty())
        {
            CreateAccountError error = new CreateAccountError(CreateAccountError.Type.EMPTY_USERNAME);
            return new ValidationResult<>(false, error);
        }
        if(!validateUsername(createAccountDTO.getUsername()))
        {
            CreateAccountError error = new CreateAccountError(CreateAccountError.Type.INVALID_USERNAME);
            return new ValidationResult<>(false, error);
        }
        if(createAccountDTO.getPassword() == null || createAccountDTO.getPassword().isEmpty())
        {
            CreateAccountError error = new CreateAccountError(CreateAccountError.Type.EMPTY_PASSWORD);
            return new ValidationResult<>(false, error);
        }
        if(!validatePassword(createAccountDTO.getPassword()))
        {
            CreateAccountError error = new CreateAccountError(CreateAccountError.Type.WEAK_PASSWORD);
            return new ValidationResult<>(false, error);
        }
        return new ValidationResult<>(true, null);
    }

    private static boolean validateEmail(String email)
    {
        return EmailValidator.getInstance().isValid(email);
    }

    private static boolean validatePassword(String password)
    {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=\\S+$).{8,}$");
        if(pattern.matcher(password).matches())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private static boolean validateUsername(String username)
    {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{5,}$");
        if(pattern.matcher(username).matches())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
