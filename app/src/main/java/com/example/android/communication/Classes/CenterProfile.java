package com.example.android.communication.Classes;

public class CenterProfile {
    public String city;
    public String country;
    public String email;
    public String id;
    public String name;
    public String personContact;
    public Long phoneContact;

    public CenterProfile() {

    }

    public CenterProfile(String city, String country, String email, String id, String name, String personContact, Long phoneContact) {
        this.id = id;
        this.city = city;
        this.email = email;
        this.name = name;
        this.country = country;
        this.personContact = personContact;
        this.phoneContact = phoneContact;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonContact() {
        return personContact;
    }

    public void setPersonContact(String personContact) {
        this.personContact = personContact;
    }

    public Long getPhoneContact() {
        return phoneContact;
    }

    public void setPhoneContact(Long phoneContact) {
        this.phoneContact = phoneContact;
    }
}
