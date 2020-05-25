package com.skellyco.hito.core.entity;

import java.util.List;
import java.util.Objects;

public class PrivateConversation {

    private User firstInterlocutor;
    private User secondInterlocutor;
    private List<Message> messages;

    public PrivateConversation()
    {

    }

    public PrivateConversation(User firstInterlocutor, User secondInterlocutor, List<Message> messages)
    {
        this.firstInterlocutor = firstInterlocutor;
        this.secondInterlocutor = secondInterlocutor;
        this.messages = messages;
    }

    public User getFirstInterlocutor()
    {
        return firstInterlocutor;
    }

    public void setFirstInterlocutor(User firstInterlocutor)
    {
        this.firstInterlocutor = firstInterlocutor;
    }

    public User getSecondInterlocutor()
    {
        return secondInterlocutor;
    }

    public void setSecondInterlocutor(User secondInterlocutor)
    {
        this.secondInterlocutor = secondInterlocutor;
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
