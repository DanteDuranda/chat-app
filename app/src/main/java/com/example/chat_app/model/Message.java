package com.example.chat_app.model;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;

public class Message {
    String message;
    String sender;
    String recipient;
    String time;
    private @ServerTimestamp Date timestamp;

    public Message() {
    }

    public Message(String message, String sender, String recipient, String time) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.time = time;
    }

    public Message(String message, String sender, String recipient, Date timestamp) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}
