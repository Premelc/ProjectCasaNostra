package com.example.cn.sorting;
import java.io.Serializable;

public class UsableActiveUser extends UsableUser {
    private String username;
    private String email;
    private String password;
    private boolean zasebna_soba;
    private int cijena_max;
    private int id_kvart;
    private int id_lokacija;
    private int hasApt;

    public UsableActiveUser() {
    }

    public UsableActiveUser(int id_korisnik, String username, String email, String password, String ime, int godina_rodenja, String opis, char spol, int id_fakultet, boolean pusac, boolean ljubimac, boolean miran_zivot , char cimer_spol , int cimer_godine_od , int cimer_godine_do, boolean cimer_pusac, boolean cimer_ljubimac) {
        this.setId_korisnik(id_korisnik);
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
        this.setIme(ime);
        this.setGodina_rodenja(godina_rodenja);
        this.setOpis(opis);
        this.setSpol(spol);
        this.setId_fakultet(id_fakultet);
        this.setPusac(pusac);
        this.setLjubimac(ljubimac);
        this.setMiran_zivot(miran_zivot);
        this.setCimer_spol(cimer_spol);
        this.setCimer_godine_od(cimer_godine_od);
        this.setCimer_godine_do(cimer_godine_do);
        this.setCimer_ljubimac(cimer_ljubimac);
        this.setCimer_pusac(cimer_pusac);
    }

    public boolean isZasebna_soba() {
        return zasebna_soba;
    }

    public void setZasebna_soba(boolean zasebna_soba) {
        this.zasebna_soba = zasebna_soba;
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
    public void setCijenaMax(int cijena_do) {
        this.cijena_max = cijena_do;
    }

    public int getCijena_max(){
        return this.cijena_max;
    }

    @Override
    public void setZasebnaSoba(boolean b) {
        this.zasebna_soba = b;
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
    this.id_kvart = b;
    }

    @Override
    public int getId_kvart() {
        return this.id_kvart;
    }

    @Override
    public void setId_lokacija(int b) {
    this.id_lokacija = b;
    }
    @Override
    public int getId_lokacija() {
        return this.id_lokacija;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
