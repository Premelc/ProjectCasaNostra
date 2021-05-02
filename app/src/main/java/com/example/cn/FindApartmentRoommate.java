package com.example.cn;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import java.io.Serializable;

public class FindApartmentRoommate extends AboutYou {
    private AppCompatActivity activity = FindApartmentRoommate.this;
    private RadioButton roommateAndApartment, onlyRoommate;

    activeUser userActive = new activeUser();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_apartment_roommate);

        initViews();
        initObjects();

        /*Intent i  = getIntent();
        korisnik = (activeUser)i.getSerializableExtra("InhUser2");*/

    }

    private void initViews() {
        roommateAndApartment = findViewById(R.id.roommateApartment);
        onlyRoommate = findViewById(R.id.roommate);
    }


    private void initObjects() {
        /*Na ovaj nacin se nasljeduje objekt instanciran od prethodnog activityja
         * Valjda https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android */
        /*Intent i  = getIntent();
        korisnik = (activeUser)i.getSerializableExtra("InhUser");*/
    }

    public void onlyRoommate(View v){
        RadioButton roommate = (RadioButton)v;

        if(roommate.isChecked()){
            // Zapis odgovora u klasu
            Intent i  = getIntent();
            activeUser userActive = (activeUser)i.getSerializableExtra("InhUser2");

            userActive.setTrazimStan(false);

            Intent i2 = new Intent(this, OnlyRoommate.class);
            i2.putExtra("InhUser3", userActive);
            startActivity(i2);
        }
    }

    public void roommateApartment(View v){
        RadioButton roommateApartment = (RadioButton)v;
        if(roommateApartment.isChecked()){

            Intent i  = getIntent();
            activeUser userActive = (activeUser)i.getSerializableExtra("InhUser2");
            userActive.setTrazimStan(true);

            Intent i2 = new Intent(this, ApartmentAndRoommate.class);
            i2.putExtra("InhUser3", userActive);
            startActivity(i2);

        }
    }

}