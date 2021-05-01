package com.example.cn.Sorting;

import android.os.Build;
import android.provider.ContactsContract;

import androidx.annotation.RequiresApi;

import com.example.cn.activeUser;
import com.example.cn.model.Korisnik;
import com.example.cn.model.NudimStan;
import com.example.cn.model.TrazimStan;
import com.example.cn.otherUser;
import com.example.cn.sql.DatabaseHelper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;

public class SimilarityGradeSorting {

    //klasa u kojoj odredjujemo ocjenu slicnosti aktivnog korisnika sa svim ostalim korisnicima u bazi
    //Instancira se jednom prilikom ulaska u aplikaciju te vraca popis svih korisnika sortiran po slicnosti

    public List<otherUser> Grade(activeUser actUsr , List<otherUser> othrUsr){
            //1. dio , prebaci korisnike koji se ne podudaraju sa eksplicitnim zahtjevima na kraj
            //To postizemo tako da im grade postavimo na -1
            //PROBLEM: na ovaj nacin tipa ako ne zelimo pusaca , ta osoba ide na kraj te ce biti randomly sortirana
            // s ostalim osobama iako je po drugim stvarima mozda slicnija aktivnom korisniku od ostalih

               boolean[] explicitReq = {actUsr.isCimer_pusac(),actUsr.isCimer_ljubimac(),actUsr.isMiran_zivot(),actUsr.isTrazimStan()};
        for (otherUser usr : othrUsr) {
            if((Boolean.compare(explicitReq[0] , usr.isPusac()) < 0 || Boolean.compare(explicitReq[1] , usr.isLjubimac()) < 0 || Boolean.compare(explicitReq[2] , usr.isMiran_zivot()) < 0 || actUsr.getCimer_spol() != usr.getSpol() || (!explicitReq[3]  && !usr.isTrazimStan()))) {
                usr.setGrade(-1);
            }
        }
            //2.dio usporedjivati jednu po jednu sličnost/razliku između korisnika od najbitnije do najnebitnije
            //PROBLEM: bitnost ovih razlika/sličnosti je poprilično subjektivna pa po pojedinačnim slucajevima vrlo vjerovatno
            //ne ispada najtocnije

        for (otherUser usr : othrUsr) {
            if(actUsr.getId_fakultet() == usr.getId_fakultet()) {
                //dodati određenu kolicinu bodova za slicnost
                usr.setGrade(usr.getGrade() + 30);
            }
            //Kasnije dodati nove argumente za povecanje/smanjenje ocjene
        }

            //3.dio pozvati funkciju sortiranja te vratiti sortirano polje za daljnje korištenje
        othrUsr = sort(othrUsr);
        return othrUsr;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<otherUser> pullData(){
        DatabaseHelper dbh = new DatabaseHelper();
       List<otherUser>completedList = dbh.queryKorisnik(/* String whereClause, String[] whereArgs, String groupBy, String having, String orderBy */);
       List<NudimStan>haveApt = dbh.queryNudimStan(/* String whereClause, String[] whereArgs, String groupBy, String having, String orderBy */);
       List<TrazimStan>needApt = dbh.queryTrazimStan(/* String whereClause, String[] whereArgs, String groupBy, String having, String orderBy */);

        for (otherUser usr:completedList) {
            //zapisuje cjenu stana na cijenaMin za korisnika
            usr.setCijenaMin((int) findUserWithApt(usr.getId_korisnik() , haveApt).getCijena());
            //zapisuje budget od min do max
            if(isNull(usr.getCijenaMin())){
                usr.setCijenaMax((int) findUserWithoutApt(usr.getId_korisnik(), needApt).getCijena_do());
                usr.setCijenaMin((int) findUserWithoutApt(usr.getId_korisnik(), needApt).getCijena_od());
                usr.setTrazimStan(true);
            }else{
                usr.setTrazimStan(false);
            }
        }
        return completedList;
    }

    public NudimStan findUserWithApt(int id , List<NudimStan>list){
        for (NudimStan item:list) {
            if (item.getId_korisnik() == id)return item;
        }
        return null;
    }
    public TrazimStan findUserWithoutApt(int id , List<TrazimStan>list){
        for (TrazimStan item:list) {
            if (item.getId_korisnik() == id)return item;
        }
        return null;
    }


    public List<otherUser> sort(List<otherUser>list){
        Collections.sort(list);
        return list;
    }
}
