package com.example.cn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class RvAdaptor extends RecyclerView.Adapter<RvAdaptor.MyViewHolder> {
    String s1[],s2[],s3[],s4[];
    int images[];
    Context c;

    public  RvAdaptor(Context ct, String s1[], String s2[], String s3[], String s4[],int images[]){

        c = ct;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
        this.images = images;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(c);
        View v = inf.inflate(R.layout.myrow,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RvAdaptor.MyViewHolder holder, int position) {
        holder.t1.setText(s1[position]);
        holder.t2.setText(s2[position]);
        holder.t3.setText(s3[position]);
        holder.t4.setText(s4[position]);
        //TODO:ovdje sliku jos doadat koristiti holder.myImage.setImageResource


    }

    @Override
    public int getItemCount() {
        return s1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView t1,t2,t3,t4;
        ImageView img1;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.pktxt);
            t2 = itemView.findViewById(R.id.pktxt2);
            t3 = itemView.findViewById(R.id.pktxt3);
            t4 = itemView.findViewById(R.id.pktxt4);
            // img1 = itemView.findViewById(R.id.pkimg);
        }
    }
}
