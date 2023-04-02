package org.orm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class Message {
    private Long id;
    private String content;
    private Date date;
    private Boolean seen;

    private Userr sender;
    private Userr receiver;

    public Message() {

    }

    public Message(Long id, String content, Date date, Boolean seen, Userr sender, Userr receiver) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.seen = seen;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Userr getSender() {
        return sender;
    }

    public void setSender(Userr sender) {
        this.sender = sender;
    }

    public Userr getReceiver() {
        return receiver;
    }

    public void setReceiver(Userr receiver) {
        this.receiver = receiver;
    }
}
