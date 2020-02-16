package com.sflpro.chat.model;

import java.util.HashMap;

public class Mail {
    private String from;
    private String to;
    private String subject;
    private HashMap<String, Object> model;

    public Mail(String from, String to, String subject, HashMap<String, Object> model) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.model = model;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public HashMap<String, Object> getModel() {
        return model;
    }

    public void setModel(HashMap<String, Object> model) {
        this.model = model;
    }
}
