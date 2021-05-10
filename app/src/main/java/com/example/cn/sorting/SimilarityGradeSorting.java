package com.example.cn.sorting;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.cn.activeUser;
import com.example.cn.model.Korisnik;
import com.example.cn.model.Kvart;
import com.example.cn.model.NudimStan;
import com.example.cn.model.PotragaLokacija;
import com.example.cn.model.TrazimStan;
import com.example.cn.otherUser;
import com.example.cn.sql.DatabaseHelper;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

public class SimilarityGradeSorting {

    //klasa u kojoj odredjujemo ocjenu slicnosti aktivnog korisnika sa svim ostalim korisnicima u bazi
    //Instancira se jednom prilikom ulaska u aplikaciju te vraca popis svih korisnika sortiran po slicnosti

    /*public List<Korisnik> Grade(activeUser actUsr , DatabaseHelper dbh){

        List<Korisnik> othrUsr = pullData(dbh , actUsr);


        int year = Calendar.getInstance().get(Calendar.YEAR);
        boolean[] explicitReq = {actUsr.isCimer_pusac(),actUsr.isCimer_ljubimac(),actUsr.isMiran_zivot(),actUsr.isTrazimStan()};
        for (Korisnik usr : othrUsr) {
            //Provjera eksplicitnih uvjeta
            if (Boolean.compare(explicitReq[0] , usr.isPusac()) < 0){
                usr.setModifier(usr.getModifier() * 2);
            }
            if(Boolean.compare(explicitReq[1] , usr.isLjubimac()) < 0 ){
                usr.setModifier(usr.getModifier()*2);
            }
            if (Boolean.compare(explicitReq[2] , usr.isMiran_zivot()) < 0 ){
                usr.setModifier(usr.getModifier()*2);
            }
            if (actUsr.getCimer_spol() != usr.getSpol()){
                othrUsr.remove(usr);
            }
            //provjera sličnosti
            if(actUsr.getId_fakultet() == usr.getId_fakultet()) {
                usr.setGrade(usr.getGrade() + 50);
            }
            if (actUsr.getId_kvart() == usr.getId_kvart()){
                usr.setGrade(usr.getGrade() + 50);
            }
            if (actUsr.getId_lokacija() == usr.getId_lokacija()){
                usr.setGrade(usr.getGrade() + 30);
            }
            if (actUsr.getCimer_godine_od() >= (year - usr.getGodina_rodenja()) && actUsr.getCimer_godine_do() <= (year - usr.getGodina_rodenja())){
                usr.setGrade(usr.getGrade() + 50);
            }
            if (actUsr.getCijenaMin() >= usr.getCijenaMin()){
                usr.setGrade(usr.getGrade() + 40);
            }

            //Naposljetku, djelimo ocjenu s modifierom
            usr.setGrade(usr.getGrade() / usr.getModifier());
        }
        othrUsr = sort(othrUsr);
        return othrUsr;
    }

    public List<Korisnik> pullData(DatabaseHelper dbh , activeUser au){

        String whereClause = "id_korisnik != ?"; // uvjet koji ide odmah iza WHERE
        String[] whereArgs = new String[1]; // whereArgs zamijenjuju upitnike (u ovom slucaju samo jedan pa polje Stringova ima samo jedan element)
        whereArgs[0] = Integer.toString(au.getId_korisnik()); // activeId je id aktivnog korisnika, idActive mora biti tipa String

        List<Korisnik> haveApt = (dbh.queryKorisnik(whereClause, whereArgs, null, null, null));
        List<Korisnik> needApt = (dbh.queryKorisnik(whereClause, whereArgs, null, null, null));

        List<NudimStan>haveAptPrice = dbh.queryNudimStan(null, null, null, null, null);
        List<TrazimStan>needAptPrice = dbh.queryTrazimStan(null, null, null, null, null);

        //ljudi koji imaju stan
        whereClause = "id_kvart = ?";
        for (Korisnik usr:haveApt) {
            NudimStan stan = findUserWithApt(usr.getId_korisnik() , haveAptPrice);
            usr.setId_kvart(stan.getId_kvart());
            whereArgs[0] = Integer.toString(stan.getId_kvart());
            Kvart kvartLokacija = dbh.singleQueryKvart(whereClause,whereArgs,null,null,null);
            //zapisuje cjenu stana na cijenaMin za korisnika
            usr.setCijenaMin((int)stan.getCijena());
            if(stan.isZasebna_soba()){
                usr.setZasebnaSoba(true);
            }else{
                usr.setZasebnaSoba(false);
            }
            usr.setId_lokacija(kvartLokacija.getId_lokacija());
        }

        //ljudi koji nemaju stan
        whereClause = "id_potraga = ?";
        for (Korisnik usr:needApt) {
            TrazimStan stan = findUserNeedApt(usr.getId_korisnik() , needAptPrice);
            whereArgs[0] = Integer.toString(stan.getId_potraga());
            PotragaLokacija lokacija = dbh.singleQueryPotragaLokacija(whereClause,whereArgs,null,null,null);
            usr.setCijenaMin((int) stan.getCijena_od());
            usr.setCijenaMax((int) stan.getCijena_do());
            if(stan.isZasebna_soba()){
                usr.setZasebnaSoba(true);
            }else{
                usr.setZasebnaSoba(false);
            }
            usr.setId_lokacija(lokacija.getId_lokacija());
        }

        if(findUser(au.getId_korisnik(),needApt).getId_korisnik() == au.getId_korisnik()){
            haveApt.addAll(needApt);
            return haveApt;
        }else{
            return needApt;
        }
    }

    public Korisnik findUser(int id , List<Korisnik>list){
        for (Korisnik item:list) {
            if (item.getId_korisnik() == id)return item;
        }
        return null;
    }

    public NudimStan findUserWithApt(int id , List<NudimStan> apts){
        for(NudimStan usr : apts){
            if (id == usr.getId_korisnik()) return usr;
        }
        return null;
    }
    public TrazimStan findUserNeedApt(int id  , List<TrazimStan> apts){
        for(TrazimStan usr : apts){
            if (id == usr.getId_korisnik()) return usr;
        }
        return null;
    }

    public List<Korisnik> sort(List<Korisnik>list){
        Collections.sort(list);
        return list;
    }*/
}
