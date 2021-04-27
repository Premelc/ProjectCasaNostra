package com.example.cn;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.cn.model.Fakultet;
import com.example.cn.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AboutYou extends AppCompatActivity {
    private AppCompatActivity activity = AboutYou.this;
    private DatabaseHelper databaseHelper;
    private List<Fakultet> listFakultet;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_you);

        listFakultet = new ArrayList<Fakultet>();
        databaseHelper = new DatabaseHelper(activity);

        // Dropdown
        Spinner dropdown = findViewById(R.id.spinner3);
        //create a list of items for the spinner.

        listFakultet.clear();
        listFakultet.addAll(databaseHelper.queryFakultet(null, null, null, null, "naziv ASC"));

        String[] items;

        items = listFakultet.stream().map(Fakultet::getNaziv).toArray(String[]::new);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
    }
    /*Return arrows*/
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    /*Hide/show checkboxes*/
    public void chooseAnimal(View view) {
        RadioButton animal = (RadioButton) view;
        if(animal.isChecked()){
            findViewById(R.id.dog).setVisibility(View.VISIBLE);
            findViewById(R.id.cat).setVisibility(View.VISIBLE);
            findViewById(R.id.rabbit).setVisibility(View.VISIBLE);
            findViewById(R.id.hamster).setVisibility(View.VISIBLE);
            findViewById(R.id.parrot).setVisibility(View.VISIBLE);
            findViewById(R.id.other).setVisibility(View.VISIBLE);

        }
    }
    public void noAnimal(View v){
        RadioButton animal = (RadioButton) v;
        if(animal.isChecked()){
            findViewById(R.id.dog).setVisibility(View.GONE);
            findViewById(R.id.cat).setVisibility(View.GONE);
            findViewById(R.id.rabbit).setVisibility(View.GONE);
            findViewById(R.id.hamster).setVisibility(View.GONE);
            findViewById(R.id.parrot).setVisibility(View.GONE);
            findViewById(R.id.other).setVisibility(View.GONE);
        }
    }

    public void onRadioButtonClicked(View v){
        RadioButton btn = (RadioButton) v;
    }

    /*Next intent on button click*/
    public void findApartmentRoommate(View v){
        Intent roommate = new Intent(this, FindApartmentRoommate.class);
        startActivity(roommate);
    }

}