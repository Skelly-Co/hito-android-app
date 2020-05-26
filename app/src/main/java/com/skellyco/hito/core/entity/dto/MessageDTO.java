package com.skellyco.hito.core.entity.dto;

import java.util.Date;

public class MessageDTO {

    private String interlocutorId;
    private Date postTime;
    private String text;

    public MessageDTO(String interlocutorId, Date postTime, String text)
    {
        this.interlocutorId = interlocutorId;
        this.postTime = postTime;
        this.text = text;
    }

    public String getInterlocutorId()
    {
        return interlocutorId;
    }

    public Date getPostTime()
    {
        return postTime;
    }

    public String getText()
    {
        return text;
    }
}
