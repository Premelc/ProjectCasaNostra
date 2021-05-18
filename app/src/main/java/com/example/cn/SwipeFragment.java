package com.example.cn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cn.model.Korisnik;
import com.example.cn.sorting.SimilarityGradeSorting;
import com.example.cn.sorting.UsableOtherUser;
import com.example.cn.sql.DatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SwipeFragment extends Fragment {

    DatabaseHelper databaseHelper;
    Korisnik sessionUser;

    private ImageView imageView;

    // Za slike:
    private StorageReference mStorageReference;

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
        SimilarityGradeSorting sgs = new SimilarityGradeSorting();
        List<UsableOtherUser> usableOtherUser = sgs.Grade(sessionUser,databaseHelper);

        final ImageButton rightSwipe = (ImageButton) getView().findViewById(R.id.imageButton2);
        final ImageButton leftSwipe = (ImageButton) getView().findViewById(R.id.imageButton);

        display(usableOtherUser);
        setOnRightClick(rightSwipe,usableOtherUser);
        setOnLeftClick(leftSwipe , usableOtherUser);

        initViews();
        initListeners();
    }

    public void initViews(){
        imageView = (ImageView) getActivity().findViewById(R.id.pictureOfUser);
    }

    public void initListeners(){

    }

    public void setOnRightClick(ImageButton swp , List<UsableOtherUser> usableOtherUser){
        swp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(usableOtherUser.size() > 1){
                    //uklanja korisnika za popisa
                    //ANDELA dodaj ovdje insert u bazu za to da je aktivni korisnik swipeao lijevo
                    usableOtherUser.remove(usableOtherUser.get(0));
                    display(usableOtherUser);
                }
                else{
                    //Ovdje dodati view na koji se prebacimo kad vise nema korisnika za ponuditi
                    //kao neki placeholder sam stavio da cjelo vrijeme prikazuje zadnjeg korisnika
                    //ovo cemo izbrisati kad napravimo view za kad nema korisnika za ponuditi
                    display(usableOtherUser);
                }
            }
        });
    }

    public void setOnLeftClick(ImageButton swp , List<UsableOtherUser> usableOtherUser){
        swp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(usableOtherUser.size() > 1){
                    //uklanja korisnika za popisa
                    //ANDELA dodaj ovdje insert u bazu za to da je aktivni korisnik swipeao desno
                    usableOtherUser.remove(usableOtherUser.get(0));
                    display(usableOtherUser);
                }
                else{
                    //Ovdje dodati view na koji se prebacimo kad vise nema korisnika za ponuditi
                    //kao neki placeholder sam stavio da cjelo vrijeme prikazuje zadnjeg korisnika
                    //ovo cemo izbrisati kad napravimo view za kad nema korisnika za ponuditi
                    display(usableOtherUser);
                }
            }
        });
    }

    private void display(List<UsableOtherUser> usableOtherUser){
        if(!usableOtherUser.isEmpty()) {

            final TextView name = (TextView) getView().findViewById(R.id.textView7);
            name.setText(usableOtherUser.get(0).getIme());

            final TextView description = (TextView) getView().findViewById(R.id.textView17);
            description.setText(usableOtherUser.get(0).getOpis());

            final TextView zasebnaSoba = (TextView) getView().findViewById(R.id.textView20);
            if (usableOtherUser.get(0).isZasebnaSoba() && usableOtherUser.get(0).isApt()) {
                zasebnaSoba.setText("Zasebne sobe");
            } else if (!usableOtherUser.get(0).isZasebnaSoba() && usableOtherUser.get(0).isApt()) {
                zasebnaSoba.setText("Zajednička soba");
            } else if (usableOtherUser.get(0).isZasebnaSoba() && !usableOtherUser.get(0).isApt()) {
                zasebnaSoba.setText("Traži zasebnu sobu");
            } else if (!usableOtherUser.get(0).isZasebnaSoba() && !usableOtherUser.get(0).isApt()) {
                zasebnaSoba.setText("Može djeliti sobu");
            }

            final TextView trazi = (TextView) getView().findViewById(R.id.textView18);
            if (usableOtherUser.get(0).isApt()) {
                trazi.setText("Traži cimera za stan na kvartu " + usableOtherUser.get(0).getKvart_ime() + ", po cijeni " + usableOtherUser.get(0).getCijenaMax() + " kuna po osobi");
            } else {
                trazi.setText("Traži cimera i stan na području " + usableOtherUser.get(0).getLokacija_ime() + ", po cijeni maksimalno " + usableOtherUser.get(0).getCijenaMax() + " kuna po osobi");
            }

            final TextView ljubimci = (TextView) getView().findViewById(R.id.textView19);
            if (usableOtherUser.get(0).isLjubimac()) {
                ljubimci.setText("Ima ljubimce: ");
            } else {
                ljubimci.setText("Nema ljubimce");
            }

            final TextView age = (TextView) getView().findViewById(R.id.textView8);
            age.setText(Integer.toString(2021 - usableOtherUser.get(0).getGodina_rodenja()));

            // Slike:
            int idOfUser = usableOtherUser.get(0).getId_korisnik();
            String number = Integer.toString(idOfUser);
            String nameOfPic = "usr" + number;
            mStorageReference = FirebaseStorage.getInstance().getReference().child("images/volarevic/usr" + idOfUser);

            try {
                final File localFile = File.createTempFile(nameOfPic, "jpg");
                mStorageReference.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                                //(getView().findViewById(R.id.progressBar2)).setVisibility(View.INVISIBLE);
                                // (getView().findViewById(R.id.pictureOfUser)).setVisibility(View.VISIBLE);

                                ((ImageView) getActivity().findViewById(R.id.pictureOfUser)).setImageBitmap(bitmap);

                            }
                        })/*.addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull FileDownloadTask.TaskSnapshot snapshot) {
                        (getView().findViewById(R.id.pictureOfUser)).setVisibility(View.INVISIBLE);
                        (getView().findViewById(R.id.progressBar2)).setVisibility(View.VISIBLE);
                    }
                })*/.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //(getView().findViewById(R.id.progressBar2)).setVisibility(View.INVISIBLE);
                        (getActivity().findViewById(R.id.pictureOfUser)).setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Slika nije dohvacena", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}