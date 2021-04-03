package com.example.cn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

public class FindApartmentRoommate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_apartment_roommate);

    }

    public void onlyRoommate(View v){
        RadioButton roommate = (RadioButton)v;
        if(roommate.isChecked()){
            Intent next = new Intent(this, OnlyRoommate.class);
            startActivity(next);
        }
    }

    public void roommateAndApartment(View v){
        RadioButton roommate = (RadioButton)v;
        if(roommate.isChecked()){
            Intent next = new Intent(this, OnlyRoommate.class);
            startActivity(next);
        }
    }


}