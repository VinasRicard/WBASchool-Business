package com.example.android.communication.Classes;

public class SavedChat {

    String sender;
    String senderID;

    public SavedChat() {
    }

    public SavedChat(String sender, String senderID) {
        this.sender = sender;
        this.senderID = senderID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

}

