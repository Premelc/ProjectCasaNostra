package com.example.cn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.example.cn.MessageAdapter;
import com.example.cn.model.Fakultet;
import com.example.cn.model.Korisnik;
import com.example.cn.model.Kvart;
import com.example.cn.sql.DatabaseHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment {

    DatabaseHelper databaseHelper;
    Korisnik sessionUser;
    Korisnik chatUser;

    private RecyclerView recyclerView;

    public static MessagesFragment newInstance(Korisnik sessionUser, Korisnik chatUser){
        MessagesFragment fragment = new MessagesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Session", sessionUser);
        bundle.putSerializable("ChatUser", chatUser);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sessionUser = (Korisnik) getArguments().getSerializable("Session");
        chatUser = (Korisnik) getArguments().getSerializable("ChatUser");

        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setContentView(R.layout.fragment_chat);
        recyclerView = getView().findViewById(R.id.recyclerView);
        ScrollView scrollView = getView().findViewById(R.id.scrollView);


        databaseHelper = new DatabaseHelper(getActivity());

        MessageAdapter messageAdapter = new MessageAdapter(getActivity(), chatUser);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);

        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);

        initViews();
        initListeners();
    }

    public void initViews(){

    }

    public void initListeners(){

    }
}