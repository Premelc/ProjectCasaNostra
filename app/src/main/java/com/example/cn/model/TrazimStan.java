package com.example.cn.model;

public class TrazimStan {
    private int id_potraga;
    private int id_korisnik;
    private double cijena_od;
    private double cijena_do;
    private boolean zasebna_soba;

    public int getId_potraga() {
        return id_potraga;
    }

    public void setId_potraga(int id_potraga) {
        this.id_potraga = id_potraga;
    }

    public int getId_korisnik() {
        return id_korisnik;
    }

    public void setId_korisnik(int id_korisnik) {
        this.id_korisnik = id_korisnik;
    }

    public double getCijena_od() {
        return cijena_od;
    }

    public void setCijena_od(double cijena_od) {
        this.cijena_od = cijena_od;
    }

    public double getCijena_do() {
        return cijena_do;
    }

    public void setCijena_do(double cijena_do) {
        this.cijena_do = cijena_do;
    }

    public boolean isZasebna_soba() {
        return zasebna_soba;
    }

    public void setZasebna_soba(boolean zasebna_soba) {
        this.zasebna_soba = zasebna_soba;
    }
}