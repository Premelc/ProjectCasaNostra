package com.example.cn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cn.R;
import com.example.cn.model.Chat;
import com.example.cn.model.Fakultet;
import com.example.cn.model.Korisnik;
import com.example.cn.model.Kvart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Viewholder> {

    private Context context;
    private Korisnik chatUser;

    private ArrayList<Chat> chatList;
    Korisnik sessionUser;

    // Constructor
    public MessageAdapter(Context context, Korisnik sessionUser, Korisnik chatUser, ArrayList<Chat> chatList) {
        this.context = context;
        this.chatUser = chatUser;
        this.chatList = chatList;
        this.sessionUser = sessionUser;
    }

    @NonNull
    @Override
    public MessageAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        Chat model = chatList.get(position);

        String sessionId = Integer.toString(sessionUser.getId_korisnik());

        if(chatList.get(position).getSender().equals(sessionId)){
            holder.myMessage.setText("" + model.getMessage());
            holder.myMessage.setVisibility(View.VISIBLE);
            holder.othersLayout.setVisibility(View.GONE);
        } else {
            holder.othersMessage.setText("" + model.getMessage());
            holder.othersLayout.setVisibility(View.VISIBLE);
            holder.myMessage.setVisibility(View.GONE);
        }

        //holder.courseIV.setImageResource(model.getCourse_image());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.

        return chatList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView othersProfilePic;
        private TextView othersMessage, myMessage;
        private LinearLayout othersLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            othersProfilePic = itemView.findViewById(R.id.othersProfilePic);
            othersMessage = itemView.findViewById(R.id.othersMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            othersLayout = itemView.findViewById(R.id.othersLayout);
        }
    }
}
