package com.example.cn.model;

import javax.annotation.Nullable;

public class Swipe {
    private int id_1;
    private int id_2;
    private Boolean swipe_1 = null;
    private Boolean swipe_2 = null;

    @Override
    public String toString() {
        return "Swipe{" +
                "id_1=" + id_1 +
                ", id_2=" + id_2 +
                ", swipe_1=" + swipe_1 +
                ", swipe_2=" + swipe_2 +
                '}';
    }

    public int getId_1() {
        return id_1;
    }

    public void setId_1(int id_1) {
        this.id_1 = id_1;
    }

    public int getId_2() {
        return id_2;
    }

    public void setId_2(int id_2) {
        this.id_2 = id_2;
    }

    public Boolean isSwipe_1() {
        return swipe_1;
    }

    public void setSwipe_1(boolean swipe_1) {
        this.swipe_1 = swipe_1;
    }

    public Boolean isSwipe_2() {
        return swipe_2;
    }

    public void setSwipe_2(boolean swipe_2) {
        this.swipe_2 = swipe_2;
    }
}