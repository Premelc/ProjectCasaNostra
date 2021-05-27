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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.example.cn.MessageAdapter;
import com.example.cn.model.Chat;
import com.example.cn.model.Fakultet;
import com.example.cn.model.Korisnik;
import com.example.cn.model.Kvart;
import com.example.cn.sql.DatabaseHelper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    FirebaseUser fuser;
    DatabaseReference reference;

    private ImageButton sendButton;
    private EditText textMessage;

    ArrayList<Chat> chatList;


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

        chatList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://com-example-cn-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Chats");

            MessageAdapter messageAdapter = new MessageAdapter(getActivity(), sessionUser, chatUser, chatList);
            // below line is for setting a layout manager for our recycler view.
            // here we are creating vertical list so we will provide orientation as vertical
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

            // in below two lines we are setting layoutmanager and adapter to our recycler view.
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(messageAdapter);

            recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);





        initViews();
        initListeners();

        String sessionId = Integer.toString(sessionUser.getId_korisnik());
        String chatUserId = Integer.toString(chatUser.getId_korisnik());
        readMessage(sessionId,chatUserId);
    }

    public void initViews(){
        sendButton = getView().findViewById(R.id.sendButton);
        textMessage = getView().findViewById(R.id.textMessage);
    }

    public void initListeners(){
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textMessage.getText().toString();
                if (!msg.equals("")){
                    String sendUser = Integer.toString(sessionUser.getId_korisnik());
                    String recUser = Integer.toString(chatUser.getId_korisnik());
                    sendMessage(sendUser, recUser, msg);
                }
                textMessage.setText("");
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://com-example-cn-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessage(String myid, String userId){
        String sessionId = Integer.toString(sessionUser.getId_korisnik());
        String chatUserId = Integer.toString(chatUser.getId_korisnik());
        chatList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance("https://com-example-cn-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(sessionId) && chat.getSender().equals(chatUserId) || chat.getReceiver().equals(chatUserId) && chat.getSender().equals(sessionId)){
                        chatList.add(chat);
                    }
                    if(!chatList.isEmpty()){
                        MessageAdapter messageAdapter = new MessageAdapter(getActivity(), sessionUser, chatUser, chatList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                        // in below two lines we are setting layoutmanager and adapter to our recycler view.
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(messageAdapter);

                        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }
}