package com.example.cn;

import com.example.cn.model.Korisnik;

public class activeUser extends Korisnik {
    private String username;
    private String email;
    private String password;



    public activeUser(){
        super();
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}