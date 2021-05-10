package com.example.cn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cn.model.Korisnik;
import com.example.cn.model.KorisnikLjubimac;
import com.example.cn.model.Kvart;
import com.example.cn.model.NudimStan;
import com.example.cn.model.PotragaLokacija;
import com.example.cn.model.TrazimStan;
import com.example.cn.sql.DatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApartmentAndRoommate extends AppCompatActivity implements View.OnClickListener{
    private AppCompatActivity activity = ApartmentAndRoommate.this;

    private TextView seekBarValue;
    private SeekBar seekBar;
    private int priceTo = 5000;
    private CheckBox west1, east1, center1, suburbs1, other1;
    private RadioButton femaleGender, maleGender, maleFemale;
    private EditText yearFrom, yearTo;
    private RadioButton roommateSmoker, roommateNonSmoker;
    private RadioButton roommatePet, roommateNoPet;
    private Button button;
    private DatabaseHelper databaseHelper = new DatabaseHelper(activity);

    TrazimStan needApt = new TrazimStan();
    KorisnikLjubimac havePet = new KorisnikLjubimac();
    PotragaLokacija area = new PotragaLokacija();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_and_roommate);

        initViews();
        initListeners();


    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initListeners() {
        button.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                seekBarValue.setText(""+progress+ " kn");
                seekBarValue.setX(seekBar.getX() + value - 60);
                priceTo = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
              //seekBarValue.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
              //seekBarValue.setVisibility(View.INVISIBLE);
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @SuppressLint("WrongViewCast")
    private void initViews(){
        seekBarValue = findViewById(R.id.seekBarValue);
        seekBar = findViewById(R.id.seekBar);

        west1 = findViewById(R.id.west);
        east1 = findViewById(R.id.east);
        center1 = findViewById(R.id.center);
        suburbs1 = findViewById(R.id.suburbs);
        other1 = findViewById(R.id.other);

        femaleGender = findViewById(R.id.female);
        maleGender = findViewById(R.id.male);
        maleFemale = findViewById(R.id.femaleMale);

        yearFrom = findViewById(R.id.year1);
        yearTo = findViewById(R.id.year2);

        roommateSmoker = findViewById(R.id.smoke);
        roommateNonSmoker = findViewById(R.id.noSmoke);

        roommatePet = findViewById(R.id.pet);
        roommateNoPet = findViewById(R.id.noPet);

        button = findViewById(R.id.apartmentAndRoommateButton);
    }


    @Override
    public void onClick(View v) {
        inputData();
        /*Intent main = new Intent(this, HomePage.class);
        startActivity(main);*/
    }

    private void inputData(){


        int yearOfRoommateFrom = Integer.parseInt(yearFrom.getText().toString().trim());
        int yearOfRoommateTo = Integer.parseInt(yearTo.getText().toString().trim());

        // !!! FALI: trazim zasebnu sobu ili sobu koju cemo dijeliti

        boolean[] location = new boolean[4];
        Arrays.fill(location, false);

        if(west1.isChecked()){
            location[0] = true;
        }
        if(center1.isChecked()){
            location[1] = true;
        }
        if(east1.isChecked()){
            location[2] = true;
        }
        if(suburbs1.isChecked()){
            location[3] = true;
        }
        if(other1.isChecked()){
            Arrays.fill(location, true); // ako mu je svejedno, postavi sve na true
        }

        char gender = 0;
        if(maleGender.isChecked()){
            gender = 'M';
        }
        if(femaleGender.isChecked()){
            gender = 'Z';
        }


        boolean smoker = false;
        if(roommateSmoker.isChecked()){
            smoker = true;
        }

        boolean pet = false;
        if(roommatePet.isChecked()){
            pet = true;
        }

        // Preuzmi objekt i polje booleana
        Intent intent  = getIntent();
        Korisnik userActive = (Korisnik) intent.getSerializableExtra("InhUser");
        boolean[] pets = intent.getBooleanArrayExtra("Pets");

        // Zapis u objekt
        userActive.setCimer_pusac(smoker);
        userActive.setCimer_spol(gender);
        userActive.setCimer_godine_od(yearOfRoommateFrom);
        userActive.setCimer_godine_do(yearOfRoommateTo);
        userActive.setCimer_ljubimac(pet);

        databaseHelper.insertKorisnika(userActive);

        // dohvacanje unesenog korisnika skupa sa automatski postavljenim id-om
        List<Korisnik> userList = new ArrayList<Korisnik>();
        String whereClause = "username = ?";
        String[] whereArgs = new String[1];
        whereArgs[0] = userActive.getUsername();
        userList.addAll(databaseHelper.queryKorisnik(whereClause, whereArgs, null, null, null));

        if(!userList.isEmpty()){
            // unos podataka u tablicu TrazimStan
            userActive = userList.get(0);
            needApt.setId_korisnik(userActive.getId_korisnik());
            needApt.setCijena_do(priceTo);
            needApt.setZasebna_soba(true); // ISPRAVITI kad se doda ta mogucnost


            databaseHelper.insertTrazimStan(needApt);

            // unos podataka u tablicu KorisnikLjubimac
            for(int i = 0; i < pets.length; i++){
                if(pets[i]){
                    havePet.setId_ljubimac(i+1);
                    havePet.setId_korisnik(userActive.getId_korisnik());
                    databaseHelper.insertKorisnikLjubimac(havePet);
                }
            }

            List<TrazimStan> needAptList = new ArrayList<TrazimStan>();
            whereClause = "id_korisnik = ?";
            whereArgs[0] = String.valueOf(needApt.getId_korisnik());
            needAptList.addAll(databaseHelper.queryTrazimStan(whereClause, whereArgs, null, null, null));
            needApt = needAptList.get(0);

            // unos podataka u tablicu PotragaLokacija
            for(int i = 0; i < location.length; i++){
                if(location[i]){
                    area.setId_lokacija(i+1);
                    area.setId_potraga(needApt.getId_potraga());
                    databaseHelper.insertPotragaLokacija(area);
                }
            }

        } else{
            // Dodati alert: Doslo je do greske
        }

        //2. prosljedi dalje
        Intent i2 = new Intent(this, HomePage.class);
        i2.putExtra("InhUser", userActive);
        startActivity(i2);

        /* ANGEL ----> mozes ovdje sve upisati u bazu jer je ovo zadnja stranica
         * prije homepagea ili mozes u homepageu kako god ti je lakse
         * ako ovdje budes htjela samo makni ovo prosljedivanje i otkomentiraj
         * intent u metodi onClck*/

    }
}