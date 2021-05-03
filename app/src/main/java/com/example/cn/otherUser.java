package com.example.cn;

import com.example.cn.model.Korisnik;

public class otherUser extends Korisnik implements Comparable {

    private  int cijenaMax = 0;
    private int cijenaMin = 0;
    private boolean zasebnaSoba;
    private int modifier = 1;
    private int grade = 0;
    private int id_kvart;
    private int id_lokacija;

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

    public int getCijenaMin() {
        return cijenaMin;
    }

    public void setCijenaMin(int cijenaMin) {
        this.cijenaMin = cijenaMin;
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

    public otherUser(){
        super();
    }

    @Override
    public int compareTo(Object comparesTo) {
        int comparage=((otherUser)comparesTo).getGrade();
        /* For Descending order*/
        return comparage - this.getGrade();

        /* For Ascending order do like this */
        //return comparage-this.getGrade();
    }
}
