package com.realisatie.realisatiesiem.classes;

public class User {

    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private int id;



    public User(String username, String password, String first_name, String last_name, int id) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getId() {
        return id;
    }
}
