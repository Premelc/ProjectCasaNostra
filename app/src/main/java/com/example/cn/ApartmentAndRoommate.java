package com.example.cn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ApartmentAndRoommate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_and_roommate);

        Spinner dropdown = findViewById(R.id.price);
        String[] items = new String[]{"600 - 1000kn", "1000 - 1500kn", "1500 - 2000kn", "2000 - 2500kn", "2500 - 3000kn", "Cijena mi nije bitna"};
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

    public void toMainPage(View v){
        Intent main = new Intent(this, HomePage.class);
        startActivity(main);
    }
}