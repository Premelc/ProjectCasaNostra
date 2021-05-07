package com.example.cn.model;

import java.io.Serializable;

public class TrazimStan implements Serializable {
    private int id_potraga;
    private int id_korisnik;
    private double cijena_do;
    private boolean zasebna_soba;

    @Override
    public String toString() {
        return "TrazimStan{" +
                "id_potraga=" + id_potraga +
                ", id_korisnik=" + id_korisnik +
                ", cijena_do=" + cijena_do +
                ", zasebna_soba=" + zasebna_soba +
                '}';
    }

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