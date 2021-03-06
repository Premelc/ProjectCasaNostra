package com.example.cn;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cn.helpers.SaveSharedPreference;
import com.example.cn.model.Korisnik;
import com.example.cn.model.KorisnikLjubimac;
import com.example.cn.model.Kvart;
import com.example.cn.model.NudimStan;
import com.example.cn.sql.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class OnlyRoommate extends AppCompatActivity implements View.OnClickListener {
    private AppCompatActivity activity = OnlyRoommate.this;
    private DatabaseHelper databaseHelper;
    private List<Kvart> listKvart;

    Korisnik userActive = new Korisnik();
    NudimStan haveApt = new NudimStan();
    KorisnikLjubimac havePet = new KorisnikLjubimac();

    private EditText price;
    private Spinner locationDropdown;
    private RadioButton room, shRoom;
    private RadioButton femaleGender, maleGender, maleFemale;
    private NumberPicker yearFrom, yearTo;
    private RadioButton roommateSmoker, roommateNonSmoker;
    private RadioButton roommatePet, roommateNoPet;
    private Button button;
    private TextView error;
    private ScrollView scrollView;
    private com.google.android.material.slider.RangeSlider rangeSlider;
    private TextView yearRange;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
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

        auth = FirebaseAuth.getInstance();

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
        price = findViewById(R.id.price);
        locationDropdown = findViewById(R.id.location);

        room = findViewById(R.id.soloRoom);
        shRoom = findViewById(R.id.sharedRoom);

        femaleGender = findViewById(R.id.female);
        maleGender = findViewById(R.id.male);
        maleFemale = findViewById(R.id.femaleMale);


        roommateSmoker = findViewById(R.id.smoke);
        roommateNonSmoker = findViewById(R.id.noSmoke);

        roommatePet = findViewById(R.id.pet);
        roommateNoPet = findViewById(R.id.noPet);

        button = findViewById(R.id.onlyRoommateButton);

        error = findViewById(R.id.error);

        scrollView = findViewById(R.id.imageView1);

        rangeSlider = findViewById(R.id.rangeSlider);
        rangeSlider.setLabelBehavior(LabelFormatter.LABEL_GONE);
        yearRange = findViewById(R.id.yearRange);
    }

    private void initListeners() {
        button.setOnClickListener(this);

        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> values = slider.getValues();
                int yearFrom = Collections.min(values).intValue();
                int yearTo = Collections.max(values).intValue();
                yearRange.setText(yearFrom + " - " + yearTo);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    /*Kada se pritisne button dalje
    * 1. poziva se funkcija koja upisuje viewse u tmp varijable pa u klasu
    * 2. pokrece se nova aktivnost*/

    public void onClick(View v) {
        if(price.getText().toString().trim().length() == 0){
            error.setVisibility(View.VISIBLE);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        } else{
            error.setVisibility(View.GONE);
            inputOnlyRoommateData();
        }
    }

    public void inputOnlyRoommateData(){
        /*Zapis u objekt*/
        // 1. uzmi objekt
        Intent intent  = getIntent();
        userActive = (Korisnik)intent.getSerializableExtra("InhUser");
        boolean[] pets = intent.getBooleanArrayExtra("Pets");

        String location = locationDropdown.getSelectedItem().toString().trim();

        int priceIntTo = Integer.parseInt(price.getText().toString().trim());

        List<Float> values = rangeSlider.getValues();
        int yearOfRoommateFrom = 2021 - Collections.min(values).intValue();
        int yearOfRoommateTo = 2021 - Collections.max(values).intValue();


        char gender = 0;
        if(maleGender.isChecked()){
            gender = 'M';
        }
        else if(femaleGender.isChecked()){
            gender = 'Z';
        }
        else if(maleFemale.isChecked()){
            gender = 'S';
        }

        boolean roomSolo = true;
        if(shRoom.isChecked()){
            roomSolo = false; // dijeliti sobu
        }
        else if (room.isChecked()){
            roomSolo = true; // zasebna soba
        }

        boolean smoker = true;
        if(roommateSmoker.isChecked()){
            smoker = false; // ne zeli cimera pusaca
        }
        else if (roommateNonSmoker.isChecked()){
            smoker = true; // svejedno
        }

        boolean pet = true;
        if(roommatePet.isChecked()){
            pet = false; // ne zeli cimera s ljubimcem
        }
        else if(roommateNoPet.isChecked()){
            pet = true; // svejedno
        }

        // manji broj se zapisuje u varijablu cimer_godine_od
        if(yearOfRoommateFrom < yearOfRoommateTo){
            userActive.setCimer_godine_od(yearOfRoommateFrom);
            userActive.setCimer_godine_do(yearOfRoommateTo);
        } else{
            userActive.setCimer_godine_od(yearOfRoommateTo);
            userActive.setCimer_godine_do(yearOfRoommateFrom);
        }

        userActive.setCimer_ljubimac(pet);
        userActive.setCimer_pusac(smoker);
        userActive.setCimer_spol(gender);

        databaseHelper.insertKorisnika(userActive);

        // dohvacanje unesenog korisnika skupa sa automatski postavljenim id-om
        List<Korisnik> userList = new ArrayList<Korisnik>();
        String whereClause = "username = ?";
        String[] whereArgs = new String[1];
        whereArgs[0] = userActive.getUsername();
        userList.addAll(databaseHelper.queryKorisnik(whereClause, whereArgs, null, null, null));

        List<Kvart> locationList = new ArrayList<Kvart>();
        whereArgs[0] = location;
        locationList.clear();
        locationList.addAll(databaseHelper.queryKvart("naziv = ?", whereArgs, null, null, null));
        int idLocation = locationList.get(0).getId_kvart();

        if(!userList.isEmpty()){
            // unos podataka u tablicu NudimStan
            userActive = userList.get(0);
            haveApt.setId_korisnik(userActive.getId_korisnik());
            haveApt.setId_kvart(idLocation);
            haveApt.setZasebna_soba(roomSolo);
            haveApt.setCijena(priceIntTo);

            databaseHelper.insertNudimStan(haveApt);

            // unos podataka u tablicu KorisnikLjubimac
            for(int i = 0; i < pets.length; i++){
                if(pets[i]){
                    havePet.setId_ljubimac(i+1);
                    havePet.setId_korisnik(userActive.getId_korisnik());
                    databaseHelper.insertKorisnikLjubimac(havePet);
                }
            }

            // Firebase
            String email = userActive.getEmail();
            String password = userActive.getPassword();
            String username = userActive.getUsername();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                String userId = Integer.toString(userActive.getId_korisnik());

                                reference = FirebaseDatabase.getInstance("https://com-example-cn-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(userId);

                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("id", userId);
                                hashMap.put("username", username);
                                hashMap.put("password", password);

                                reference.setValue(hashMap);
                            }
                        }
                    });
        } else{
            // Dodati alert: Doslo je do greske
        }


        //2. prosljedi dalje
        SaveSharedPreference.setSessionUser(this, userActive);
        Intent i2 = new Intent(this, HomePage.class);
        startActivity(i2);
        finish();

        /* ANGEL ----> mozes ovdje sve upisati u bazu jer je ovo zadnja stranica
         * prije homepagea ili mozes u homepageu kako god ti je lakse
         * ako ovdje budes htjela samo makni ovo prosljedivanje i otkomentiraj
         * intent u metodi onClck*/


    }
}