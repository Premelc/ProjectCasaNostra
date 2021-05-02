package com.example.cn;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.cn.model.Kvart;
import com.example.cn.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OnlyRoommate extends AppCompatActivity implements View.OnClickListener{
    private AppCompatActivity activity = OnlyRoommate.this;
    private DatabaseHelper databaseHelper;
    private List<Kvart> listKvart;

    private Spinner priceTo;
    private RadioButton room, shRoom;
    private RadioButton femaleGender, maleGender, maleFemale;
    private EditText yearFrom, yearTo;
    private RadioButton roommateSmoker, roommateNonSmoker;
    private RadioButton roommatePet, roommateNoPet;
    private AppCompatButton button;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_roommate);

        initViews();
        initListeners();

        listKvart = new ArrayList<Kvart>();
        databaseHelper = new DatabaseHelper(activity);

        Spinner dropdown = findViewById(R.id.location);
        listKvart.clear();
        listKvart.addAll(databaseHelper.queryKvart(null, null, null, null, "naziv ASC"));

        String[] items;
        items = listKvart.stream().map(Kvart::getNaziv).toArray(String[]::new);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        /*Za cijenu do*/
        Spinner dropdown3 = findViewById(R.id.to);
        String[] items3 = new String[]{"1000", "1500", "2000", "2500", "3000", "Cijena mi nije bitna"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items3);
        dropdown3.setAdapter(adapter3);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        priceTo = findViewById(R.id.to);

        room = findViewById(R.id.soloRoom);
        shRoom = findViewById(R.id.sharedRoom);

        femaleGender = findViewById(R.id.female);
        maleGender = findViewById(R.id.male);
        maleFemale = findViewById(R.id.femaleMale);

        yearFrom = findViewById(R.id.year1);
        yearTo = findViewById(R.id.year2);

        roommateSmoker = findViewById(R.id.smoke);
        roommateNonSmoker = findViewById(R.id.noSmoke);

        roommatePet = findViewById(R.id.pet);
        roommateNoPet = findViewById(R.id.noPet);

        button = findViewById(R.id.onlyRoommateButton);
    }

    private void initListeners() {
        button.setOnClickListener(this);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    /*Kada se pritisne button dalje
    * 1. poziva se funkcija koja upisuje viewse u tmp varijable pa u klasu
    * 2. pokrece se nova aktivnost*/

    public void onClick(View v) {
        inputOnlyRoommateData();
        /*Intent main = new Intent(this, HomePage.class);
        startActivity(main);*/
    }

    public void inputOnlyRoommateData(){
        int priceIntTo = Integer.parseInt(priceTo.getSelectedItem().toString().trim());

        int yearOfRoommateFrom = Integer.parseInt(yearFrom.getText().toString().trim());
        int yearOfRoommateTo = Integer.parseInt(yearTo.getText().toString().trim());

        char gender = 'M';
        if(femaleGender.isChecked()){
            gender = 'Z';
        }
        if(maleFemale.isChecked()){
           gender = 'S';
           // kao svejedno
        }

        boolean roomSolo = true;
        if(shRoom.isChecked()){
            roomSolo = false;
        }

        boolean smoker = false;
        if(roommateSmoker.isChecked()){
            smoker = true;
        }

        boolean pet = false;
        if(roommatePet.isChecked()){
            pet = true;
        }

        /*Zapis u objekt*/
        // 1. uzmi objekt
        Intent i  = getIntent();
        activeUser userActive = (activeUser)i.getSerializableExtra("InhUser");

        userActive.setCimer_godine_od(yearOfRoommateFrom);
        userActive.setCimer_godine_do(yearOfRoommateTo);
        userActive.setCimer_ljubimac(pet);
        userActive.setCimer_spol(gender);
        userActive.setCimer_pusac(smoker);

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