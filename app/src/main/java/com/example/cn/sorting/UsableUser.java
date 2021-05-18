package com.example.cn.sorting;

import com.example.cn.model.Korisnik;

public abstract class UsableUser extends Korisnik implements Comparable{

    public abstract void setCijenaMax(int cijena_do);
    public abstract void setZasebnaSoba(boolean b);
    public abstract void setModifier(int b);
    public abstract int getModifier();
    public abstract void setGrade(int b);
    public abstract int getGrade();
    public abstract void setId_kvart(int b);
    public abstract int getId_kvart();
    public abstract void setId_lokacija(int b);
    public abstract int getId_lokacija();

}
