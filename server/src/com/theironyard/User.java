package com.theironyard;

import java.util.ArrayList;

/**
 * Created by DrScott on 11/5/15.
 */
public class User {
   int id;
    String firstName;
    String lastName;
    String email;
    String password;

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User(int id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    public User(){}

}
