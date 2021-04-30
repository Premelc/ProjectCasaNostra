package com.example.cn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.example.cn.helpers.InputValidation;
import com.example.cn.model.Korisnik;
import com.example.cn.sql.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;


public class Register extends AppCompatActivity implements View.OnClickListener, Serializable {

    private final AppCompatActivity activity = Register.this;
    private ConstraintLayout nestedScrollView;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    private ConstraintLayout constraint;
    ActiveUser userActive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        //constraint = (ConstraintLayout) findViewById(R.id.nestedScrollView);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);
        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }
    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        userActive = new ActiveUser();
    }
    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonRegister:
                if(postDataToSQLite()){
                    // Ako je postData true onda ide na sljedeci activity
                    Intent about = new Intent(this, AboutYou.class);
                    startActivity(about);
                }

                break;
            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }
    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private boolean postDataToSQLite() {
        /*Provjere unosa*/
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutConfirmPassword, getString(R.string.error_message_password))) {
            return false;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return false;
        }
        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            /*Novo: ubacivanje dio po dio u privremene varijable pa kasnije u bazu
            * Tmp varijable koje se kasnije salju u bazu*/
            String name = textInputEditTextName.getText().toString().trim();
            String email = textInputEditTextEmail.getText().toString().trim();
            String password = textInputEditTextPassword.getText().toString().trim();

            /*Tmp varijable zapisuju se u klasu active user*/
            userActive.setIme(name);
            userActive.setEmail(email);
            userActive.setPassword(password);

            /*Mozda ce se trebati prebaciti u grananje kod swicha di je intent za ic na novu str*/
            Intent i = new Intent(this, AboutYou.class);
            i.putExtra("InhUser", userActive);
            startActivity(i);

            /*
            Dio za bazu - kada se sve unese u privremene varijable onda se koristi
            user.setIme(textInputEditTextName.getText().toString().trim());
            userActive.setEmail(textInputEditTextEmail.getText().toString().trim());
            userActive.setPassword(textInputEditTextPassword.getText().toString().trim());
            databaseHelper.insertKorisnika(userActive);
            */

            // Snack Bar vam je ona poruka uz dno ekrana koja se pokaze pri uspjesnoj prijavi
            View view = findViewById(R.id.main_layout_id);
            Snackbar.make(view, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();

            // Funkcija koja sve obrise iz inputa
            emptyInputEditText();

            return true;
        } else {
            View view = findViewById(R.id.main_layout_id);
            Snackbar.make(view, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }


}