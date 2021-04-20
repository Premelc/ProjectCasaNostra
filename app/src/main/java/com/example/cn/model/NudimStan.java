package com.example.cn.model;

public class NudimStan {
    private int id_stan;
    private int id_korisnik;
    private double cijena;
    private int id_kvart;
    private boolean zasebna_soba;

    public int getId_stan() {
        return id_stan;
    }

    public void setId_stan(int id_stan) {
        this.id_stan = id_stan;
    }

    public int getId_korisnik() {
        return id_korisnik;
    }

    public void setId_korisnik(int id_korisnik) {
        this.id_korisnik = id_korisnik;
    }

    public double getCijena() {
        return cijena;
    }

    public void setCijena(double cijena) {
        this.cijena = cijena;
    }

    public int getId_kvart() {
        return id_kvart;
    }

    public void setId_kvart(int id_kvart) {
        this.id_kvart = id_kvart;
    }

    public boolean isZasebna_soba() {
        return zasebna_soba;
    }

    public void setZasebna_soba(boolean zasebna_soba) {
        this.zasebna_soba = zasebna_soba;
    }
}