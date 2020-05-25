package com.skellyco.hito.core.entity;

import java.util.Date;
import java.util.Objects;

public class Message {

    private User user;
    private Date postTime;
    private String text;

    public Message()
    {

    }

    public Message(User user, Date postTime, String text)
    {
        this.user = user;
        this.postTime = postTime;
        this.text = text;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Date getPostTime()
    {
        return postTime;
    }

    public void setPostTime(Date postTime)
    {
        this.postTime = postTime;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Message message = (Message) o;
        return Objects.equals(user, message.user) &&
                Objects.equals(postTime, message.postTime) &&
                Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(user);
        hash = 59 * hash + Objects.hashCode(postTime);
        hash = 59 * hash + Objects.hashCode(text);
        return hash;
    }
}
