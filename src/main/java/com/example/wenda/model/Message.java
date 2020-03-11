package com.example.wenda.model;

import java.util.Date;

public class Message {

    private int id;

    private int formId;

    private int toId;

    private String content;

    private Date createdDate;

    private int hasRead;

    private String conversationId;

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public String getConversationId() {
        if(formId < toId){
            return String.format("%d_%d",formId,toId);
        }else {
            return String.format("%d_%d",toId,formId);
        }
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
