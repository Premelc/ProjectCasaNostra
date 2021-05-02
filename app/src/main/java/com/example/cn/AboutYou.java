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
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.cn.model.Fakultet;
import com.example.cn.sql.DatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AboutYou extends AppCompatActivity implements View.OnClickListener {
    private AppCompatActivity activity = AboutYou.this;
    private DatabaseHelper databaseHelper;
    private List<Fakultet> listFakultet;

    /*Varijable iz kojih je potrebno citati odgovore*/
    private EditText year;
    private RadioButton genderF, genderM;
    private RadioButton smoker,nonSmoker;
    private Spinner faculty;
    private RadioButton party, noParty;
    private RadioButton pet, noPet;
    private CheckBox dog, cat, parrot, hamster, rabbit, other;
    private Spinner dropdown;
    private AppCompatButton appCompatButtonAboutYou;
    activeUser userActive = new activeUser();

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

        genderF = findViewById(R.id.female);
        genderM = findViewById(R.id.male);

        smoker = findViewById(R.id.smoker);
        nonSmoker = findViewById(R.id.nonSmoker);

        dropdown = findViewById(R.id.faculty);

        party = findViewById(R.id.party);
        noParty = findViewById(R.id.noParty);

        pet = findViewById(R.id.pet);
        noPet = findViewById(R.id.petNo);

        dog = findViewById(R.id.dog);
        cat = findViewById(R.id.cat);
        rabbit = findViewById(R.id.rabbit);
        hamster = findViewById(R.id.hamster);
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
        int userYear = Integer.parseInt(year.getText().toString().trim());

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

        boolean userSmoker = true;
        if(nonSmoker.isChecked()){
            userSmoker = false;
        }

        boolean noPartyLifestyle = true;
        if(party.isChecked()){
            noPartyLifestyle = false;
        }

        boolean userHasPet = true;
        if(noPet.isChecked()){
            userHasPet = false;
        }

        /*String userPet = new String();
        if(userHasPet){
            if(dog.isChecked()){
                userPet = "Pas";
            }
            if(cat.isChecked()){
                userPet = "Mačka";
            }
            if(parrot.isChecked()){
                userPet = "Papiga";
            }
            if(hamster.isChecked()){
                userPet = "Hrčak";
            }
            if(other.isChecked()){
                userPet = "Ostalo";
            }
        }*/

        Intent i  = getIntent();
        userActive = (activeUser)i.getSerializableExtra("InhUser");

        //userActive.setId_fakultet(idFaculty);
        userActive.setSpol(userGender);
        userActive.setMiran_zivot(noPartyLifestyle);
        userActive.setPusac(userSmoker);
        userActive.setLjubimac(userHasPet);
        userActive.setGodina_rodenja(userYear);

        // Prosljedi dalje
        Intent i2 = new Intent(this, FindApartmentRoommate.class);
        i2.putExtra("InhUser2", userActive);
        startActivity(i2);

        return;
    }


    /*Hide/show checkboxes*/
    public void chooseAnimal (View view){
        RadioButton animal = (RadioButton) view;
        if (animal.isChecked()) {
            findViewById(R.id.dog).setVisibility(View.VISIBLE);
            findViewById(R.id.cat).setVisibility(View.VISIBLE);
            findViewById(R.id.rabbit).setVisibility(View.VISIBLE);
            findViewById(R.id.hamster).setVisibility(View.VISIBLE);
            findViewById(R.id.parrot).setVisibility(View.VISIBLE);
            findViewById(R.id.other).setVisibility(View.VISIBLE);

        }
    }
    public void noAnimal (View v){
        RadioButton animal = (RadioButton) v;
        if (animal.isChecked()) {
            findViewById(R.id.dog).setVisibility(View.GONE);
            findViewById(R.id.cat).setVisibility(View.GONE);
            findViewById(R.id.rabbit).setVisibility(View.GONE);
            findViewById(R.id.hamster).setVisibility(View.GONE);
            findViewById(R.id.parrot).setVisibility(View.GONE);
            findViewById(R.id.other).setVisibility(View.GONE);
        }
    }

    public void onRadioButtonClicked (View v){
        RadioButton btn = (RadioButton) v;
    }

}

