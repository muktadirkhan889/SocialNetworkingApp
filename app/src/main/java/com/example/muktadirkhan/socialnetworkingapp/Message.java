package com.example.muktadirkhan.socialnetworkingapp;

public class Message {
    String sender;
    String name;
    String time;
    String message;
    String receiver;

    Message() {

    }
    Message(String message, String sender, String name, String time, String receiver) {
        this.message = message;
        this.sender = sender;
        this.name = name;
        this.time = time;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
