package com.skellyco.hito.core.entity;

public class User {

    private String uid;
    private String email;
    private String username;

    public User()
    {

    }

    public User(String uid, String email, String username)
    {
        this.uid = uid;
        this.email = email;
        this.username = username;
    }

    public String getUid()
    {
        return uid;
    }

    public String getEmail()
    {
        return email;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
