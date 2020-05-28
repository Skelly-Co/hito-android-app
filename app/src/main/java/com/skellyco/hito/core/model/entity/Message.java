package com.skellyco.hito.core.model.entity;

import java.util.Date;
import java.util.Objects;

public class Message {

    private String id;
    private User sender;
    private Date postTime;
    private String text;

    public Message()
    {

    }

    public Message(String id, User sender, Date postTime, String text)
    {
        this.id = id;
        this.sender = sender;
        this.postTime = postTime;
        this.text = text;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public User getSender()
    {
        return sender;
    }

    public void setSender(User sender)
    {
        this.sender = sender;
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
        return Objects.equals(id, message.id) &&
                Objects.equals(sender, message.sender) &&
                Objects.equals(postTime, message.postTime) &&
                Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(id);
        hash = 59 * hash + Objects.hashCode(sender);
        hash = 59 * hash + Objects.hashCode(postTime);
        hash = 59 * hash + Objects.hashCode(text);
        return hash;
    }
}
