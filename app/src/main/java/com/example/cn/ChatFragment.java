package com.example.cn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cn.ChatAdapter;
import com.example.cn.model.Chat;
import com.example.cn.model.Fakultet;
import com.example.cn.model.Korisnik;
import com.example.cn.model.Swipe;
import com.example.cn.sql.DatabaseHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatFragment extends Fragment {

    DatabaseHelper databaseHelper;
    Korisnik sessionUser;

    private RecyclerView recyclerView;
    private ArrayList<Swipe> swipeList = new ArrayList<>();
    private ArrayList<Korisnik> chatUsers = new ArrayList<>();
    private ChatAdapter.RecyclerViewClickListener listener;

    public static ChatFragment newInstance(Korisnik user){
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Session", user);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sessionUser = (Korisnik) getArguments().getSerializable("Session");

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setContentView(R.layout.fragment_chat);
        recyclerView = getView().findViewById(R.id.recyclerView);

        databaseHelper = new DatabaseHelper(getActivity());

        swipeList.addAll(databaseHelper.querySwipe(null, null, null, null, null));

        if(!swipeList.isEmpty()){
            for(Swipe swipe : swipeList){
                if(swipe != null && swipe.getId_1() > 0 && swipe.getId_2() > 0 && swipe.getId_1() == sessionUser.getId_korisnik() && swipe.isSwipe_1() != null && swipe.isSwipe_2() != null && swipe.isSwipe_1() == true && swipe.isSwipe_2() == true){
                    Korisnik user = databaseHelper.queryKorisnik("id_korisnik=?", new String[]{String.valueOf(swipe.getId_2())}, null, null, null).get(0);
                    chatUsers.add(user);
                }
                if(swipe != null && swipe.getId_1() > 0 && swipe.getId_2() > 0 && swipe.getId_2() == sessionUser.getId_korisnik() && swipe.isSwipe_1() != null && swipe.isSwipe_2() != null && swipe.isSwipe_1() == true && swipe.isSwipe_2() == true){
                    Korisnik user = databaseHelper.queryKorisnik("id_korisnik=?", new String[]{String.valueOf(swipe.getId_1())}, null, null, null).get(0);
                    chatUsers.add(user);
                }
            }
        }

        setOnClickListener();

        ChatAdapter chatAdapter = new ChatAdapter(getActivity(), chatUsers, sessionUser, listener);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);

        initViews();
        initListeners();
    }

    private void setOnClickListener() {
        listener = new ChatAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position, ArrayList<Korisnik> chatUsers) {
                Fragment selectedFragment = null;
                selectedFragment = new MessagesFragment().newInstance(sessionUser, chatUsers.get(position));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
            }
        };
    }

    public void initViews(){

    }

    public void initListeners(){


    }
}
