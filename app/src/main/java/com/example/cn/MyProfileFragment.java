package com.example.cn;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cn.helpers.SaveSharedPreference;
import com.example.cn.model.Korisnik;
import com.example.cn.sql.DatabaseHelper;

public class MyProfileFragment extends Fragment {

    // Objekti
    DatabaseHelper databaseHelper;
    Korisnik sessionUser;

    // Widgeti
    private Button logout;
    private TextView deleteAccount;

    public static MyProfileFragment newInstance(Korisnik user){
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Session", user);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sessionUser = (Korisnik) getArguments().getSerializable("Session");

        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(getActivity());

        initViews();
        initListeners();
    }

    private void initViews(){
        logout = (Button) getView().findViewById(R.id.logout);
        deleteAccount = (TextView) getView().findViewById(R.id.deleteAccount);
    }

    private void initListeners() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prije odjave postavi null objekt kao session user
                Korisnik user = new Korisnik();
                SaveSharedPreference.setSessionUser(getActivity(), user);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteUser();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }

                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Jeste li sigurni da želite izbrisati svoj korisnički račun?").setPositiveButton("Da", dialogClickListener)
                        .setNegativeButton("Ne", dialogClickListener).show();
            }
        });
    }

    public void deleteUser(){
        databaseHelper.deleteKorisnik(sessionUser); // brisanje korisnika

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Uspješno ste izbrisali svoj korisnički račun.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Korisnik user = new Korisnik();
                        SaveSharedPreference.setSessionUser(getActivity(), user);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
        ;

        AlertDialog alert = builder.create();

        alert.setTitle("USPJEH");
        alert.show();
    }

}