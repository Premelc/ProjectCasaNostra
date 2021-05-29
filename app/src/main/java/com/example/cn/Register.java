package com.example.cn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cn.helpers.InputValidation;
import com.example.cn.model.Korisnik;
import com.example.cn.sql.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class Register extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = Register.this;
    private ConstraintLayout nestedScrollView;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextUsername;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    private ConstraintLayout constraint;
    Korisnik userActive = new Korisnik();

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();

        auth = FirebaseAuth.getInstance();

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        //constraint = (ConstraintLayout) findViewById(R.id.nestedScrollView);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutUsername = (TextInputLayout) findViewById(R.id.textInputLayoutUsername);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextUsername = (TextInputEditText) findViewById(R.id.textInputEditTextUsername);
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
                    Intent i = new Intent(this, AboutYou.class);
                    i.putExtra("InhUser", userActive);
                    startActivity(i);
                    finish();
                }

                break;
            case R.id.appCompatTextViewLoginLink:
                Intent login = new Intent(this, Login.class);
                startActivity(login);
                finish();
                break;
        }
    }
    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private boolean postDataToSQLite() {
        /*Provjere unosa*/
        boolean check = true;
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            check = false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUsername, textInputLayoutUsername, "Unesite korisničko ime")) {
            check = false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            check = false;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            check = false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            check = false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextConfirmPassword, textInputLayoutConfirmPassword, getString(R.string.error_message_password))) {
            check = false;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            check = false;
        }
        if(!check){
            return false;
        }
        if (!databaseHelper.checkUserEmail(textInputEditTextEmail.getText().toString().trim())) {

            if(databaseHelper.checkUserUsername(textInputEditTextUsername.getText().toString().trim())){
                View view = findViewById(R.id.main_layout_id);
                Snackbar.make(view, "Korisničko ime već postoji", Snackbar.LENGTH_LONG).show();
                return false;
            }

            String username = textInputEditTextUsername.getText().toString().trim();
            String name = textInputEditTextName.getText().toString().trim();
            String email = textInputEditTextEmail.getText().toString().trim();
            String password = textInputEditTextPassword.getText().toString().trim();

            /*Tmp varijable zapisuju se u klasu active user*/

            userActive.setUsername(username);
            userActive.setIme(name);
            userActive.setEmail(email);
            userActive.setPassword(password);

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
        textInputEditTextUsername.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }


}