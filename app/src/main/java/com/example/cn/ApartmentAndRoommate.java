package com.example.cn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class ApartmentAndRoommate extends AppCompatActivity implements View.OnClickListener{
    private AppCompatActivity activity = ApartmentAndRoommate.this;

    private Spinner priceTo;
    private CheckBox west1, east1, center1, suburbs1, other1;
    private RadioButton femaleGender, maleGender, maleFemale;
    private EditText yearFrom, yearTo;
    private RadioButton roommateSmoker, roommateNonSmoker;
    private RadioButton roommatePet, roommateNoPet;
    private AppCompatButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_and_roommate);

        initViews();
        initListeners();

        Spinner dropdown = findViewById(R.id.to2);
        String[] items = new String[]{"1000", "1500", "2000", "2500", "3000", "Cijena mi nije bitna"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

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
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void initViews(){
        priceTo = findViewById(R.id.to2);

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
        int priceIntTo = Integer.parseInt(priceTo.getSelectedItem().toString().trim());

        int yearOfRoommateFrom = Integer.parseInt(yearFrom.getText().toString().trim());
        int yearOfRoommateTo = Integer.parseInt(yearTo.getText().toString().trim());

        /*Angel ne znam kako ti se upisuje lokacija u bazu po id-u ili imenu ja cu ti staviti u boolean
        kad korisnik oznaci da postane true pa ti vidi
         */
        if(west1.isChecked()){
            boolean west = true;
        }
        if(east1.isChecked()){
            boolean east = true;
        }
        if(center1.isChecked()){
           boolean center = true;
        }
        if(suburbs1.isChecked()){
            boolean suburbs = true;
        }
        if(other1.isChecked()){
            boolean other = true;
        }

        char gender = 'M';
        if(femaleGender.isChecked()){
            gender = 'Z';
        }
        if(maleFemale.isChecked()){
            gender = 'S';
            // kao svejedno
        }

        boolean smoker = false;
        if(roommateSmoker.isChecked()){
            smoker = true;
        }

        boolean pet = false;
        if(roommatePet.isChecked()){
            pet = true;
        }

        // Preuzmi objekt
        Intent i  = getIntent();
        activeUser userActive = (activeUser)i.getSerializableExtra("InhUser");

        // Zapis u objekt
        userActive.setCimer_pusac(smoker);
        userActive.setCimer_spol(gender);
        userActive.setCimer_godine_od(yearOfRoommateFrom);
        userActive.setCimer_godine_do(yearOfRoommateTo);
        userActive.setCimer_ljubimac(pet);

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