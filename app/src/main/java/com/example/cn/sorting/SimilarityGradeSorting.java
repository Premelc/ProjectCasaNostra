package com.example.cn.sorting;

import com.example.cn.model.Korisnik;
import com.example.cn.model.Kvart;
import com.example.cn.model.NudimStan;
import com.example.cn.model.PotragaLokacija;
import com.example.cn.model.Swipe;
import com.example.cn.model.TrazimStan;
import com.example.cn.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimilarityGradeSorting {

    //klasa u kojoj odredjujemo ocjenu slicnosti aktivnog korisnika sa svim ostalim korisnicima u bazi
    //Instancira se jednom prilikom ulaska u aplikaciju te vraca popis svih korisnika sortiran po slicnosti

    public List<UsableOtherUser> Grade(Korisnik activeUser , DatabaseHelper dbh){
        List<UsableOtherUser> othrUsr = pullData(dbh, activeUser);
        UsableActiveUser actUsr = writeActiveUserData(activeUser, dbh);
        return Execute(actUsr , othrUsr);
    }

    public List<UsableOtherUser> GradingTestInterface(UsableActiveUser actUsr, List<UsableOtherUser> othrUsr , List<Swipe> swipes){
        List<UsableOtherUser> toRemove = new ArrayList<>();
        for (UsableOtherUser usr : othrUsr){
            if (actUsr.getCimer_spol() != 'S' && actUsr.getCimer_spol() != usr.getSpol()) {
                toRemove.add(usr);
            }
            if (actUsr.isApt() && usr.isApt()){
                toRemove.add(usr);
            }
            if(getIfUserSwiped(swipes,actUsr.getId_korisnik(),usr.getId_korisnik())){
                toRemove.add(usr);
            }
        }
        othrUsr.removeAll(toRemove);
        return Execute(actUsr , othrUsr);
    }

    public List<UsableOtherUser> Execute(UsableActiveUser actUsr, List<UsableOtherUser> othrUsr) {

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

                //provjera sličnosti
                if (actUsr.getId_fakultet() == usr.getId_fakultet()) {
                    usr.setGrade(usr.getGrade() + 50);
                }
                if (actUsr.getId_kvart() == usr.getId_kvart()) {
                    usr.setGrade(usr.getGrade() + 50);
                }
                if (compareLocData(actUsr, usr)) {
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
            //noinspection unchecked
            Collections.sort(othrUsr);
            return othrUsr;
        }
        return othrUsr;
    }

    @SuppressWarnings("IfStatementWithIdenticalBranches")
    public List<UsableOtherUser> pullData(DatabaseHelper dbh , Korisnik au){

        String whereClause = "id_korisnik != ?"; // uvjet koji ide odmah iza WHERE
        String[] whereArgs = new String[1]; // whereArgs zamijenjuju upitnike (u ovom slucaju samo jedan pa polje Stringova ima samo jedan element)
        whereArgs[0] = Integer.toString(au.getId_korisnik()); // activeId je id aktivnog korisnika, idActive mora biti tipa String



        List<Korisnik> users = (dbh.queryKorisnik(whereClause, whereArgs, null, null, null));

        List<UsableOtherUser> allUsers = writeData(users);

        List<UsableOtherUser> haveApt = new ArrayList<>();
        List<UsableOtherUser> needApt = new ArrayList<>();

        UsableActiveUser actUsr = writeActiveUserData(au, dbh);

        List<NudimStan>haveAptPrice = dbh.queryNudimStan(null, null, null, null, null);
        List<TrazimStan>needAptPrice = dbh.queryTrazimStan(null, null, null, null, null);

        List<UsableOtherUser> toRemove1 = new ArrayList<>();

        //ljudi koji imaju stan
        whereClause = "id_kvart = ?";
        for (UsableOtherUser usr:allUsers) {
            NudimStan stan = findUserWithApt(usr.getId_korisnik() , haveAptPrice);
            if(stan != null) {
                if (getIfUserSwiped(usr.getId_korisnik(), au.getId_korisnik() , dbh)){
                    toRemove1.add(usr);
                }else{
                    if (actUsr.getCimer_spol() != 'S' && actUsr.getCimer_spol() != usr.getSpol()) {
                        toRemove1.add(usr);
                    }else {
                        usr.setId_kvart(stan.getId_kvart());

                        whereArgs[0] = Integer.toString(stan.getId_kvart());
                        Kvart kvartLokacija = dbh.singleQueryKvart(whereClause, whereArgs, null, null, null);

                        usr.setCijenaMax((int) stan.getCijena());
                        usr.setApt(true);
                        usr.setZasebnaSoba(stan.isZasebna_soba());
                        setKvartData(usr,kvartLokacija);
                        haveApt.add(usr);
                        toRemove1.add(usr);
                    }
                }
            }
        }
        allUsers.removeAll(toRemove1);

        List<UsableOtherUser> toRemove2 = new ArrayList<>();
        //ljudi koji nemaju stan

        for (UsableOtherUser usr:allUsers) {
            TrazimStan stan = findUserNeedApt(usr.getId_korisnik() , needAptPrice);

            if (stan != null) {
                if(getIfUserSwiped(usr.getId_korisnik(), au.getId_korisnik() , dbh)){
                    toRemove2.add(usr);
                }else{
                    if (actUsr.getCimer_spol() != 'S' && actUsr.getCimer_spol() != usr.getSpol()) {
                        toRemove2.add(usr);
                    }else {
                        whereClause = "id_potraga = ?";
                        whereArgs[0] = Integer.toString(stan.getId_potraga());
                        List<PotragaLokacija> lokacija = dbh.queryPotragaLokacija(whereClause, whereArgs, null, null, null);

                        usr.setCijenaMax((int) stan.getCijena_do());
                        usr.setApt(false);

                        setOtherUserLocationData(usr , lokacija);
                        needApt.add(usr);
                        toRemove2.add(usr);
                    }
                }
            }
        }
        allUsers.removeAll(toRemove2);

        if(actUsr.isApt()){
            return needApt;
        }else{
            needApt.addAll(haveApt);
            return needApt;
        }
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

    public List<UsableOtherUser> writeData(List<Korisnik> users){
        List<UsableOtherUser> usableOtherUsers = new ArrayList<>();
        for (Korisnik usr : users){
            usableOtherUsers.add(new UsableOtherUser(usr.getId_korisnik(), usr.getIme(),usr.getGodina_rodenja(),usr.getOpis(),usr.getSpol(),usr.getId_fakultet(),usr.isPusac(),usr.isLjubimac(),usr.isMiran_zivot()));
        }

        return usableOtherUsers;
    }

    @SuppressWarnings("IfStatementWithIdenticalBranches")
    public UsableActiveUser writeActiveUserData(Korisnik usr, DatabaseHelper dbh) {
        UsableActiveUser activeUser = new UsableActiveUser(usr.getId_korisnik(), usr.getUsername(), usr.getEmail(), usr.getPassword(), usr.getIme(), usr.getGodina_rodenja(), usr.getOpis(), usr.getSpol(), usr.getId_fakultet(), usr.isPusac(), usr.isLjubimac(), usr.isMiran_zivot(), usr.getCimer_spol(), usr.getCimer_godine_od(), usr.getCimer_godine_do(), usr.isCimer_pusac(), usr.isCimer_ljubimac());

        List<Korisnik> users = (dbh.queryKorisnik(null, null, null, null, null));
        for (Korisnik user : users) {
            if (user.getId_korisnik() == usr.getId_korisnik()) {
                List<NudimStan> haveAptPrice = dbh.queryNudimStan(null, null, null, null, null);
                NudimStan stan = findUserWithApt(usr.getId_korisnik(), haveAptPrice);
                if (stan != null) {
                    activeUser.setCijenaMax((int) stan.getCijena());
                    activeUser.setZasebna_soba(stan.isZasebna_soba());
                    activeUser.setApt(true);
                    return activeUser;
                } else{
                    List<TrazimStan> needAptPrice = dbh.queryTrazimStan(null, null, null, null, null);
                    TrazimStan stan2 = findUserNeedApt(usr.getId_korisnik(), needAptPrice);
                    if (stan2 != null) {
                        String whereClause = "id_potraga = ?";
                        String[] whereArgs = new String[1];
                        whereArgs[0] = Integer.toString(stan2.getId_potraga());
                        List<PotragaLokacija> potragaLokacija = dbh.queryPotragaLokacija(whereClause, whereArgs, null, null, null);
                        if (!potragaLokacija.isEmpty()) {

                            setActiveUserLocationData(activeUser,potragaLokacija);
                            activeUser.setCijenaMax((int) stan2.getCijena_do());
                            activeUser.setZasebna_soba(stan2.isZasebna_soba());

                            return activeUser;
                        }else{
                            activeUser.setCijenaMax((int) stan2.getCijena_do());
                            activeUser.setZasebna_soba(stan2.isZasebna_soba());
                            return activeUser;
                        }
                    } else{
                        return new UsableActiveUser();
                    }
                }
            }
        }
        return new UsableActiveUser();
    }


    public boolean getIfUserSwiped(int usr_id , int actUsr_id , DatabaseHelper dbh) {
        Swipe swipe;

        if(actUsr_id < usr_id){
            swipe = dbh.checkSwipe(String.valueOf(actUsr_id), String.valueOf(usr_id));
            if(swipe != null && swipe.getId_1() > 0 && swipe.getId_2() > 0){
                return swipe.isSwipe_1() != null;
            }
        } else if(actUsr_id > usr_id){
            swipe = dbh.checkSwipe(String.valueOf(usr_id), String.valueOf(actUsr_id));
            if(swipe != null && swipe.getId_1() > 0 && swipe.getId_2() > 0){
                return swipe.isSwipe_2() != null;
            }
        }

        return false;
    }

    public boolean getIfUserSwiped(List<Swipe> swipes ,int actUsr_id , int usr_id) {
        for (Swipe swp : swipes){
            if(swp.getId_1() == actUsr_id && swp.getId_2() == usr_id){
                if(swp.isSwipe_1() != null) return true;
            }if(swp.getId_2() == actUsr_id && swp.getId_1() > usr_id){
                if(swp.isSwipe_2() != null) return true;
            }
        }
        return false;
    }

    public void setKvartData(UsableOtherUser usr , Kvart kvart){
        usr.setId_lokacija(kvart.getId_lokacija());
        usr.setKvart_ime(kvart.getNaziv());
        usr.setId_kvart(kvart.getId_kvart());
    }
    public void setOtherUserLocationData(UsableOtherUser usr , List<PotragaLokacija> pot){
        for (PotragaLokacija pl : pot){
            usr.setId_lokacija(pl.getId_lokacija()-1);
        }
    }
    public void setActiveUserLocationData(UsableActiveUser usr , List<PotragaLokacija> pot){
        for (PotragaLokacija pl : pot){
            usr.setId_lokacija(pl.getId_lokacija()-1);
        }
    }

    public boolean compareLocData(UsableActiveUser au , UsableOtherUser ou){
        int count = 0;
        for (int i : au.getIdLokacija()){
            if (i == 1 && ou.getIdLokacija()[count] == i)return true;
            count++;
        }
        return false;
    }
}



