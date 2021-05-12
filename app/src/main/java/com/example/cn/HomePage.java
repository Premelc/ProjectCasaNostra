package com.example.cn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cn.model.Korisnik;

public class HomePage extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent  = getIntent();
        Korisnik userActive = (Korisnik) intent.getSerializableExtra("InhUser");

        textView = findViewById(R.id.textView2);
        textView.setText(userActive.toString());
    }
}