package com.example.cn.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.cn.model.Fakultet;
import com.example.cn.model.Korisnik;
import com.example.cn.model.KorisnikLjubimac;
import com.example.cn.model.Ljubimac;
import com.example.cn.model.Lokacija;
import com.example.cn.model.Kvart;
import com.example.cn.model.NudimStan;
import com.example.cn.model.PotragaLokacija;
import com.example.cn.model.Swipe;
import com.example.cn.model.TrazimStan;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 62;
    // Database Name
    private static final String DATABASE_NAME = "CasaNostra.db";

    // Tablica fakultet
    private static final String TABLE_FAKULTET = "Fakultet";

    private static final String FAKULTET_ID = "id_fakultet";
    private static final String FAKULTET_NAZIV = "naziv";

    private String CREATE_FAKULTET_TABLE = "CREATE TABLE " + TABLE_FAKULTET + " (" +
            FAKULTET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FAKULTET_NAZIV + " varchar(255) NOT NULL" +
            ")";



    // Tablica korisnik
    private static final String TABLE_KORISNIK = "Korisnik";

    private static final String KORISNIK_ID = "id_korisnik";
    private static final String KORISNIK_USERNAME = "username";
    private static final String KORISNIK_EMAIL = "email";
    private static final String KORISNIK_PASSWORD = "password";
    private static final String KORISNIK_IME = "ime";
    private static final String KORISNIK_GODINA = "godina_rodenja";
    private static final String KORISNIK_OPIS = "opis";
    private static final String KORISNIK_SPOL = "spol";
    private static final String KORISNIK_ID_FAKULTET = "id_fakultet";
    private static final String KORISNIK_PUSAC = "pusac";
    private static final String KORISNIK_LJUBIMAC = "ljubimac";
    private static final String KORISNIK_CIMER_SPOL = "cimer_spol";
    private static final String KORISNIK_CIMER_GODINE_OD = "cimer_godine_od";
    private static final String KORISNIK_CIMER_GODINE_DO = "cimer_godine_do";
    private static final String KORISNIK_CIMER_PUSAC = "cimer_pusac";
    private static final String KORISNIK_CIMER_LJUBIMAC = "cimer_ljubimac";
    private static final String KORISNIK_MIRAN_ZIVOT = "miran_zivot";

    private String CREATE_KORISNIK_TABLE = "CREATE TABLE " + TABLE_KORISNIK + " (" +
            KORISNIK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KORISNIK_USERNAME + " varchar(255) NOT NULL, " +
            KORISNIK_PASSWORD + " varchar(255) NOT NULL, " +
            KORISNIK_EMAIL + " varchar(255) NOT NULL, " +
            KORISNIK_IME + " varchar(255) NOT NULL, " +
            KORISNIK_GODINA + " int(11) NOT NULL, " +
            KORISNIK_OPIS + " varchar(255) DEFAULT NULL, " +
            KORISNIK_SPOL + " char(8) NOT NULL, " +
            KORISNIK_ID_FAKULTET + " int(11) NOT NULL," +
            KORISNIK_PUSAC + " tinyint(1) NOT NULL, " +
            KORISNIK_LJUBIMAC + " tinyint(1) NOT NULL, " +
            KORISNIK_CIMER_SPOL + " char(8) DEFAULT NULL, " +
            KORISNIK_CIMER_GODINE_OD + " int(11) DEFAULT NULL, " +
            KORISNIK_CIMER_GODINE_DO + " int(11) DEFAULT NULL, " +
            KORISNIK_CIMER_PUSAC + " tinyint(1) NOT NULL, " +
            KORISNIK_CIMER_LJUBIMAC + " tinyint(1) NOT NULL, " +
            KORISNIK_MIRAN_ZIVOT + " tinyint(1) NOT NULL, " +
            "FOREIGN KEY (" + KORISNIK_ID_FAKULTET + ") REFERENCES " + TABLE_FAKULTET + "(" + FAKULTET_ID + ")" +
            ")";

    // Tablica ljubimac
    private static final String TABLE_LJUBIMAC = "Ljubimac";

    private static final String LJUBIMAC_ID = "id_ljubimac";
    private static final String LJUBIMAC_VRSTA = "vrsta";

    private String CREATE_LJUBIMAC_TABLE = "CREATE TABLE " + TABLE_LJUBIMAC + " (" +
            LJUBIMAC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LJUBIMAC_VRSTA + " varchar(255) NOT NULL" +
            ")";

    // Veza korisnik_ljubimac
    private static final String RELATION_KORISNIK_LJUBIMAC = "Korisnik_ljubimac";

    private static final String RELATION_KORISNIK_ID = "id_korisnik";
    private static final String RELATION_LJUBIMAC_ID = "id_ljubimac";

    private String CREATE_KORISNIK_LJUBIMAC_RELATION = "CREATE TABLE " + RELATION_KORISNIK_LJUBIMAC + "(" +
            RELATION_KORISNIK_ID + " INTEGER NOT NULL, " +
            RELATION_LJUBIMAC_ID + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + RELATION_KORISNIK_ID + ") REFERENCES " + TABLE_KORISNIK + "(" + KORISNIK_ID + ") ON DELETE CASCADE, " +
            "FOREIGN KEY (" + RELATION_LJUBIMAC_ID + ") REFERENCES " + TABLE_LJUBIMAC + "(" + LJUBIMAC_ID + "), " +
            "PRIMARY KEY (" + RELATION_KORISNIK_ID + ", " + RELATION_LJUBIMAC_ID + ")" +
            ")";

    // Tablica lokacija
    private static final String TABLE_LOKACIJA = "Lokacija";

    private static final String LOKACIJA_ID = "id_lokacija";
    private static final String LOKACIJA_NAZIV = "naziv";

    private String CREATE_LOKACIJA_TABLE = "CREATE TABLE " + TABLE_LOKACIJA + " (" +
            LOKACIJA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LOKACIJA_NAZIV + " varchar(255) NOT NULL" +
            ")";

    // Tablica kvart
    private static final String TABLE_KVART = "Kvart";

    private static final String KVART_ID = "id_kvart";
    private static final String KVART_NAZIV = "naziv";
    private static final String KVART_ID_LOKACIJA = "id_lokacija";

    private String CREATE_KVART_TABLE = "CREATE TABLE " + TABLE_KVART + " (" +
            KVART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KVART_NAZIV + " varchar(255) NOT NULL, " +
            KVART_ID_LOKACIJA + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + KVART_ID_LOKACIJA + ") REFERENCES " + TABLE_LOKACIJA + "(" + LOKACIJA_ID + ")" +
            ")";

    // Tablica trazim_stan
    private static final String TABLE_TRAZIM_STAN = "Trazim_stan";

    private static final String TRAZIM_STAN_ID_POTRAGA = "id_potraga";
    private static final String TRAZIM_STAN_ID_KORISNIK = "id_korisnik";
    private static final String TRAZIM_STAN_CIJENA_DO = "cijena_do";
    private static final String TRAZIM_STAN_ZASEBNA_SOBA = "zasebna_soba";

    private String CREATE_TRAZIM_STAN_TABLE = "CREATE TABLE " + TABLE_TRAZIM_STAN + "(" +
            TRAZIM_STAN_ID_POTRAGA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TRAZIM_STAN_ID_KORISNIK + " INTEGER NOT NULL, " +
            TRAZIM_STAN_CIJENA_DO + " double DEFAULT NULL, " +
            TRAZIM_STAN_ZASEBNA_SOBA + " tinyint(1) NOT NULL, " +
            "FOREIGN KEY (" + TRAZIM_STAN_ID_KORISNIK + ") REFERENCES " + TABLE_KORISNIK + "(" + KORISNIK_ID + ") ON DELETE CASCADE" +
            ")";

    // Tablica nudim_stan
    private static final String TABLE_NUDIM_STAN = "Nudim_stan";

    private static final String NUDIM_STAN_ID_STAN = "id_stan";
    private static final String NUDIM_STAN_ID_KORISNIK = "id_korisnik";
    private static final String NUDIM_STAN_CIJENA = "cijena";
    private static final String NUDIM_STAN_ID_KVART = "id_kvart";
    private static final String NUDIM_STAN_ZASEBNA_SOBA = "zasebna_soba";

    private String CREATE_NUDIM_STAN_TABLE = "CREATE TABLE " + TABLE_NUDIM_STAN + "(" +
            NUDIM_STAN_ID_STAN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NUDIM_STAN_ID_KORISNIK + " INTEGER NOT NULL, " +
            NUDIM_STAN_CIJENA + " double DEFAULT NULL, " +
            NUDIM_STAN_ID_KVART + " INTEGER NOT NULL, " +
            NUDIM_STAN_ZASEBNA_SOBA + " tinyint(1) NOT NULL, " +
            "FOREIGN KEY (" + NUDIM_STAN_ID_KORISNIK + ") REFERENCES " + TABLE_KORISNIK + "(" + KORISNIK_ID + ") ON DELETE CASCADE, " +
            "FOREIGN KEY (" + NUDIM_STAN_ID_KVART + ") REFERENCES " + TABLE_KVART + "(" + KVART_ID + ") " +
            ")";

    // Veza potraga_lokacija
    private static final String RELATION_POTRAGA_LOKACIJA = "Potraga_lokacija";

    private static final String RELATION_POTRAGA_ID = "id_potraga";
    private static final String RELATION_LOKACIJA_ID = "id_lokacija";

    private String CREATE_POTRAGA_LOKACIJA_RELATION = "CREATE TABLE " + RELATION_POTRAGA_LOKACIJA + "(" +
            RELATION_POTRAGA_ID + " INTEGER NOT NULL, " +
            RELATION_LOKACIJA_ID + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + RELATION_POTRAGA_ID + ") REFERENCES " + TABLE_TRAZIM_STAN + "(" + TRAZIM_STAN_ID_POTRAGA + ")  ON DELETE CASCADE, " +
            "FOREIGN KEY (" + RELATION_LOKACIJA_ID + ") REFERENCES " + TABLE_LOKACIJA + "(" + LOKACIJA_ID + "), " +
            "PRIMARY KEY (" + RELATION_POTRAGA_ID + ", " + RELATION_LOKACIJA_ID + ")" +
            ")";

    // Tablica swipe
    private static final String TABLE_SWIPE = "Swipe";

    private static final String SWIPE_ID1 = "id_1";
    private static final String SWIPE_ID2 = "id_2";
    private static final String SWIPE_SWIPE1 = "swipe_1";
    private static final String SWIPE_SWIPE2 = "swipe_2";

    private String CREATE_SWIPE_TABLE = "CREATE TABLE " + TABLE_SWIPE + "(" +
            SWIPE_ID1 + " INTEGER NOT NULL, " +
            SWIPE_ID2 + " INTEGER NOT NULL, " +
            SWIPE_SWIPE1 + " tinyint(1) DEFAULT NULL, " +
            SWIPE_SWIPE2 + " tinyint(1) DEFAULT NULL, " +
            "FOREIGN KEY (" + SWIPE_ID1 + ") REFERENCES " + TABLE_KORISNIK + "(" + KORISNIK_ID + ") ON DELETE CASCADE, " +
            "FOREIGN KEY (" + SWIPE_ID2 + ") REFERENCES " + TABLE_KORISNIK + "(" + KORISNIK_ID + ") ON DELETE CASCADE, " +
            "PRIMARY KEY (" + SWIPE_ID1 + ", " + SWIPE_ID2 + ")" +
            ")";

    // drop table sql query
    private String DROP_FAKULTET_TABLE = "DROP TABLE IF EXISTS " + TABLE_FAKULTET;
    private String DROP_KORISNIK_TABLE = "DROP TABLE IF EXISTS " + TABLE_KORISNIK;
    private String DROP_LJUBIMAC_TABLE = "DROP TABLE IF EXISTS " + TABLE_LJUBIMAC;
    private String DROP_KORISNIK_LJUBIMAC_RELATION = "DROP TABLE IF EXISTS " + RELATION_KORISNIK_LJUBIMAC;
    private String DROP_LOKACIJA_TABLE = "DROP TABLE IF EXISTS " + TABLE_LOKACIJA;
    private String DROP_KVART_TABLE = "DROP TABLE IF EXISTS " + TABLE_KVART;
    private String DROP_TRAZIM_STAN_TABLE = "DROP TABLE IF EXISTS " + TABLE_TRAZIM_STAN;
    private String DROP_NUDIM_STAN_TABLE = "DROP TABLE IF EXISTS " + TABLE_NUDIM_STAN;
    private String DROP_POTRAGA_LOKACIJA_RELATION = "DROP TABLE IF EXISTS " + RELATION_POTRAGA_LOKACIJA;
    private String DROP_SWIPE_TABLE = "DROP TABLE IF EXISTS " + TABLE_SWIPE;



    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onConfigure(SQLiteDatabase db){
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        db.execSQL(CREATE_FAKULTET_TABLE);
        values.put(FAKULTET_NAZIV, "Filozofski fakultet u Rijeci");
        db.insert(TABLE_FAKULTET, null, values);
        values.put(FAKULTET_NAZIV, "Sveučilište u Rijeci");
        db.insert(TABLE_FAKULTET, null, values);
        values.put(FAKULTET_NAZIV, "Ekonomski fakultet u Rijeci");
        db.insert(TABLE_FAKULTET, null, values);
        values.put(FAKULTET_NAZIV, "Medicinski fakultet u Rijeci");
        db.insert(TABLE_FAKULTET, null, values);
        values.put(FAKULTET_NAZIV, "Tehnički fakultet u Rijeci");
        db.insert(TABLE_FAKULTET, null, values);
        values.put(FAKULTET_NAZIV, "Građevinski fakultet u Rijeci");
        db.insert(TABLE_FAKULTET, null, values);
        values.put(FAKULTET_NAZIV, "Pravni fakultet u Rijeci");
        db.insert(TABLE_FAKULTET, null, values);
        values.clear();

        db.execSQL(CREATE_KORISNIK_TABLE);

        db.execSQL(CREATE_LJUBIMAC_TABLE);
        values.put(LJUBIMAC_VRSTA, "Pas");
        db.insert(TABLE_LJUBIMAC, null, values);
        values.put(LJUBIMAC_VRSTA, "Mačka");
        db.insert(TABLE_LJUBIMAC, null, values);
        values.put(LJUBIMAC_VRSTA, "Papiga");
        db.insert(TABLE_LJUBIMAC, null, values);
        values.put(LJUBIMAC_VRSTA, "Hrčak");
        db.insert(TABLE_LJUBIMAC, null, values);
        values.put(LJUBIMAC_VRSTA, "Zec");
        db.insert(TABLE_LJUBIMAC, null, values);
        values.put(LJUBIMAC_VRSTA, "Zmija");
        db.insert(TABLE_LJUBIMAC, null, values);
        values.clear();

        db.execSQL(CREATE_KORISNIK_LJUBIMAC_RELATION);

        db.execSQL(CREATE_LOKACIJA_TABLE);
        values.put(LOKACIJA_NAZIV, "Zapad");
        db.insert(TABLE_LOKACIJA, null, values);
        values.put(LOKACIJA_NAZIV, "Centar");
        db.insert(TABLE_LOKACIJA, null, values);
        values.put(LOKACIJA_NAZIV, "Istok");
        db.insert(TABLE_LOKACIJA, null, values);
        values.put(LOKACIJA_NAZIV, "Prigrad");
        db.insert(TABLE_LOKACIJA, null, values);
        values.clear();

        db.execSQL(CREATE_KVART_TABLE);
        values.put(KVART_NAZIV, "Kantrida");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Zamet");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Srdoči");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Grbci");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Gornji Zamet");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Krnjevo");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Turnić");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Pehlin");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Podmurvice");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Mlaka");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Drenova");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Škurinje");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Škurinjska Draga");
        values.put(KVART_ID_LOKACIJA, 1);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Potok");
        values.put(KVART_ID_LOKACIJA, 2);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Banderovo");
        values.put(KVART_ID_LOKACIJA, 2);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Belveder");
        values.put(KVART_ID_LOKACIJA, 2);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Brajda");
        values.put(KVART_ID_LOKACIJA, 2);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Kozala");
        values.put(KVART_ID_LOKACIJA, 2);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Brašćine");
        values.put(KVART_ID_LOKACIJA, 2);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Centar");
        values.put(KVART_ID_LOKACIJA, 2);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Školjić");
        values.put(KVART_ID_LOKACIJA, 2);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Bulevard");
        values.put(KVART_ID_LOKACIJA, 2);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Trsat");
        values.put(KVART_ID_LOKACIJA, 3);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Vojak");
        values.put(KVART_ID_LOKACIJA, 3);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Krimeja");
        values.put(KVART_ID_LOKACIJA, 3);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Pećine");
        values.put(KVART_ID_LOKACIJA, 3);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Gornja Vežica");
        values.put(KVART_ID_LOKACIJA, 3);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Podvežica");
        values.put(KVART_ID_LOKACIJA, 3);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Draga");
        values.put(KVART_ID_LOKACIJA, 3);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Opatija");
        values.put(KVART_ID_LOKACIJA, 4);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Matulji");
        values.put(KVART_ID_LOKACIJA, 4);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Viškovo");
        values.put(KVART_ID_LOKACIJA, 4);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Kastav");
        values.put(KVART_ID_LOKACIJA, 4);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Grobnik");
        values.put(KVART_ID_LOKACIJA, 4);
        db.insert(TABLE_KVART, null, values);
        values.put(KVART_NAZIV, "Kostrena");
        values.put(KVART_ID_LOKACIJA, 4);
        db.insert(TABLE_KVART, null, values);
        values.clear();

        db.execSQL(CREATE_TRAZIM_STAN_TABLE);
        db.execSQL(CREATE_NUDIM_STAN_TABLE);
        db.execSQL(CREATE_POTRAGA_LOKACIJA_RELATION);
        db.execSQL(CREATE_SWIPE_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_SWIPE_TABLE);
        db.execSQL(DROP_POTRAGA_LOKACIJA_RELATION);
        db.execSQL(DROP_NUDIM_STAN_TABLE);
        db.execSQL(DROP_TRAZIM_STAN_TABLE);
        db.execSQL(DROP_KVART_TABLE);
        db.execSQL(DROP_LOKACIJA_TABLE);
        db.execSQL(DROP_KORISNIK_LJUBIMAC_RELATION);
        db.execSQL(DROP_LJUBIMAC_TABLE);
        db.execSQL(DROP_KORISNIK_TABLE);
        db.execSQL(DROP_FAKULTET_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void insertKorisnika(Korisnik korisnik) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KORISNIK_USERNAME, korisnik.getUsername());
        values.put(KORISNIK_EMAIL, korisnik.getEmail());
        values.put(KORISNIK_PASSWORD, korisnik.getPassword());
        values.put(KORISNIK_IME, korisnik.getIme());
        values.put(KORISNIK_GODINA, korisnik.getGodina_rodenja());
        values.put(KORISNIK_OPIS, korisnik.getOpis());
        values.put(KORISNIK_SPOL, (int) korisnik.getSpol());
        values.put(KORISNIK_ID_FAKULTET, korisnik.getId_fakultet());

        int pusac = (korisnik.isPusac()) ? 1 : 0;
        values.put(KORISNIK_PUSAC, pusac);

        int ljubimac = (korisnik.isLjubimac()) ? 1 : 0;
        values.put(KORISNIK_LJUBIMAC, ljubimac);

        values.put(KORISNIK_CIMER_SPOL, (int) korisnik.getCimer_spol());
        values.put(KORISNIK_CIMER_GODINE_OD, korisnik.getCimer_godine_od());
        values.put(KORISNIK_CIMER_GODINE_DO, korisnik.getCimer_godine_do());

        int cimerPusac = (korisnik.isCimer_pusac()) ? 1 : 0;
        values.put(KORISNIK_CIMER_PUSAC, cimerPusac);

        int cimerLjubimac = (korisnik.isCimer_ljubimac()) ? 1 : 0;
        values.put(KORISNIK_CIMER_LJUBIMAC, cimerLjubimac);

        int miranZivot = (korisnik.isMiran_zivot()) ? 1 : 0;
        values.put(KORISNIK_MIRAN_ZIVOT, miranZivot);

        // Inserting Row
        db.insert(TABLE_KORISNIK, null, values);
        db.close();
    }

    public void insertTrazimStan(TrazimStan trazimStan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRAZIM_STAN_ID_KORISNIK, trazimStan.getId_korisnik());
        values.put(TRAZIM_STAN_CIJENA_DO, trazimStan.getCijena_do());

        int zasebnaSoba = (trazimStan.isZasebna_soba()) ? 1 : 0;
        values.put(TRAZIM_STAN_ZASEBNA_SOBA, zasebnaSoba);

        // Inserting Row
        db.insert(TABLE_TRAZIM_STAN, null, values);
        db.close();
    }


    public void insertPotragaLokacija(PotragaLokacija potragaLokacija) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RELATION_LOKACIJA_ID, potragaLokacija.getId_lokacija());
        values.put(RELATION_POTRAGA_ID, potragaLokacija.getId_potraga());

        // Inserting Row
        db.insert(RELATION_POTRAGA_LOKACIJA, null, values);
        db.close();
    }

    public void insertNudimStan(NudimStan nudimStan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NUDIM_STAN_ID_KORISNIK, nudimStan.getId_korisnik());
        values.put(NUDIM_STAN_CIJENA, nudimStan.getCijena());
        values.put(NUDIM_STAN_ID_KVART, nudimStan.getId_kvart());

        int zasebnaSoba = (nudimStan.isZasebna_soba()) ? 1 : 0;
        values.put(NUDIM_STAN_ZASEBNA_SOBA, zasebnaSoba);

        // Inserting Row
        db.insert(TABLE_NUDIM_STAN, null, values);
        db.close();
    }

    public void insertKorisnikLjubimac(KorisnikLjubimac korisnikLjubimac) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RELATION_KORISNIK_ID, korisnikLjubimac.getId_korisnik());
        values.put(RELATION_LJUBIMAC_ID, korisnikLjubimac.getId_ljubimac());

        // Inserting Row
        db.insert(RELATION_KORISNIK_LJUBIMAC, null, values);
        db.close();
    }

    public void insertSwipe(Swipe swipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SWIPE_ID1, swipe.getId_1());
        values.put(SWIPE_ID2, swipe.getId_2());

        if(swipe.isSwipe_1() != null){
            if(swipe.isSwipe_1() == true){
                int swipe1 = 1;
                values.put(SWIPE_SWIPE1, swipe1);
            } else if(swipe.isSwipe_1() == false){
                int swipe1 = 0;
                values.put(SWIPE_SWIPE1, swipe1);
            }
        }


        if(swipe.isSwipe_2() != null){
            if(swipe.isSwipe_2() == true){
                int swipe2 = 1;
                values.put(SWIPE_SWIPE2, swipe2);
            } else if(swipe.isSwipe_2() == false){
                int swipe2 = 0;
                values.put(SWIPE_SWIPE2, swipe2);
            }
        }


        // Inserting Row
        db.insert(TABLE_SWIPE, null, values);
        db.close();
    }

    public Swipe checkSwipe(String id_1, String id_2) {
        // array of columns to fetch

        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = "id_1 = ? AND id_2 = ?";
        // selection argument
        String[] selectionArgs = {id_1, id_2};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_SWIPE, //Table to query
                null,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order

        Swipe swipe = new Swipe();
        if (cursor.moveToFirst()) {
            swipe.setId_1(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_ID1))));
            swipe.setId_2(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_ID2))));
            if(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE1)) != null){
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE1))) == 1){
                    swipe.setSwipe_1(true);
                } else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE1))) == 0){
                    swipe.setSwipe_1(false);
                }
            }

            if(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE2)) != null){
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE2))) == 1){
                    swipe.setSwipe_2(true);
                } else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE2))) == 0){
                    swipe.setSwipe_2(false);
                }
            }

        }
        cursor.close();
        db.close();
        // return user list
        return swipe;
    }

    public void updateSwipe(Swipe swipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SWIPE_ID1, swipe.getId_1());
        values.put(SWIPE_ID2, swipe.getId_2());

        if(swipe.isSwipe_1() != null){
            if(swipe.isSwipe_1() == true){
                int swipe1 = 1;
                values.put(SWIPE_SWIPE1, swipe1);
            } else if(swipe.isSwipe_1() == false){
                int swipe1 = 0;
                values.put(SWIPE_SWIPE1, swipe1);
            }
        }


        if(swipe.isSwipe_2() != null){
            if(swipe.isSwipe_2() == true){
                int swipe2 = 1;
                values.put(SWIPE_SWIPE2, swipe2);
            } else if(swipe.isSwipe_2() == false){
                int swipe2 = 0;
                values.put(SWIPE_SWIPE2, swipe2);
            }
        }

        // updating row
        db.update(TABLE_SWIPE, values, SWIPE_ID1 + " = ? AND " + SWIPE_ID2 + " = ?",
                new String[]{String.valueOf(swipe.getId_1()), String.valueOf(swipe.getId_2())});
        db.close();
    }

    public List<Korisnik> queryKorisnik(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        List<Korisnik> userList = new ArrayList<Korisnik>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_KORISNIK, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Korisnik korisnik = new Korisnik();
                korisnik.setId_korisnik(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_ID))));
                korisnik.setUsername(cursor.getString(cursor.getColumnIndex(KORISNIK_USERNAME)));
                korisnik.setEmail(cursor.getString(cursor.getColumnIndex(KORISNIK_EMAIL)));
                korisnik.setPassword(cursor.getString(cursor.getColumnIndex(KORISNIK_PASSWORD)));
                korisnik.setIme(cursor.getString(cursor.getColumnIndex(KORISNIK_IME)));
                korisnik.setGodina_rodenja(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_GODINA))));
                korisnik.setOpis(cursor.getString(cursor.getColumnIndex(KORISNIK_OPIS)));

                int korisnikSpol = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_SPOL)));
                korisnik.setSpol((char) korisnikSpol);

                korisnik.setId_fakultet(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_ID_FAKULTET))));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_PUSAC))) == 1){
                    korisnik.setPusac(true);
                } else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_PUSAC))) == 0){
                    korisnik.setPusac(false);
                }

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_LJUBIMAC))) == 1){
                    korisnik.setLjubimac(true);
                } else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_LJUBIMAC))) == 0){
                    korisnik.setLjubimac(false);
                }

                int cimerSpol = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_CIMER_SPOL)));
                korisnik.setCimer_spol((char) cimerSpol);

                korisnik.setCimer_godine_od(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_CIMER_GODINE_OD))));
                korisnik.setCimer_godine_do(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_CIMER_GODINE_DO))));
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_CIMER_PUSAC))) == 1){
                    korisnik.setCimer_pusac(true);
                } else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_CIMER_PUSAC))) == 0){
                    korisnik.setCimer_pusac(false);
                }
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_CIMER_LJUBIMAC))) == 1){
                    korisnik.setCimer_ljubimac(true);
                } else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_CIMER_LJUBIMAC))) == 0){
                    korisnik.setCimer_ljubimac(false);
                }
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_MIRAN_ZIVOT))) == 1){
                    korisnik.setMiran_zivot(true);
                } else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KORISNIK_MIRAN_ZIVOT))) == 0){
                    korisnik.setMiran_zivot(false);
                }
                // Adding user record to list
                userList.add(korisnik);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return userList;
    }

    public List<Fakultet> queryFakultet(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        List<Fakultet> fakultetList = new ArrayList<Fakultet>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAKULTET, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Fakultet fakultet = new Fakultet();
                fakultet.setId_fakultet(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FAKULTET_ID))));
                fakultet.setNaziv(cursor.getString(cursor.getColumnIndex(FAKULTET_NAZIV)));

                // Adding user record to list
                fakultetList.add(fakultet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return fakultetList;
    }

    public List<Lokacija> queryLokacija(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        List<Lokacija> lokacijaList = new ArrayList<Lokacija>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOKACIJA, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Lokacija lokacija = new Lokacija();
                lokacija.setId_lokacija(Integer.parseInt(cursor.getString(cursor.getColumnIndex(LOKACIJA_ID))));
                lokacija.setNaziv(cursor.getString(cursor.getColumnIndex(LOKACIJA_NAZIV)));

                // Adding user record to list
                lokacijaList.add(lokacija);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return lokacijaList;
    }

    public Lokacija singleQueryLokacija(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        Lokacija lokacija = new Lokacija();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOKACIJA, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lokacija.setId_lokacija(Integer.parseInt(cursor.getString(cursor.getColumnIndex(LOKACIJA_ID))));
                lokacija.setNaziv(cursor.getString(cursor.getColumnIndex(LOKACIJA_NAZIV)));
                // Adding user record to list
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lokacija;
    }

    public List<Kvart> queryKvart(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        List<Kvart> kvartList = new ArrayList<Kvart>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_KVART, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Kvart kvart = new Kvart();
                kvart.setId_kvart(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KVART_ID))));
                kvart.setNaziv(cursor.getString(cursor.getColumnIndex(KVART_NAZIV)));
                kvart.setId_lokacija(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KVART_ID_LOKACIJA))));

                // Adding user record to list
                kvartList.add(kvart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return kvartList;
    }

    public Kvart singleQueryKvart(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        Kvart kvart = new Kvart();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_KVART, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                kvart.setId_kvart(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KVART_ID))));
                kvart.setNaziv(cursor.getString(cursor.getColumnIndex(KVART_NAZIV)));
                kvart.setId_lokacija(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KVART_ID_LOKACIJA))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return kvart;
    }

    public List<TrazimStan> queryTrazimStan(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        List<TrazimStan> trazimStanList = new ArrayList<TrazimStan>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRAZIM_STAN, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TrazimStan trazimStan = new TrazimStan();
                trazimStan.setId_potraga(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TRAZIM_STAN_ID_POTRAGA))));
                trazimStan.setId_korisnik(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TRAZIM_STAN_ID_KORISNIK))));
                trazimStan.setCijena_do(Double.parseDouble(cursor.getString(cursor.getColumnIndex(TRAZIM_STAN_CIJENA_DO))));
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TRAZIM_STAN_ZASEBNA_SOBA))) == 1){
                    trazimStan.setZasebna_soba(true);
                } else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TRAZIM_STAN_ZASEBNA_SOBA))) == 0){
                    trazimStan.setZasebna_soba(false);
                }

                // Adding user record to list
                trazimStanList.add(trazimStan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return trazimStanList;
    }

    public List<NudimStan> queryNudimStan(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        List<NudimStan> nudimStanList = new ArrayList<NudimStan>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NUDIM_STAN, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NudimStan nudimStan = new NudimStan();
                nudimStan.setId_stan(Integer.parseInt(cursor.getString(cursor.getColumnIndex(NUDIM_STAN_ID_STAN))));
                nudimStan.setId_korisnik(Integer.parseInt(cursor.getString(cursor.getColumnIndex(NUDIM_STAN_ID_KORISNIK))));
                nudimStan.setCijena(Double.parseDouble(cursor.getString(cursor.getColumnIndex(NUDIM_STAN_CIJENA))));
                nudimStan.setId_kvart(Integer.parseInt(cursor.getString(cursor.getColumnIndex(NUDIM_STAN_ID_KVART))));
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(NUDIM_STAN_ZASEBNA_SOBA))) == 1){
                    nudimStan.setZasebna_soba(true);
                } else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(NUDIM_STAN_ZASEBNA_SOBA))) == 0){
                    nudimStan.setZasebna_soba(false);
                }

                // Adding user record to list
                nudimStanList.add(nudimStan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return nudimStanList;
    }

    public List<PotragaLokacija> queryPotragaLokacija(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        List<PotragaLokacija> potragaLokacijaList = new ArrayList<PotragaLokacija>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(RELATION_POTRAGA_LOKACIJA, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PotragaLokacija potragaLokacija = new PotragaLokacija();
                potragaLokacija.setId_potraga(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RELATION_POTRAGA_ID))));
                potragaLokacija.setId_lokacija(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RELATION_LOKACIJA_ID))));

                // Adding user record to list
                potragaLokacijaList.add(potragaLokacija);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return potragaLokacijaList;
    }

    public PotragaLokacija singleQueryPotragaLokacija(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        SQLiteDatabase db = this.getReadableDatabase();
        PotragaLokacija potLok = new PotragaLokacija();

        Cursor cursor = db.query(RELATION_POTRAGA_LOKACIJA, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                potLok.setId_potraga(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RELATION_POTRAGA_ID))));
                potLok.setId_lokacija(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RELATION_LOKACIJA_ID))));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list

        return potLok;
    }

    public List<Ljubimac> queryLjubimac(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        List<Ljubimac> ljubimacList = new ArrayList<Ljubimac>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LJUBIMAC, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Ljubimac ljubimac = new Ljubimac();
                ljubimac.setId_ljubimac(Integer.parseInt(cursor.getString(cursor.getColumnIndex(LJUBIMAC_ID))));
                ljubimac.setVrsta(cursor.getString(cursor.getColumnIndex(LJUBIMAC_VRSTA)));

                // Adding user record to list
                ljubimacList.add(ljubimac);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return ljubimacList;
    }

    public List<KorisnikLjubimac> queryKorisnikLjubimac(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        List<KorisnikLjubimac> korisnikLjubimacList = new ArrayList<KorisnikLjubimac>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(RELATION_KORISNIK_LJUBIMAC, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                KorisnikLjubimac korisnikLjubimac = new KorisnikLjubimac();
                korisnikLjubimac.setId_korisnik(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RELATION_KORISNIK_ID))));
                korisnikLjubimac.setId_ljubimac(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RELATION_LJUBIMAC_ID))));

                // Adding user record to list
                korisnikLjubimacList.add(korisnikLjubimac);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return korisnikLjubimacList;
    }

    public List<Swipe> querySwipe(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {

        List<Swipe> swipeList = new ArrayList<Swipe>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SWIPE, //Table to query
                null,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                groupBy,       //group the rows
                having,       //filter by row groups
                orderBy); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Swipe swipe = new Swipe();
                swipe.setId_1(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_ID1))));
                swipe.setId_2(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_ID2))));
                if(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE1)) != null){
                    if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE1))) == 1){
                        swipe.setSwipe_1(true);
                    } else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE1))) == 0){
                        swipe.setSwipe_1(false);
                    }
                }
                if(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE2)) != null){
                    if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE2))) == 1){
                        swipe.setSwipe_2(true);
                    } else if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SWIPE_SWIPE2))) == 0){
                        swipe.setSwipe_2(false);
                    }
                }


                // Adding user record to list
                swipeList.add(swipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return swipeList;
    }




    /**
     * This method to update user record
     *
     * @param korisnik
     */
    public void updateKorisnik(Korisnik korisnik) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KORISNIK_USERNAME, korisnik.getUsername());
        values.put(KORISNIK_EMAIL, korisnik.getEmail());
        values.put(KORISNIK_PASSWORD, korisnik.getPassword());
        values.put(KORISNIK_IME, korisnik.getIme());
        values.put(KORISNIK_GODINA, korisnik.getGodina_rodenja());
        values.put(KORISNIK_OPIS, korisnik.getOpis());
        values.put(KORISNIK_SPOL, (int) korisnik.getSpol());
        values.put(KORISNIK_ID_FAKULTET, korisnik.getId_fakultet());
        values.put(KORISNIK_PUSAC, korisnik.isPusac());
        values.put(KORISNIK_LJUBIMAC, korisnik.isLjubimac());
        values.put(KORISNIK_CIMER_SPOL, (int) korisnik.getCimer_spol());
        values.put(KORISNIK_CIMER_GODINE_OD, korisnik.getCimer_godine_od());
        values.put(KORISNIK_CIMER_GODINE_DO, korisnik.getCimer_godine_do());
        values.put(KORISNIK_CIMER_PUSAC, korisnik.isCimer_pusac());
        values.put(KORISNIK_CIMER_LJUBIMAC, korisnik.isCimer_ljubimac());
        values.put(KORISNIK_MIRAN_ZIVOT, korisnik.isMiran_zivot());
        // updating row
        db.update(TABLE_KORISNIK, values, KORISNIK_ID + " = ?",
                new String[]{String.valueOf(korisnik.getId_korisnik())});
        db.close();
    }
    /**
     * This method is to delete user record
     *
     * @param korisnik
     */
    public void deleteKorisnik(Korisnik korisnik) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_KORISNIK, KORISNIK_ID + " = ?",
                new String[]{String.valueOf(korisnik.getId_korisnik())});
        db.close();
    }
    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUserEmail(String email) {
        // array of columns to fetch
        String[] columns = {
                KORISNIK_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = KORISNIK_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {email};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_KORISNIK, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    public boolean checkUserUsername(String username) {
        // array of columns to fetch
        String[] columns = {
                KORISNIK_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = KORISNIK_USERNAME + " = ?";
        // selection argument
        String[] selectionArgs = {username};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_KORISNIK, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    /**
     * This method to check user exist or not
     *
     * @param username
     * @param password
     * @return true/false
     */
    public boolean checkUser(String username, String password) {
        // array of columns to fetch
        String[] columns = {
                KORISNIK_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = KORISNIK_USERNAME + " = ?" + " AND " + KORISNIK_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {username, password};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_KORISNIK, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}