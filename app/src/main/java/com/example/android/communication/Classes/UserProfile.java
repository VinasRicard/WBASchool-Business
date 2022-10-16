package com.example.android.communication.Classes;

public class UserProfile {
    public String id;
    public String Name;
    public String email;
    public String accountType;
    public String center;

    public UserProfile() {

    }

    public UserProfile(String id, String Name, String email, String accountType, String center) {
        this.id = id;
        this.Name = Name;
        this.email = email;
        this.accountType = accountType;
        this.center = center;
    }

    public String getUserName() {
        return Name;
    }

    public void setUserName(String Name) {
        this.Name = Name;
    }

    public String getUserEmail() {
        return email;
    }

    public void setUserEmail(String email) {
        this.email = email;
    }

    public String getUserCenter() {
        return center;
    }

    public void setUserCenter(String center) {
        this.center = center;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
