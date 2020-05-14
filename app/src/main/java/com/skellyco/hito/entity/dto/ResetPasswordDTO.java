package com.skellyco.hito.entity.dto;

public class ResetPasswordDTO {

    private String email;

    public ResetPasswordDTO(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
}
