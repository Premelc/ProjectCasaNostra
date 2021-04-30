package com.example.cn;

import com.example.cn.model.Korisnik;

public class otherUser extends Korisnik implements Comparable {

    private  int cijenaMax = 0;
    private int cijenaMin = 0;
    private boolean zasebnaSoba;

    private int grade = 0;

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
