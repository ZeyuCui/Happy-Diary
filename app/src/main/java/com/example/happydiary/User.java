package com.example.happydiary;

public class User {

    private String email;
    private String password;
    private String name;
    private String id;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "name: " + name + "\nemail: " + email;
    }

}
