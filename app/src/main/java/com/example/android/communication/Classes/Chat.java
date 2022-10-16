package com.example.android.communication.Classes;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String ID1;
    private String ID2;
    private Boolean seen;

    public Chat(String sender, String receiver, String message, String ID1, String ID2, Boolean seen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.ID1 = ID1;
        this.ID2 = ID2;
        this.seen = seen;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getID1() {
        return ID1;
    }

    public void setID1(String ID1) {
        this.ID1 = ID1;
    }

    public String getID2() {
        return ID2;
    }

    public void setID2(String ID2) {
        this.ID2 = ID2;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

}

