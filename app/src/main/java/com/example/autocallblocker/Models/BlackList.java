package com.example.autocallblocker.Models;

public class BlackList {
    // Two mapping fields for the database table blacklist
    public long id;
    public String phoneNumber;

    // Default constructor
    public BlackList() {
    }

    // To easily create Blacklist object, an alternative constructor
    public BlackList(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Overriding the default method to compare between the two objects bu phone number
    @Override
    public boolean equals(final Object obj) {

        // If passed object is an instance of Blacklist, then compare the phone numbers, else return false as they are not equal
        if(obj.getClass().isInstance(new BlackList()))
        {
            // Cast the object to Blacklist
             BlackList bl = (BlackList) obj;

            // Compare whether the phone numbers are same, if yes, it defines the objects are equal
            if(bl.phoneNumber.equalsIgnoreCase(this.phoneNumber))
                return true;
        }
        return false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
