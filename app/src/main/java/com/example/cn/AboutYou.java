package com.example.cn;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.cn.model.Fakultet;
import com.example.cn.model.Korisnik;
import com.example.cn.sql.DatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AboutYou extends AppCompatActivity implements View.OnClickListener {
    private AppCompatActivity activity = AboutYou.this;
    private DatabaseHelper databaseHelper;
    private List<Fakultet> listFakultet;
    private int userYear;

    /*Varijable iz kojih je potrebno citati odgovore*/
    private RadioButton genderF, genderM;
    private androidx.appcompat.widget.SwitchCompat smoker;
    private Spinner faculty;
    private RadioButton party, noParty;
    private androidx.appcompat.widget.SwitchCompat pet;
    private CheckBox dog, cat, parrot, hamster, rabbit, snake, other;
    private Spinner dropdown;
    private AppCompatButton appCompatButtonAboutYou;
    private NumberPicker year;
    Korisnik userActive = new Korisnik();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_you);

        getSupportActionBar().hide();
        initViews();
        initListeners();

        listFakultet = new ArrayList<Fakultet>();
        databaseHelper = new DatabaseHelper(activity);


        dropdown = findViewById(R.id.faculty);
        listFakultet.clear();
        listFakultet.addAll(databaseHelper.queryFakultet(null, null, null, null, "naziv ASC"));

        String[] items = new String[listFakultet.size()];

        items = listFakultet.stream().map(Fakultet::getNaziv).toArray(String[]::new);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    private void initViews() {
        /*Inicijalizacija cijele stranice (tj. njezinih "objekata")- kasnije se unose podaci u tmp varijable i upisuju
        * u klase metodom aboutYouDetails()*/

        year = findViewById(R.id.yearOfBirth);
        year.setMaxValue(2021);
        year.setMinValue(1900);
        year.setValue(2000);
        year.setWrapSelectorWheel(false);

        genderF = findViewById(R.id.female);
        genderM = findViewById(R.id.male);

        smoker = findViewById(R.id.smoker);

        dropdown = findViewById(R.id.faculty);

        party = findViewById(R.id.party);
        noParty = findViewById(R.id.noParty);

        pet = findViewById(R.id.pet);

        dog = findViewById(R.id.dog);
        cat = findViewById(R.id.cat);
        parrot = findViewById(R.id.parrot);
        rabbit = findViewById(R.id.rabbit);
        hamster = findViewById(R.id.hamster);
        snake = findViewById(R.id.snake);
        other = findViewById(R.id.other);

        appCompatButtonAboutYou = findViewById(R.id.appCompatButtonAboutYou);
    }

    private void initListeners() {
        appCompatButtonAboutYou.setOnClickListener(this);
    }

    public void onClick(View v) {
        /*Klikom ulazi u aboutYouDetails metodu pa onda ide na daljnju aktivnost*/
        aboutYouDetails();
    }

    public void aboutYouDetails () {
        /* Provjera godine --> ili staviti u dropdown */
        /* TODO: Trebat ce dolaziti obavijesti ako user ne unese ispravnu godinu, kasnije cijenu stana itd*/

        userYear = year.getValue();

        String faculty = dropdown.getSelectedItem().toString().trim();

        String[] whereArgs = new String[1];
        whereArgs[0] = faculty;
        listFakultet.clear();
        listFakultet.addAll(databaseHelper.queryFakultet("naziv = ?", whereArgs, null, null, null));
        int idFaculty = listFakultet.get(0).getId_fakultet();

        char userGender = 'M';
        if(genderF.isChecked()){
            userGender = 'Z';
        }

        boolean userSmoker = false;
        if(smoker.isChecked()){
            userSmoker = true;
        }

        boolean noPartyLifestyle = true;
        if(party.isChecked()){
            noPartyLifestyle = false;
        }

        boolean userHasPet = false;
        if(pet.isChecked()){
            userHasPet = true;
        }

        // postavljanje svih ljubimaca na false
        boolean[] pets = new boolean[6];
        Arrays.fill(pets, false);

        if(userHasPet){
            if(dog.isChecked()){
                pets[0] = true;
            }
            if(cat.isChecked()){
                pets[1] = true;
            }
            if(parrot.isChecked()){
                pets[2] = true;
            }
            if(hamster.isChecked()){
                pets[3] = true;
            }
            if(rabbit.isChecked()){
                pets[4] = true;
            }
            if(snake.isChecked()){
                pets[5] = true;
            }

        }

        Intent i  = getIntent();
        userActive = (Korisnik) i.getSerializableExtra("InhUser");

        userActive.setId_fakultet(idFaculty);
        userActive.setSpol(userGender);
        userActive.setMiran_zivot(noPartyLifestyle);
        userActive.setPusac(userSmoker);
        userActive.setLjubimac(userHasPet);
        userActive.setGodina_rodenja(userYear);

        // Prosljedi dalje
        Intent i2 = new Intent(this, FindApartmentRoommate.class);
        i2.putExtra("InhUser", userActive);
        i2.putExtra("Pets", pets);
        startActivity(i2);
        finish();
        return;
    }


    /*Hide/show checkboxes*/
    public void chooseAnimal (View view){
        androidx.appcompat.widget.SwitchCompat animal = (androidx.appcompat.widget.SwitchCompat) view;
        if (animal.isChecked()) {
            findViewById(R.id.dog).setVisibility(View.VISIBLE);
            findViewById(R.id.cat).setVisibility(View.VISIBLE);
            findViewById(R.id.rabbit).setVisibility(View.VISIBLE);
            findViewById(R.id.hamster).setVisibility(View.VISIBLE);
            findViewById(R.id.parrot).setVisibility(View.VISIBLE);
            findViewById(R.id.snake).setVisibility(View.VISIBLE);
            findViewById(R.id.other).setVisibility(View.VISIBLE);
        } else{
            findViewById(R.id.dog).setVisibility(View.GONE);
            findViewById(R.id.cat).setVisibility(View.GONE);
            findViewById(R.id.rabbit).setVisibility(View.GONE);
            findViewById(R.id.hamster).setVisibility(View.GONE);
            findViewById(R.id.parrot).setVisibility(View.GONE);
            findViewById(R.id.snake).setVisibility(View.GONE);
            findViewById(R.id.other).setVisibility(View.GONE);
        }
    }

    public void onRadioButtonClicked (View v){
        RadioButton btn = (RadioButton) v;
    }

}

