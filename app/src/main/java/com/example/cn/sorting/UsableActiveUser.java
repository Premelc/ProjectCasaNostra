package com.example.cn.sorting;
import java.io.Serializable;

public class UsableActiveUser extends UsableUser implements Serializable {
    private String username;
    private String email;
    private String password;

    public UsableActiveUser(int id_korisnik,String username,String email,String password, String ime, int godina_rodenja, String opis, char spol, int id_fakultet, boolean pusac, boolean ljubimac, boolean miran_zivot , char cimer_spol , int cimer_godine_od , int cimer_godine_do,boolean cimer_pusac,boolean cimer_ljubimac) {
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
    public void setCijenaMin(int cijena) {

    }@Override
    public void setCijenaMax(int cijena_do) {

    }

    @Override
    public int getCijenaMin() {
        return 0;
    }

    @Override
    public void setZasebnaSoba(boolean b) {

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

    }

    @Override
    public int getId_kvart() {
        return 0;
    }

    @Override
    public void setId_lokacija(int b) {

    }

    @Override
    public int getId_lokacija() {
        return 0;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
