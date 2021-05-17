package com.example.cn.sorting;


public class UsableOtherUser extends UsableUser implements Comparable {
    private  int cijenaMax = 0;
    private boolean zasebnaSoba;
    private int modifier = 1;
    private int grade = 0;
    private int id_kvart;
    private String kvart_ime;
    private String lokacija_ime;
    private int id_lokacija;
    private boolean apt;
    private int[] ljubimci;

    public UsableOtherUser(int id_korisnik, String ime, int godina_rodenja, String opis, char spol, int id_fakultet, boolean pusac, boolean ljubimac, boolean miran_zivot) {
            this.setId_korisnik(id_korisnik);
            this.setIme(ime);
            this.setGodina_rodenja(godina_rodenja);
            this.setOpis(opis);
            this.setSpol(spol);
            this.setId_fakultet(id_fakultet);
            this.setPusac(pusac);
            this.setLjubimac(ljubimac);
            this.setMiran_zivot(miran_zivot);
        }
        public UsableOtherUser(String name){
        this.setIme(name);
        }

    public boolean isApt() {
        return apt;
    }

    public int[] getLjubimci() {
        return ljubimci;
    }

    public void setLjubimci(int[] ljubimci) {
        this.ljubimci = ljubimci;
    }

    public void setApt(boolean apt) {
        this.apt = apt;
    }

    public String getKvart_ime() {
        return kvart_ime;
    }

    public void setKvart_ime(String kvart_ime) {
        this.kvart_ime = kvart_ime;
    }

    public String getLokacija_ime() {
        return lokacija_ime;
    }

    public void setLokacija_ime(String lokacija_ime) {
        this.lokacija_ime = lokacija_ime;
    }

    public int getId_lokacija() {
        return id_lokacija;
    }

    public void setId_lokacija(int id_lokacija) {
        this.id_lokacija = id_lokacija;
    }

    public int getId_kvart() {
        return id_kvart;
    }

    public void setId_kvart(int id_kvart) {
        this.id_kvart = id_kvart;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public boolean isZasebnaSoba() {
        return zasebnaSoba;
    }

    public void setZasebnaSoba(boolean zasebnaSoba) {
        this.zasebnaSoba = zasebnaSoba;
    }

    public int getCijenaMax() {
        return cijenaMax;
    }

    public void setCijenaMax(int cijenaMax) {
        this.cijenaMax = cijenaMax;
    }

    public int getGrade() {
        return grade;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }

    public UsableOtherUser(){
        super();
    }

    @Override
    public int compareTo(Object usableOtherUser) {
        UsableOtherUser usr = (UsableOtherUser) usableOtherUser;
        int comparage = usr.getGrade();
        return  comparage - this.getGrade();
    }
}
