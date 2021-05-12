package com.example.cn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cn.model.Korisnik;

import java.util.ArrayList;
import java.util.List;

import com.example.cn.sql.DatabaseHelper;

public class AdminPage extends AppCompatActivity {

    private AppCompatActivity activity = AdminPage.this;
    private DatabaseHelper databaseHelper;

    Korisnik user;
    List<Korisnik> userList = new ArrayList<Korisnik>();
    String username;
    EditText nameinput;
    Button but1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        nameinput = (EditText) findViewById(R.id.user);
        but1 = (Button) findViewById(R.id.delete);

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = nameinput.getText().toString();


                databaseHelper = new DatabaseHelper(activity);

                String whereClause = "username = ?"; // where upit
                String[] whereArgs = new String[1];
                whereArgs[0] = username; // zamjenjuje ?
                userList.addAll(databaseHelper.queryKorisnik(whereClause, whereArgs, null, null, null)); // dohvacanje korisnika po username-u

                if(!userList.isEmpty()){
                    user = userList.get(0);
                    databaseHelper.deleteKorisnik(user); // brisanje korisnika

                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminPage.this);
                    builder.setMessage("Korisnik uspjesno izbrisan") .setTitle("");


                    builder.setMessage("Korisnik uspjesno izbrisan")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                    ;

                    AlertDialog alert = builder.create();

                    alert.setTitle("USPJEH");
                    alert.show();
                } else{
                    // dio koda koji ce se izvrsiti ukoliko ne postoji taj username
                    //DODANO
                    //Dodavanje alerta
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminPage.this);
                    builder.setMessage("Navedeni korisnik ne postoji") .setTitle("Greška");


                    builder.setMessage("Navedeni korisnik ne postoji")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                    AlertDialog alert = builder.create();

                    alert.setTitle("GREŠKA");
                    alert.show();

                }

            }
        });


    }
}