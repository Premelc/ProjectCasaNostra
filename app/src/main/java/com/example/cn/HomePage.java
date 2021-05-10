package com.example.cn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.cn.model.Korisnik;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent  = getIntent();
        Korisnik userActive = (Korisnik) intent.getSerializableExtra("InhUser");
    }
}