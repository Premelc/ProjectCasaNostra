package com.example.cn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cn.model.Korisnik;
import com.example.cn.model.Kvart;
import com.example.cn.sql.DatabaseHelper;

import java.util.ArrayList;

public class SwipeFragment extends Fragment {

    DatabaseHelper databaseHelper;
    Korisnik sessionUser;

    public static SwipeFragment newInstance(Korisnik user){
        SwipeFragment fragment = new SwipeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Session", user);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sessionUser = (Korisnik) getArguments().getSerializable("Session");

        return inflater.inflate(R.layout.fragment_swipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(getActivity());

        initViews();
        initListeners();
    }

    public void initViews(){

    }

    public void initListeners(){

    }
}