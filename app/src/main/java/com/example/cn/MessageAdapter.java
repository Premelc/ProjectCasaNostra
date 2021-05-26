package com.example.cn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cn.R;
import com.example.cn.model.Fakultet;
import com.example.cn.model.Korisnik;
import com.example.cn.model.Kvart;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Viewholder> {

    private Context context;
    private Korisnik chatUser;

    // Constructor
    public MessageAdapter(Context context, Korisnik chatUser) {
        this.context = context;
        this.chatUser = chatUser;
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
        // Kvart model = messages.get(position);

        holder.othersMessage.setText("" + chatUser.getIme());
        holder.othersLayout.setVisibility(View.VISIBLE);
        holder.myMessage.setVisibility(View.GONE);

        /*if((position % 2) == 0){
            holder.othersMessage.setText("" + model.getNaziv());
            holder.othersLayout.setVisibility(View.VISIBLE);
            holder.myMessage.setVisibility(View.GONE);
        } else {
            holder.myMessage.setText("" + model.getNaziv());
            holder.myMessage.setVisibility(View.VISIBLE);
            holder.othersLayout.setVisibility(View.GONE);
        }*/

        //holder.courseIV.setImageResource(model.getCourse_image());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.

        //return messages.size();
        return 1;
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
