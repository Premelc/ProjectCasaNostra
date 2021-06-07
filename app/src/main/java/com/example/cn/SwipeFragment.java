package com.example.cn;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.cn.Utils.SwipeImageView;
import com.example.cn.model.Korisnik;
import com.example.cn.model.KorisnikLjubimac;
import com.example.cn.model.NudimStan;
import com.example.cn.model.Swipe;
import com.example.cn.sorting.SimilarityGradeSorting;
import com.example.cn.sorting.UsableOtherUser;
import com.example.cn.sql.DatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SwipeFragment extends Fragment {

    DatabaseHelper databaseHelper;
    Korisnik sessionUser;
    List<UsableOtherUser> usableOtherUser;

    private ImageButton swipeRight, swipeLeft;
    private TextView name, description, faculty, age, apartment, pets;
    private ImageView clear, check, forward, back;
    private LinearLayout hasApartment, separateRoom, hasPets;

    // Za slike:
    private StorageReference mStorageReference;
    private SwipeImageView imageView;
    private final String FOLDER_NAME = "volarevic";
    ArrayList<Bitmap> images;
    int i = 0;
    int cnt = 0;

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

        images = new ArrayList<Bitmap>();

        databaseHelper = new DatabaseHelper(getActivity());
        SimilarityGradeSorting sgs = new SimilarityGradeSorting();
        usableOtherUser = new ArrayList<>();
        usableOtherUser = sgs.Grade(sessionUser,databaseHelper);

        initViews();
        initListeners();

        display(usableOtherUser);
    }

    public void initViews(){
        swipeRight = (ImageButton) getView().findViewById(R.id.swipeRight);
        swipeLeft = (ImageButton) getView().findViewById(R.id.swipeLeft);
        forward = (ImageView) getView().findViewById(R.id.forward);
        forward.setVisibility(View.GONE);
        back = (ImageView) getView().findViewById(R.id.back);
        back.setVisibility(View.GONE);

        name = (TextView) getView().findViewById(R.id.name);
        description = (TextView) getView().findViewById(R.id.description);
        faculty = (TextView) getView().findViewById(R.id.faculty);
        age = (TextView) getView().findViewById(R.id.age);
        apartment = (TextView) getView().findViewById(R.id.apartment);
        pets = (TextView) getView().findViewById(R.id.pets);

        check = (ImageView) getView().findViewById(R.id.check); // kvacica
        clear = (ImageView) getView().findViewById(R.id.clear); // iksic

        hasApartment = (LinearLayout) getView().findViewById(R.id.hasApartment);
        separateRoom = (LinearLayout) getView().findViewById(R.id.separateRoom);
        hasPets = (LinearLayout) getView().findViewById(R.id.hasPets);

        imageView = (SwipeImageView) getView().findViewById(R.id.pictureOfUser);
    }

    public void initListeners(){
        swipeRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Swipe swipe = new Swipe();
                if(sessionUser.getId_korisnik() < usableOtherUser.get(0).getId_korisnik()){
                    swipe = databaseHelper.checkSwipe(String.valueOf(sessionUser.getId_korisnik()), String.valueOf(usableOtherUser.get(0).getId_korisnik()));

                    if(swipe != null && swipe.getId_1() > 0 && swipe.getId_2() > 0){
                        swipe.setSwipe_1(true);
                        databaseHelper.updateSwipe(swipe);
                    } else{
                        swipe.setId_1(sessionUser.getId_korisnik());
                        swipe.setId_2(usableOtherUser.get(0).getId_korisnik());
                        swipe.setSwipe_1(true);
                        databaseHelper.insertSwipe(swipe);
                    }
                } else if (sessionUser.getId_korisnik() > usableOtherUser.get(0).getId_korisnik()){
                    swipe = databaseHelper.checkSwipe(String.valueOf(usableOtherUser.get(0).getId_korisnik()), String.valueOf(sessionUser.getId_korisnik()));

                    if(swipe != null && swipe.getId_1() > 0 && swipe.getId_2() > 0){
                        swipe.setSwipe_2(true);
                        databaseHelper.updateSwipe(swipe);
                    } else{
                        swipe.setId_1(usableOtherUser.get(0).getId_korisnik());
                        swipe.setId_2(sessionUser.getId_korisnik());
                        swipe.setSwipe_2(true);
                        databaseHelper.insertSwipe(swipe);

                    }
                }

                // MATCH
                swipe = databaseHelper.checkSwipe(String.valueOf(sessionUser.getId_korisnik()), String.valueOf(usableOtherUser.get(0).getId_korisnik()));
                if(swipe.isSwipe_1() != null && swipe.isSwipe_1() == true && swipe.isSwipe_2() != null && swipe.isSwipe_2() == true){
                    Toast.makeText(getActivity(), "IT'S A MATCH!", Toast.LENGTH_SHORT).show();
                }

                usableOtherUser.remove(usableOtherUser.get(0));
                display(usableOtherUser);
            }
        });

        swipeLeft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Swipe swipe = new Swipe();
                if(sessionUser.getId_korisnik() < usableOtherUser.get(0).getId_korisnik()){
                    swipe = databaseHelper.checkSwipe(String.valueOf(sessionUser.getId_korisnik()), String.valueOf(usableOtherUser.get(0).getId_korisnik()));

                    if(swipe != null && swipe.getId_1() > 0 && swipe.getId_2() > 0){
                        swipe.setSwipe_1(false);
                        databaseHelper.updateSwipe(swipe);
                    } else{
                        swipe.setId_1(sessionUser.getId_korisnik());
                        swipe.setId_2(usableOtherUser.get(0).getId_korisnik());
                        swipe.setSwipe_1(false);
                        databaseHelper.insertSwipe(swipe);
                    }

                } else if (sessionUser.getId_korisnik() > usableOtherUser.get(0).getId_korisnik()){
                    swipe = databaseHelper.checkSwipe(String.valueOf(usableOtherUser.get(0).getId_korisnik()), String.valueOf(sessionUser.getId_korisnik()));

                    if(swipe != null && swipe.getId_1() > 0 && swipe.getId_2() > 0){
                        swipe.setSwipe_2(false);
                        databaseHelper.updateSwipe(swipe);
                    } else{
                        swipe.setId_1(usableOtherUser.get(0).getId_korisnik());
                        swipe.setId_2(sessionUser.getId_korisnik());
                        swipe.setSwipe_2(false);
                        databaseHelper.insertSwipe(swipe);
                    }

                }
                usableOtherUser.remove(usableOtherUser.get(0));
                display(usableOtherUser);

            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                imageView.setImageBitmap(images.get(i));
                back.setVisibility(View.VISIBLE);
                if(i == images.size()-1){
                    forward.setVisibility(View.GONE);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i--;
                imageView.setImageBitmap(images.get(i));
                forward.setVisibility(View.VISIBLE);
                if(i == 0){
                    back.setVisibility(View.GONE);
                }
            }
        });
    }



    private void display(List<UsableOtherUser> usableOtherUser){

        if(usableOtherUser.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("Došli ste do kraja!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // preusmjeri na chat
                    BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
                    bottomNav.setSelectedItemId(R.id.nav_chat);
                    Fragment selectedFragment = new ChatFragment().newInstance(sessionUser);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                }
            });

            AlertDialog alert = builder.create();

            alert.setTitle("");
            alert.show();
        }
        else{
            images.clear(); // lista bitmapa sa slikama korisnika koji se prikazuje
            i = 0; // index slike u listi images koja se trenutno prikazuje
            cnt = 0; // counter da se zna koliko se slika mora ucitati (4 pokusaja dohvacanja)

            fetchImage(1);
            fetchImage(2);
            fetchImage(3);
            fetchImage(4);

            name.setText(usableOtherUser.get(0).getIme());
            if(usableOtherUser.get(0).getOpis() == null || usableOtherUser.get(0).getOpis().equals("")){
                description.setVisibility(View.GONE);
            } else{
                description.setText(usableOtherUser.get(0).getOpis());
            }
            age.setText(Integer.toString(2021 - usableOtherUser.get(0).getGodina_rodenja()));

            // dohvacanje naziva fakulteta
            String whereClause = "id_korisnik = ?";
            String[] whereArgs = new String[1];
            whereArgs[0] = String.valueOf(usableOtherUser.get(0).getId_korisnik());

            String idFaculty = String.valueOf(databaseHelper.queryKorisnik(whereClause, whereArgs, null, null, null).get(0).getId_fakultet());
            whereArgs[0] = idFaculty;


            faculty.setText(databaseHelper
                    .queryFakultet("id_fakultet = ?", whereArgs, null, null, null)
                    .get(0)
                    .getNaziv());


            whereClause = "id_korisnik = ?";
            whereArgs[0] = String.valueOf(usableOtherUser.get(0).getId_korisnik());
            List<NudimStan> haveApt = new ArrayList<NudimStan>();
            haveApt = databaseHelper.queryNudimStan(whereClause, whereArgs, null, null, null);
            // ako user ima stan, pokaze se redak sa cijenom i lokacijom
            // ako nema, postavi se na GONE
            if (/*!haveApt.isEmpty()*/ usableOtherUser.get(0).isApt()) {
                hasApartment.setVisibility(View.VISIBLE);
                whereClause = "id_kvart = ?";
                whereArgs[0] = String.valueOf(haveApt.get(0).getId_kvart());
                apartment.setText("Traži cimera za stan na kvartu " +
                        databaseHelper.singleQueryKvart(whereClause, whereArgs, null, null, null).getNaziv() +
                        ", po cijeni " + haveApt.get(0).getCijena() + " kuna po osobi");
            } else {
                hasApartment.setVisibility(View.GONE);
            }

            // ako user ima stan, pokaze se redak "Zasebna soba" sa kvacicom ako ima zasebna soba, ili s iksicem ako nema
            // ako user nema stan, cijeli redak se postavi na GONE
            if(!haveApt.isEmpty()){
                separateRoom.setVisibility(View.VISIBLE);

                if(haveApt.get(0).isZasebna_soba()){
                    check.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.GONE);
                } else{
                    clear.setVisibility(View.VISIBLE);
                    check.setVisibility(View.GONE);
                }
            } else{
                separateRoom.setVisibility(View.GONE);
            }

            // dohvacanje svih ljubimaca
            whereClause = "id_korisnik = ?";
            whereArgs[0] = String.valueOf(usableOtherUser.get(0).getId_korisnik());

            List<KorisnikLjubimac> allPets = new ArrayList<>();
            allPets = databaseHelper.queryKorisnikLjubimac(whereClause, whereArgs, null, null, null);

            if (usableOtherUser.get(0).isLjubimac()) {
                hasPets.setVisibility(View.VISIBLE);

                String petString = "Ima ljubimce: ";
                for(KorisnikLjubimac pet : allPets){
                    whereClause = "id_ljubimac = ?";
                    whereArgs[0] = String.valueOf(pet.getId_ljubimac());

                    petString = petString + databaseHelper.queryLjubimac(whereClause, whereArgs, null, null, null).get(0).getVrsta().toLowerCase() + ", ";
                }
                petString = petString.substring(0, petString.length()-2);
                pets.setText(petString);
            } else {
                hasPets.setVisibility(View.GONE);
            }
        }

    }

    private void fetchImage(int num){
        int idOfUser = usableOtherUser.get(0).getId_korisnik();
        String number = Integer.toString(idOfUser);
        String nameOfPic = "usr" + number;
        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr"+ idOfUser + "/pic" + num);

        try {
            final File localFile = File.createTempFile(nameOfPic, "jpg");
            mStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                            images.add(bitmap);
                            if(images.size() == 1) {
                                ((SwipeImageView) getView().findViewById(R.id.pictureOfUser)).setImageBitmap(bitmap);
                                (getView().findViewById(R.id.progressBar2)).setVisibility(View.INVISIBLE);
                                (getView().findViewById(R.id.pictureOfUser)).setVisibility(View.VISIBLE);
                            } else if (images.size() > 1){
                                    // kad se uspjesno ucitaju barem dvije slike, moze se poceti navigirati po slikama
                                    forward.setVisibility(View.VISIBLE);
                                }

                            cnt++;
                            if (cnt == 4){
                                // kad su sve slike ucitane, mogu se kliknuti svi buttoni
                                getActivity().findViewById(R.id.nav_profile).setClickable(true);
                                getActivity().findViewById(R.id.nav_swipe).setClickable(true);
                                getActivity().findViewById(R.id.nav_chat).setClickable(true);
                                swipeRight.setClickable(true);
                                swipeLeft.setClickable(true);
                            }
                        }
                    }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull FileDownloadTask.TaskSnapshot snapshot) {
                    // blokirano prebacivanje na chat i moj profil dok se ucitavaju slike
                    getActivity().findViewById(R.id.nav_profile).setClickable(false);
                    getActivity().findViewById(R.id.nav_swipe).setClickable(false);
                    getActivity().findViewById(R.id.nav_chat).setClickable(false);
                    swipeRight.setClickable(false);
                    swipeLeft.setClickable(false);

                    if(images.isEmpty()){
                        (getView().findViewById(R.id.pictureOfUser)).setVisibility(View.INVISIBLE);
                        (getView().findViewById(R.id.progressBar2)).setVisibility(View.VISIBLE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    cnt++;
                    if (cnt == 4){
                        // kad su sve slike ucitane, mogu se kliknuti svi buttoni
                        getActivity().findViewById(R.id.nav_profile).setClickable(true);
                        getActivity().findViewById(R.id.nav_swipe).setClickable(true);
                        getActivity().findViewById(R.id.nav_chat).setClickable(true);
                        swipeRight.setClickable(true);
                        swipeLeft.setClickable(true);

                        if(images.isEmpty()){
                            // placeholder ako korisnik nema nijednu sliku
                            ((SwipeImageView) getView().findViewById(R.id.pictureOfUser)).setImageResource(R.drawable.ic_baseline_person_24);
                            (getView().findViewById(R.id.progressBar2)).setVisibility(View.INVISIBLE);
                            (getView().findViewById(R.id.pictureOfUser)).setVisibility(View.VISIBLE);
                        }
                    }
                    //Toast.makeText(getActivity(), "Slika nije dohvacena", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}