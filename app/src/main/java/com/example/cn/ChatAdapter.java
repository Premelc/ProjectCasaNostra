package com.example.cn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cn.R;
import com.example.cn.helpers.GlideApp;
import com.example.cn.model.Chat;
import com.example.cn.model.Fakultet;
import com.example.cn.model.Korisnik;
import com.example.cn.model.Swipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Viewholder> {

    private FragmentActivity context;
    private ArrayList<Korisnik> chatUsers;
    private Korisnik sessionUser;
    private RecyclerViewClickListener listener;

    private StorageReference mStorageReference;
    private final String FOLDER_NAME = "volarevic";

    // Constructor
    public ChatAdapter(FragmentActivity context, ArrayList<Korisnik> chatUsers, Korisnik sessionUser, RecyclerViewClickListener listener) {
        this.context = context;
        this.chatUsers = chatUsers;
        this.sessionUser = sessionUser;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        Korisnik model = chatUsers.get(position);
        holder.name.setText(String.valueOf(model.getIme()));

       /*ArrayList<Chat> last = model.getMessage();

        if(last.isEmpty()){
            holder.message.setText("Nemate poruka");
        }else{
            holder.message.setText(last.toString());
        }*/

        int idOfUser = model.getId_korisnik();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic1");

        if(mStorageReference != null){
            Context cont = context.getApplicationContext();
            GlideApp.with(cont)
                    .load(mStorageReference)
                    .into(holder.profilePic);
        }else{
            holder.profilePic.setImageResource(R.drawable.ic_baseline_person_24);
        }




        //holder.courseIV.setImageResource(model.getCourse_image());
        //holder.profilePic.setImageResource();

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return chatUsers.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position, ArrayList<Korisnik> chatUsers);
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView profilePic;
        private TextView name, message;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition(), chatUsers);
        }
    }

}