package com.example.cn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
    }

    public void loginRedirect(View v){
        Intent login = new Intent(this, Login.class);
        startActivity(login);
    }

    public void registerRedirect(View v){
        Intent register = new Intent(this, Register.class);
        startActivity(register);
    }

}