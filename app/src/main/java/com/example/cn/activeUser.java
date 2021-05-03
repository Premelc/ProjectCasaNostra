package com.example.cn;

import com.example.cn.model.Korisnik;

import java.io.Serializable;

public class activeUser extends Korisnik implements Serializable {
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

    @Override
    public void setCijenaMin(int cijena) {

    }

    @Override
    public void setCijenaMax(int cijena_do) {

    }

    @Override
    public int getCijenaMin() {
        return 0;
    }

    @Override
    public void setZasebnaSoba(boolean b) {

    }

    @Override
    public void setModifier(int b) {

    }

    @Override
    public int getModifier() {
        return 0;
    }

    @Override
    public void setGrade(int b) {

    }

    @Override
    public int getGrade() {
        return 0;
    }

    @Override
    public void setId_kvart(int b) {

    }

    @Override
    public int getId_kvart() {
        return 0;
    }

    @Override
    public void setId_lokacija(int b) {

    }

    @Override
    public int getId_lokacija() {
        return 0;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}