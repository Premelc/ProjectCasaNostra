package com.example.cn.sorting;

import com.example.cn.model.Korisnik;
import com.example.cn.model.Kvart;
import com.example.cn.model.Lokacija;
import com.example.cn.model.NudimStan;
import com.example.cn.model.PotragaLokacija;
import com.example.cn.model.Swipe;
import com.example.cn.model.TrazimStan;
import com.example.cn.sql.DatabaseHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class SimilarityGradeSorting {

    //klasa u kojoj odredjujemo ocjenu slicnosti aktivnog korisnika sa svim ostalim korisnicima u bazi
    //Instancira se jednom prilikom ulaska u aplikaciju te vraca popis svih korisnika sortiran po slicnosti

    public List<UsableOtherUser> Grade(Korisnik activeUser , DatabaseHelper dbh) {

        List<UsableOtherUser> othrUsr = pullData(dbh, activeUser);
        UsableActiveUser actUsr = writeActiveUserData(activeUser, dbh);
        List<UsableOtherUser> toRemove = new ArrayList<>();

        if (!othrUsr.isEmpty()) {
            boolean[] explicitReq = {actUsr.isCimer_pusac(), actUsr.isCimer_ljubimac(), actUsr.isMiran_zivot(), actUsr.isTrazimStan()};

            for (UsableOtherUser usr : othrUsr) {
                //Provjera eksplicitnih uvjeta
                if (Boolean.compare(explicitReq[0], usr.isPusac()) < 0) {
                    usr.setModifier(usr.getModifier() * 2);
                }
                if (Boolean.compare(explicitReq[1], usr.isLjubimac()) < 0) {
                    usr.setModifier(usr.getModifier() * 2);
                }
                if (Boolean.compare(explicitReq[2], usr.isMiran_zivot()) < 0 ||Boolean.compare(explicitReq[2], usr.isMiran_zivot()) > 0 ) {
                    usr.setModifier(usr.getModifier() * 2);
                }

                // ima utjecaja samo ako usr NUDI stan sa sobom koja ce se dijeliti, a actUsr zahtjeva zasebnu sobu
                // ako OBA korisnika TRAZE stan, onda nema veze ako jedan "Moze dijeliti sobu sa cimerom", a drugi "Zeli zasebnu sobu"
                if(usr.isApt() && Boolean.compare(usr.isZasebnaSoba(),actUsr.isZasebna_soba()) < 0){
                    usr.setModifier(usr.getModifier() * 2);
                }

                // Cimer_spol je 'S' ako je korisniku svejedno
                if (actUsr.getCimer_spol() != 'S' && actUsr.getCimer_spol() != usr.getSpol()) {
                        //toRemove.add(usr);
                    usr.setModifier(usr.getModifier() * 50);
                    }

                //provjera sličnosti
                if (actUsr.getId_fakultet() == usr.getId_fakultet()) {
                    usr.setGrade(usr.getGrade() + 50);
                }
                if (actUsr.getId_kvart() == usr.getId_kvart()) {
                    usr.setGrade(usr.getGrade() + 50);
                }
                if (actUsr.getId_lokacija() == usr.getId_lokacija()) {
                    usr.setGrade(usr.getGrade() + 30);
                }
                if (actUsr.getCimer_godine_od() >= usr.getGodina_rodenja() && actUsr.getCimer_godine_do() <= usr.getGodina_rodenja()) {
                    usr.setGrade(usr.getGrade() + 50);
                }
                if (actUsr.getCijena_max() >= usr.getCijenaMax()) {
                    usr.setGrade(usr.getGrade() + 40);
                }

                //Naposljetku, djelimo ocjenu s modifierom
                usr.setGrade(usr.getGrade() / usr.getModifier());
            }
            //othrUsr.removeAll(toRemove);
            Collections.sort(othrUsr);
            return othrUsr;
        }
        else{
            othrUsr.add(new UsableOtherUser("EMPTY LIST PLACEHOLDER"));
        }
        return othrUsr;
    }

    public List<UsableOtherUser> pullData(DatabaseHelper dbh , Korisnik au){

        String whereClause = "id_korisnik != ?"; // uvjet koji ide odmah iza WHERE
        String[] whereArgs = new String[1]; // whereArgs zamijenjuju upitnike (u ovom slucaju samo jedan pa polje Stringova ima samo jedan element)
        whereArgs[0] = Integer.toString(au.getId_korisnik()); // activeId je id aktivnog korisnika, idActive mora biti tipa String



        List<Korisnik> modelHaveApt = (dbh.queryKorisnik(whereClause, whereArgs, null, null, null));
        List<Korisnik> modelNeedApt = (dbh.queryKorisnik(whereClause, whereArgs, null, null, null));
        List<UsableOtherUser> haveApt = writeData(modelHaveApt);
        List<UsableOtherUser> needApt = writeData(modelNeedApt);

        whereClause = "id_1 != ?";
        List<Swipe> swipeState = (dbh.querySwipe(whereClause, whereArgs, null, null, null));



        List<NudimStan>haveAptPrice = dbh.queryNudimStan(null, null, null, null, null);
        List<TrazimStan>needAptPrice = dbh.queryTrazimStan(null, null, null, null, null);

        //ljudi koji imaju stan
        whereClause = "id_kvart = ?";
        for (UsableOtherUser usr:haveApt) {
            NudimStan stan = findUserWithApt(usr.getId_korisnik() , haveAptPrice);
            usr.setId_kvart(stan.getId_kvart());

            whereArgs[0] = Integer.toString(stan.getId_kvart());
            Kvart kvartLokacija = dbh.singleQueryKvart(whereClause,whereArgs,null,null,null);

            usr.setCijenaMax((int)stan.getCijena());
            usr.setApt(true);
            usr.setZasebnaSoba(stan.isZasebna_soba());
            usr.setId_lokacija(kvartLokacija.getId_lokacija());
            usr.setKvart_ime(kvartLokacija.getNaziv());
            usr.setId_kvart(kvartLokacija.getId_kvart());

            if(getIfUserSwiped(usr.getId_korisnik(), au.getId_korisnik() , swipeState))haveApt.remove(usr);
        }

        //ljudi koji nemaju stan

        for (UsableOtherUser usr:needApt) {
            TrazimStan stan = findUserNeedApt(usr.getId_korisnik() , needAptPrice);

            whereClause = "id_potraga = ?";
            whereArgs[0] = Integer.toString(stan.getId_potraga());
            PotragaLokacija lokacija = dbh.singleQueryPotragaLokacija(whereClause,whereArgs,null,null,null);

            whereClause = "id_lokacija = ?";
            whereArgs[0] = Integer.toString(lokacija.getId_lokacija());
            Lokacija lok = dbh.singleQueryLokacija(whereClause,whereArgs,null,null,null);

            usr.setCijenaMax((int) stan.getCijena_do());
            usr.setApt(false);
            usr.setLokacija_ime(lok.getNaziv());
            usr.setZasebnaSoba(stan.isZasebna_soba());
            usr.setId_lokacija(lokacija.getId_lokacija());

            if(getIfUserSwiped(usr.getId_korisnik(), au.getId_korisnik() , swipeState))needApt.remove(usr);
        }

        if(findUser(au.getId_korisnik(),needApt).getId_korisnik() == au.getId_korisnik()){
            haveApt.addAll(needApt);
            return haveApt;
        }else{
            return needApt;
        }
    }

    public Korisnik findUser(int id , List<UsableOtherUser>list){
        for (Korisnik item:list) {
            if (item.getId_korisnik() == id)return item;
        }
        return new Korisnik();
    }

    public NudimStan findUserWithApt(int id , List<NudimStan> apts){
        for(NudimStan usr : apts){
            if (id == usr.getId_korisnik()) return usr;
        }
        return new NudimStan();
    }
    public TrazimStan findUserNeedApt(int id  , List<TrazimStan> apts){
        for(TrazimStan usr : apts){
            if (id == usr.getId_korisnik()) return usr;
        }
        return new TrazimStan();
    }

    public List<UsableOtherUser> sort(List<UsableOtherUser>list){
        Collections.sort(list);
        return list;
    }

    public List<UsableOtherUser> writeData(List<Korisnik> users){
        List<UsableOtherUser> usableOtherUsers = new ArrayList<>();
            for (Korisnik usr : users){
                usableOtherUsers.add(new UsableOtherUser(usr.getId_korisnik(), usr.getIme(),usr.getGodina_rodenja(),usr.getOpis(),usr.getSpol(),usr.getId_fakultet(),usr.isPusac(),usr.isLjubimac(),usr.isMiran_zivot()));
            }

                return usableOtherUsers;
    }

    public UsableActiveUser writeActiveUserData(Korisnik usr, DatabaseHelper dbh){
        UsableActiveUser activeUser = new UsableActiveUser(usr.getId_korisnik(),usr.getUsername(),usr.getEmail(),usr.getPassword(), usr.getIme(),usr.getGodina_rodenja(),usr.getOpis(),usr.getSpol(),usr.getId_fakultet(),usr.isPusac(),usr.isLjubimac(),usr.isMiran_zivot(),usr.getCimer_spol(),usr.getCimer_godine_od(),usr.getCimer_godine_do(),usr.isCimer_pusac(),usr.isCimer_ljubimac());

        List<Korisnik> haveApt = (dbh.queryKorisnik(null, null, null, null, null));
        for (Korisnik user : haveApt){
            if(user.getId_korisnik() == usr.getId_korisnik()){
                List<NudimStan>haveAptPrice = dbh.queryNudimStan(null, null, null, null, null);
                NudimStan stan = findUserWithApt(usr.getId_korisnik() , haveAptPrice);
                activeUser.setCijenaMax((int)stan.getCijena());
                activeUser.setZasebna_soba(stan.isZasebna_soba());
                return activeUser;
            }
        }
        List<Korisnik> needApt = (dbh.queryKorisnik(null, null, null, null, null));
        for(Korisnik user : needApt){
            if(user.getId_korisnik() == usr.getId_korisnik()){
                List<TrazimStan>needAptPrice = dbh.queryTrazimStan(null, null, null, null, null);
                TrazimStan stan = findUserNeedApt(usr.getId_korisnik() , needAptPrice);
                String whereClause = "id_potraga = ?";
                String[] whereArgs = new String[1];
                whereArgs[0] = Integer.toString(stan.getId_potraga());
                PotragaLokacija potragaLokacija = dbh.singleQueryPotragaLokacija(whereClause,whereArgs,null,null,null);

                activeUser.setId_lokacija(potragaLokacija.getId_lokacija());
                activeUser.setCijenaMax((int)stan.getCijena_do());
                activeUser.setZasebna_soba(stan.isZasebna_soba());
                return activeUser;
            }
        }
        return new UsableActiveUser();
        }

    public boolean getIfUserSwiped(int usr_id , int actUsr_id , List<Swipe> swipeState) {
        for (Swipe swp : swipeState){
            if (swp.getId_1()== actUsr_id && usr_id == swp.getId_2())return true;
        }
        return false;
    }
}
