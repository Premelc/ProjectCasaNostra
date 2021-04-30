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
import android.widget.Spinner;

import com.example.cn.model.Kvart;
import com.example.cn.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OnlyRoommate extends AppCompatActivity {
    private AppCompatActivity activity = OnlyRoommate.this;
    private DatabaseHelper databaseHelper;
    private List<Kvart> listKvart;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_roommate);

        listKvart = new ArrayList<Kvart>();
        databaseHelper = new DatabaseHelper(activity);

        Spinner dropdown = findViewById(R.id.faculty);

        listKvart.clear();
        listKvart.addAll(databaseHelper.queryKvart(null, null, null, null, "naziv ASC"));

        String[] items;

        items = listKvart.stream().map(Kvart::getNaziv).toArray(String[]::new);

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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void toMainPageRoommate(View v){
        Intent main = new Intent(this, HomePage.class);
        startActivity(main);
    }
}