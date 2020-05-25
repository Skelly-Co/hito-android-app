package com.skellyco.hito.core.entity;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        User user = (User) o;
        return Objects.equals(uid, user.uid) &&
                Objects.equals(email, user.email) &&
                Objects.equals(username, user.username);
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.uid);
        hash = 59 * hash + Objects.hashCode(this.email);
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }
}
