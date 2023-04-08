package org.easyorm.tests.models_;

import java.util.Date;

public class Message {
    private Long id;
    private String content;
    private Date date;
    private Boolean seen;

    // relations with Personne table
    private Personne sender;
    private Personne receiver;

    public Message() {
    }

    // we can add other constructors
    public Message(Long id, String content, Date date, Boolean seen, Personne sender, Personne receiver) {
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

    public Personne getSender() {
        return sender;
    }

    public void setSender(Personne sender) {
        this.sender = sender;
    }

    public Personne getReceiver() {
        return receiver;
    }

    public void setReceiver(Personne receiver) {
        this.receiver = receiver;
    }
}