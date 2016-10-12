package org.switchyard.quickstarts.camel.service;

public class Message {
    private String subject;
    private String body;

    public String getSubject() {
        return subject;
    }
    public Message setSubject(String subject) {
        this.subject = subject;
        return this;
    }
    public String getBody() {
        return body;
    }
    public Message setBody(String body) {
        this.body = body;
        return this;
    }
    public String toString() {
        return "Message:[Subject='" + subject + "', Body='" + body + "']";
    }
}
