package com.skellyco.hito.core.model.entity;

import java.util.List;
import java.util.Objects;

public class PrivateConversation {

    private String id;
    private User firstInterlocutor;
    private User secondInterlocutor;
    private List<Message> messages;

    public PrivateConversation()
    {

    }

    public PrivateConversation(String id, User firstInterlocutor, User secondInterlocutor, List<Message> messages)
    {
        this.id = id;
        this.firstInterlocutor = firstInterlocutor;
        this.secondInterlocutor = secondInterlocutor;
        this.messages = messages;
    }

    public String getId()
    {
        return id;
    }

    public List<Message> getMessages()
    {
        return messages;
    }

    public void setMessages(List<Message> messages)
    {
        this.messages = messages;
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
        PrivateConversation that = (PrivateConversation) o;
        return Objects.equals(firstInterlocutor, that.firstInterlocutor) &&
                Objects.equals(secondInterlocutor, that.secondInterlocutor) &&
                Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.firstInterlocutor);
        hash = 59 * hash + Objects.hashCode(this.secondInterlocutor);
        hash = 59 * hash + Objects.hashCode(this.messages);
        return hash;
    }
}
