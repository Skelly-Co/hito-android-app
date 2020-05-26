package com.skellyco.hito.core.entity.dto;

public class PrivateConversationDTO {

    private String firstInterlocutorId;
    private String secondInterlocutorId;

    public PrivateConversationDTO(String firstInterlocutorId, String secondInterlocutorId)
    {
        this.firstInterlocutorId = firstInterlocutorId;
        this.secondInterlocutorId = secondInterlocutorId;
    }

    public String getFirstInterlocutorId()
    {
        return firstInterlocutorId;
    }

    public String getSecondInterlocutorId()
    {
        return secondInterlocutorId;
    }
}
