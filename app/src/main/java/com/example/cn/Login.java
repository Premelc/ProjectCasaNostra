package com.example.cn;

import com.example.cn.helpers.InputValidation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.cn.helpers.InputValidation;
import com.example.cn.helpers.SaveSharedPreference;
import com.example.cn.model.Korisnik;
import com.example.cn.sql.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = Login.this;
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextUsername;
    private TextInputEditText textInputEditTextPassword;
    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;
    private AppCompatTextView adminLink;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private Korisnik userActive;

    //private ConstraintLayout constraint;

    public static final int LENGTH_LONG = 2;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();

        auth = FirebaseAuth.getInstance();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /*Redirect on home page after successful login */
    public void homePage(View v){
        Intent hPage = new Intent(this, HomePage.class);
        startActivity(hPage);
    }



    /**
     * This method is to initialize views
     */

    private void initViews() {
        textInputLayoutUsername = (TextInputLayout) findViewById(R.id.textInputLayoutUsername);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextUsername = (TextInputEditText) findViewById(R.id.textInputEditTextUsername);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
        adminLink = (AppCompatTextView) findViewById(R.id.adminLink);
    }
    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
        adminLink.setOnClickListener(this);
    }
    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adminLink:
                if(verifyAdmin()){
                    Intent aPage = new Intent(this, AdminPage.class);
                    startActivity(aPage);
                }
                break;
            case R.id.appCompatButtonLogin:
                if(verifyFromSQLite()){
                    SaveSharedPreference.setSessionUser(this, userActive);
                    Intent i2 = new Intent(this, HomePage.class);
                    startActivity(i2);
                    finish();
                }
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), Register.class);
                startActivity(intentRegister);
                finish();
                break;
        }
    }
    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private boolean verifyFromSQLite() {
        boolean check = true;
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUsername, textInputLayoutUsername, "Unesite korisničko ime")) {
            check = false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, "Unesite zaporku")) {
            check = false;
        }
        if(!check){
            return false;
        }
        if (databaseHelper.checkUser(textInputEditTextUsername.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {
            /*Intent accountsIntent = new Intent(activity, UsersListActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);*/

            // dohvacanje korisnika da bi se proslijedio na home page
            List<Korisnik> userList = new ArrayList<Korisnik>();
            String whereClause = "username = ?"; // where upit
            String[] whereArgs = new String[1];
            whereArgs[0] = textInputEditTextUsername.getText().toString().trim(); // zamjenjuje ?
            userList.addAll(databaseHelper.queryKorisnik(whereClause, whereArgs, null, null, null));
            userActive = userList.get(0);

            String email = userActive.getEmail();
            String password = userActive.getPassword();

            auth.signInWithEmailAndPassword(email, password);


            return true;
        } else {
            // Snack Bar to show success message that record is wrong
            View view = findViewById(R.id.main_layout_id);

            /*Snackbar je poruka koja se prikazuje na dnu ekrana i pokaze se nabrzinu kao uspjesna prijava! ili privi email!*/
            Snackbar.make(view, "Pogrešno korisničko ime ili lozinka", Snackbar.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean verifyAdmin(){
        boolean check = true;
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUsername, textInputLayoutUsername, "Unesite korisničko ime")) {
            check = false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, "Unesite zaporku")) {
            check = false;
        }
        if(!check){
            return false;
        }

        String username = textInputEditTextUsername.getText().toString().trim();
        String password = textInputEditTextPassword.getText().toString().trim();

        if((username.equals("admin")) && (password.equals("1234"))){
            return true;
        } else{
            // Snack Bar to show success message that record is wrong
            View view = findViewById(R.id.main_layout_id);

            /*Snackbar je poruka koja se prikazuje na dnu ekrana i pokaze se nabrzinu kao uspjesna prijava! ili privi email!*/
            Snackbar.make(view, "Pogrešno korisničko ime ili lozinka", Snackbar.LENGTH_LONG).show();

            return false;
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextUsername.setText(null);
        textInputEditTextPassword.setText(null);
    }




}