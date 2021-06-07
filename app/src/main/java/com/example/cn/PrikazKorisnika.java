package com.example.cn;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cn.model.Korisnik;
import com.example.cn.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class PrikazKorisnika extends AppCompatActivity {
    RecyclerView rv;
    Button detaljiButton;
    String s1[] = {"","","","","","","","","",""};
    String s2[] = {"","","","","","","","","",""};
    String s3[] = {"","","","","","","","","",""};
    String s4[] = {"","","","","","","","","",""};
    int images[] = {1,2,3,4,5,6,7};
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);



    Korisnik user;
    List<Korisnik> userList = new ArrayList<Korisnik>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prikaz_korisnika);
        rv = findViewById(R.id.rv);
        //detaljiButton = (Button) findViewById(R.id.detalji);
        int p1 = 0,p2 = 0;
        Log.d("aa","ss");
        userList.clear();
        userList.addAll(databaseHelper.queryKorisnik(null, null, null, null, null)); // dohvacanje korisnika po username-u

        if(!userList.isEmpty()) {
            for (int i = 0; i < userList.size(); i++) {
                s1[i] = "Username:" + userList.get(i).getUsername();
                s2[i] = "Email:" + userList.get(i).getEmail();
                s3[i] = "Ime:" + userList.get(i).getIme();
                s4[i] = "ID:" + userList.get(i).getId_korisnik();
                Log.d("aa", s1[i]);
                final Korisnik a = userList.get(i);

            }
        }else{
            Log.d("abb", "dfsosdfji");
            s1[0] = "Nema korisnika";
            s2[0] = "Baza prazna";
        }



        RvAdaptor myAdaptor = new RvAdaptor(this,s1,s2,s3,s4,images);
        rv.setAdapter(myAdaptor);
        rv.setLayoutManager(new LinearLayoutManager(this));



    }
}