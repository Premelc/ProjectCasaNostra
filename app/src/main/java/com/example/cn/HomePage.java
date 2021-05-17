package com.example.cn;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.cn.helpers.SaveSharedPreference;
import com.example.cn.model.Korisnik;
import com.example.cn.model.Swipe;
import com.example.cn.sql.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static java.util.Objects.isNull;

public class HomePage extends AppCompatActivity {
    TextView textView;
    Korisnik sessionUser = new Korisnik();
    DatabaseHelper databaseHelper;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_page);

        databaseHelper = new DatabaseHelper(this);

        if(SaveSharedPreference.getSessionUser(HomePage.this) != null &&
                SaveSharedPreference.getSessionUser(HomePage.this).getId_korisnik() > 0){
            sessionUser = SaveSharedPreference.getSessionUser(HomePage.this);

        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        bottomNav.setSelectedItemId(R.id.nav_swipe);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SwipeFragment().newInstance(sessionUser))
                .commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_profile:
                            selectedFragment = new MyProfileFragment().newInstance(sessionUser);
                            break;
                        case R.id.nav_swipe:
                            selectedFragment = new SwipeFragment().newInstance(sessionUser);
                            break;
                        case R.id.nav_chat:
                            selectedFragment = new ChatFragment().newInstance(sessionUser);
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}