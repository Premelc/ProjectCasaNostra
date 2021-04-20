package com.example.cn.model;

public class Korisnik {
    private int id_korisnik;
    private String username;
    private String email;
    private String password;
    private String ime;
    private int godina_rodenja;
    private String opis;
    private char spol;
    private int id_fakultet;
    private boolean pusac;
    private boolean ljubimac;
    private char cimer_spol;
    private int cimer_godine_od;
    private int cimer_godine_do;
    private boolean cimer_pusac;
    private boolean cimer_ljubimac;
    private boolean miran_zivot;

    public int getId_korisnik() {
        return id_korisnik;
    }
    public void setId_korisnik(int id) {
        this.id_korisnik = id;
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

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getGodina_rodenja() {
        return godina_rodenja;
    }

    public void setGodina_rodenja(int godina_rodenja) {
        this.godina_rodenja = godina_rodenja;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public char getSpol() {
        return spol;
    }

    public void setSpol(char spol) {
        this.spol = spol;
    }

    public int getId_fakultet() {
        return id_fakultet;
    }

    public void setId_fakultet(int id_fakultet) {
        this.id_fakultet = id_fakultet;
    }

    public boolean isPusac() {
        return pusac;
    }

    public void setPusac(boolean pusac) {
        this.pusac = pusac;
    }

    public boolean isLjubimac() {
        return ljubimac;
    }

    public void setLjubimac(boolean ljubimac) {
        this.ljubimac = ljubimac;
    }

    public char getCimer_spol() {
        return cimer_spol;
    }

    public void setCimer_spol(char cimer_spol) {
        this.cimer_spol = cimer_spol;
    }

    public int getCimer_godine_od() {
        return cimer_godine_od;
    }

    public void setCimer_godine_od(int cimer_godine_od) {
        this.cimer_godine_od = cimer_godine_od;
    }

    public int getCimer_godine_do() {
        return cimer_godine_do;
    }

    public void setCimer_godine_do(int cimer_godine_do) {
        this.cimer_godine_do = cimer_godine_do;
    }

    public boolean isCimer_pusac() {
        return cimer_pusac;
    }

    public void setCimer_pusac(boolean cimer_pusac) {
        this.cimer_pusac = cimer_pusac;
    }

    public boolean isCimer_ljubimac() {
        return cimer_ljubimac;
    }

    public void setCimer_ljubimac(boolean cimer_ljubimac) {
        this.cimer_ljubimac = cimer_ljubimac;
    }

    public boolean isMiran_zivot() {
        return miran_zivot;
    }

    public void setMiran_zivot(boolean miran_zivot) {
        this.miran_zivot = miran_zivot;
    }
}