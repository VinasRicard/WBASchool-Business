package com.example.android.communication.Classes;

public final class FolderItem {

    String receiver;
    String receiverID;

    public FolderItem() {
    }

    public FolderItem(String receiver, String receiverID) {
        this.receiver = receiver;
        this.receiverID = receiverID;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }
}
