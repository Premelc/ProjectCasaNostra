package com.example.cn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cn.model.Admin;
import com.example.cn.model.Korisnik;
import com.example.cn.activeUser;

import java.util.ArrayList;
import java.util.List;

import com.example.cn.sql.DatabaseHelper;

public class AdminPage extends AppCompatActivity {

    private AppCompatActivity activity = AdminPage.this;
    private DatabaseHelper databaseHelper;

    activeUser user;
    String username;
    Admin admin = new Admin();//DODANO
    EditText nameinput;
    Button but1;
    Button but2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        nameinput = (EditText) findViewById(R.id.user);
        but1 = (Button) findViewById(R.id.delete);
        but2 = (Button) findViewById(R.id.dajadmina);

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = nameinput.getText().toString();


                databaseHelper = new DatabaseHelper(activity);

                String whereClause = "username = ?"; // where upit
                String[] whereArgs = new String[1];
                whereArgs[0] = username; // zamjenjuje ?
                user = databaseHelper.queryActiveUser(whereClause, whereArgs, null, null, null); // dohvacanje korisnika po username-u
                if(user.getId_korisnik() > 0){
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
                            })
                    ;

                    AlertDialog alert = builder.create();

                    alert.setTitle("GREŠKA");
                    alert.show();

                }



            }
        });

        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = nameinput.getText().toString();



                //prvo izbrise iz tablice korisnik i dodaje u tablicu admin
                //pozvat ono insertadmina ?
                //DODANO

                databaseHelper = new DatabaseHelper(activity);

                String whereClause = "username = ?"; // where upit
                String[] whereArgs = new String[1];
                whereArgs[0] = username; // zamjenjuje ?
                user = databaseHelper.queryActiveUser(whereClause, whereArgs, null, null, null); // dohvacanje korisnika po username-u
                if(user.getId_korisnik() > 0){
                    String str1 = user.getUsername();
                    String pas1 = user.getPassword();
                    String email1 = user.getEmail();

                    databaseHelper.deleteKorisnik(user); // brisanje korisnika iz table korisnik


                    //dodavanje njegovo u tablu admin

                    //TODO probat nac sigurnije rjesenje za postavljanje lozinke bez da se koriste geteri za lozinku
                    admin.setUsername(str1);
                    admin.setPassword(pas1);
                    admin.setEmail(email1);

                    databaseHelper.insertAdmin(admin);
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminPage.this);
                    builder.setMessage("Korisnik je sada admin") .setTitle("Greška");


                    builder.setMessage("Korisnik je sada admin")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                    ;

                    AlertDialog alert = builder.create();

                    alert.setTitle("GREŠKA");
                    alert.show();

                } else{
                    //TODO kopi pejstat alert od gore

                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminPage.this);
                    builder.setMessage("Navedeni korisnik ne postoji") .setTitle("Greška");


                    builder.setMessage("Navedeni korisnik ne postoji")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                    ;

                    AlertDialog alert = builder.create();

                    alert.setTitle("GREŠKA");
                    alert.show();
                }
            }
        });
    }
}