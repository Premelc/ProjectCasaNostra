package com.example.cn;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.Editable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.cn.Utils.SquareImageView;
import com.example.cn.helpers.SaveSharedPreference;
import com.example.cn.model.Korisnik;
import com.example.cn.sql.DatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileFragment extends Fragment {

    private static final int RESULT_OK = -1;
    // Objekti
    DatabaseHelper databaseHelper;
    Korisnik sessionUser;

    // Widgeti
    private Button logout;
    private TextView deleteAccount;
    private ImageButton editButton;
    private EditText description;

    // Buttoni itd. za Firebase
    private Uri filePath1, filePath2, filePath3, filePath4;
    private int PICK_IMAGE_REQUEST = 1;

    //Firebase - za choose i upload
    FirebaseStorage storage;
    StorageReference storageReference;
    private final String FOLDER_NAME = "jakovic";
    int cnt = 0;

    // Za dohvacanje
    private StorageReference mStorageReference;

    // Za popup
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private SquareImageView pic1, pic2, pic3, pic4;
    private ProgressBar progressBar1, progressBar2, progressBar3, progressBar4;
    private ImageButton deleteButton1, deleteButton2, deleteButton3, deleteButton4;
    private Button uploadButton;

    private boolean flag = false;

    public static MyProfileFragment newInstance(Korisnik user){
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Session", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sessionUser = (Korisnik) getArguments().getSerializable("Session");
        return inflater.inflate(R.layout.fragment_my_profile, container, false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(getActivity());

        initViews();
        initListeners();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    private void initViews(){
        logout = (Button) getView().findViewById(R.id.logout);
        deleteAccount = (TextView) getView().findViewById(R.id.deleteAccount);
        editButton = (ImageButton) getView().findViewById(R.id.editButton);

        final TextView name = (TextView) getView().findViewById(R.id.textView19);
        name.setText(sessionUser.getIme());

        description = (EditText) getView().findViewById(R.id.editTextTextPersonName3);
        description.setText(sessionUser.getOpis());

        final TextView faculty = (TextView) getView().findViewById(R.id.textView20);
        String idFaculty = String.valueOf(databaseHelper
                .queryKorisnik("id_korisnik = ?", new String[]{String.valueOf(sessionUser.getId_korisnik())}, null, null, null)
                .get(0).getId_fakultet());
        faculty.setText(databaseHelper
                .queryFakultet("id_fakultet = ?", new String[]{idFaculty}, null, null, null)
                .get(0).getNaziv());

        fetchProfilePicture();
    }



    private void initListeners() {

        description.setImeOptions(EditorInfo.IME_ACTION_DONE);
        description.setRawInputType(InputType.TYPE_CLASS_TEXT);
        description.setHorizontallyScrolling(false);
        description.setLines(3);

        description.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                sessionUser.setOpis(arg0.toString());
                databaseHelper.updateKorisnik(sessionUser);
                SaveSharedPreference.setSessionUser(getActivity(), sessionUser);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prije odjave postavi null objekt kao session user
                Korisnik user = new Korisnik();
                SaveSharedPreference.setSessionUser(getActivity(), user);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteUser();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }

                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Jeste li sigurni da želite izbrisati svoj korisnički račun?").setPositiveButton("Da", dialogClickListener)
                        .setNegativeButton("Ne", dialogClickListener).show();
            }
        });
    }

    public void deleteUser(){
        // brisanje slika
        int idOfUser = sessionUser.getId_korisnik();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic1");
        mStorageReference.delete();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic2");
        mStorageReference.delete();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic3");
        mStorageReference.delete();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic4");
        mStorageReference.delete();

        databaseHelper.deleteKorisnik(sessionUser); // brisanje korisnika

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Uspješno ste izbrisali svoj korisnički račun.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Korisnik user = new Korisnik();
                        SaveSharedPreference.setSessionUser(getActivity(), user);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
        ;

        AlertDialog alert = builder.create();

        alert.setTitle("USPJEH");
        alert.show();
    }


    public void popupWindow(){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View popup = getLayoutInflater().inflate(R.layout.popup_upload, null);

        pic1 = (SquareImageView) popup.findViewById(R.id.pic1);
        pic2 = (SquareImageView) popup.findViewById(R.id.pic2);
        pic3 = (SquareImageView) popup.findViewById(R.id.pic3);
        pic4 = (SquareImageView) popup.findViewById(R.id.pic4);

        progressBar1 = (ProgressBar) popup.findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) popup.findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) popup.findViewById(R.id.progressBar3);
        progressBar4 = (ProgressBar) popup.findViewById(R.id.progressBar4);

        deleteButton1 = (ImageButton) popup.findViewById(R.id.deleteButton1);
        deleteButton2 = (ImageButton) popup.findViewById(R.id.deleteButton2);
        deleteButton3 = (ImageButton) popup.findViewById(R.id.deleteButton3);
        deleteButton4 = (ImageButton) popup.findViewById(R.id.deleteButton4);

        uploadButton = (Button) popup.findViewById(R.id.upload);

        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();

        deleteButton1.setVisibility(View.GONE);
        deleteButton2.setVisibility(View.GONE);
        deleteButton3.setVisibility(View.GONE);
        deleteButton4.setVisibility(View.GONE);
        pic1.setVisibility(View.INVISIBLE);
        pic2.setVisibility(View.INVISIBLE);
        pic3.setVisibility(View.INVISIBLE);
        pic4.setVisibility(View.INVISIBLE);

        cnt = 0;
        // dohvacanje slika koje su vec uploadane na firebase
        fetchImage(pic1, progressBar1, deleteButton1, 1);
        fetchImage(pic2, progressBar2, deleteButton2,2);
        fetchImage(pic3, progressBar3, deleteButton3,3);
        fetchImage(pic4, progressBar4, deleteButton4,4);

        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(1);
            }
        });

        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(2);
            }
        });

        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(3);
            }
        });

        pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(4);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt = 0;
                uploadImage(filePath1, 1);
                uploadImage(filePath2, 2);
                uploadImage(filePath3, 3);
                uploadImage(filePath4, 4);

                dialog.dismiss();
            }
        });

        deleteButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idOfUser = sessionUser.getId_korisnik();
                mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic1");
                mStorageReference.delete();
                filePath1 = null;
                deleteButton1.setVisibility(View.GONE);
                pic1.setImageResource(R.drawable.ic_baseline_image_search_24);
            }
        });

        deleteButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idOfUser = sessionUser.getId_korisnik();
                mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic2");
                mStorageReference.delete();
                filePath2 = null;
                deleteButton2.setVisibility(View.GONE);
                pic2.setImageResource(R.drawable.ic_baseline_image_search_24);
            }
        });

        deleteButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idOfUser = sessionUser.getId_korisnik();
                mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic3");
                mStorageReference.delete();
                filePath3 = null;
                deleteButton3.setVisibility(View.GONE);
                pic3.setImageResource(R.drawable.ic_baseline_image_search_24);
            }
        });

        deleteButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idOfUser = sessionUser.getId_korisnik();
                mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic4");
                mStorageReference.delete();
                filePath4 = null;
                deleteButton4.setVisibility(View.GONE);
                pic4.setImageResource(R.drawable.ic_baseline_image_search_24);
            }
        });

    }


    // Za sliku
    private void chooseImage(int num) {
        PICK_IMAGE_REQUEST = num;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath1 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath1);
                pic1.setImageBitmap(bitmap);
                pic1.setVisibility(View.VISIBLE);
                deleteButton1.setVisibility(View.VISIBLE);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        else if(requestCode == 2 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath2 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath2);
                pic2.setImageBitmap(bitmap);
                pic2.setVisibility(View.VISIBLE);
                deleteButton2.setVisibility(View.VISIBLE);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        else if(requestCode == 3 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath3 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath3);
                pic3.setImageBitmap(bitmap);
                pic3.setVisibility(View.VISIBLE);
                deleteButton3.setVisibility(View.VISIBLE);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if(requestCode == 4 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath4 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath4);
                pic4.setImageBitmap(bitmap);
                pic4.setVisibility(View.VISIBLE);
                deleteButton4.setVisibility(View.VISIBLE);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage(Uri filePath, int num) {
        if(filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Promijeniti naziv slike - pomocu id-a
            int idOfUser = sessionUser.getId_korisnik();
            /*
            String usr = "user" + idOfUser;
            int i = 1;
            StorageReference ref = storageReference.child("images/usr/"+ idOfUser + "_" + i);*/

            StorageReference ref = storageReference.child("images/"+FOLDER_NAME+"/usr"+ idOfUser + "/pic" + num);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            cnt++;
                            if(cnt == 4){
                                // kad se sve 4 slike uploadaju, stavi sve buttone da su opet clickable
                                getActivity().findViewById(R.id.nav_profile).setClickable(true);
                                getActivity().findViewById(R.id.nav_swipe).setClickable(true);
                                getActivity().findViewById(R.id.nav_chat).setClickable(true);
                                fetchProfilePicture();
                            }

                            if(num == 1) fetchProfilePicture();
                            progressDialog.dismiss();
                            //Toast.makeText(MyProfileFragment.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            cnt++;
                            if(cnt == 4){
                                // kad se sve 4 slike uploadaju, stavi sve buttone da su opet clickable
                                getActivity().findViewById(R.id.nav_profile).setClickable(true);
                                getActivity().findViewById(R.id.nav_swipe).setClickable(true);
                                getActivity().findViewById(R.id.nav_chat).setClickable(true);
                                fetchProfilePicture();
                            }

                            progressDialog.dismiss();
                            // Toast.makeText(MyProfileFragment.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            if(cnt == 0){
                                // kad se pocnu uploadati slike, stavi sve buttone da nisu clickable
                                getActivity().findViewById(R.id.nav_profile).setClickable(false);
                                getActivity().findViewById(R.id.nav_swipe).setClickable(false);
                                getActivity().findViewById(R.id.nav_chat).setClickable(false);
                            }

                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    };

    private void fetchImage(SquareImageView imageView, ProgressBar progressBar, ImageButton imageButton, int num){
        // Ime slike sam stavila usr + broj jer za ime filea mora biti najmanje duzine 3
        int idOfUser = sessionUser.getId_korisnik();
        String number = Integer.toString(idOfUser);
        String nameOfPic = "usr" + number;
        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic" + num);

        try {
            final File localFile = File.createTempFile(nameOfPic, "jpg");
            mStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(getActivity(), "Slika je dohvacena", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                            progressBar.setVisibility(View.INVISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                            imageButton.setVisibility(View.VISIBLE);

                            imageView.setImageBitmap(bitmap);

                            cnt++;
                            if(cnt == 4){
                                // kad se sve 4 slike ucitaju, stavi sve buttone da su opet clickable
                                getActivity().findViewById(R.id.nav_profile).setClickable(true);
                                getActivity().findViewById(R.id.nav_swipe).setClickable(true);
                                getActivity().findViewById(R.id.nav_chat).setClickable(true);
                            }
                        }
                    }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull FileDownloadTask.TaskSnapshot snapshot) {

                    if(cnt == 0){
                        // kad se pocnu ucitavati slike, stavi sve buttone da nisu clickable
                        getActivity().findViewById(R.id.nav_profile).setClickable(false);
                        getActivity().findViewById(R.id.nav_swipe).setClickable(false);
                        getActivity().findViewById(R.id.nav_chat).setClickable(false);
                    }

                    imageView.setVisibility(View.INVISIBLE);
                    imageButton.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressBar.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    imageButton.setVisibility(View.INVISIBLE);

                    cnt++;
                    if(cnt == 4){
                        // kad se sve 4 slike ucitaju, stavi sve buttone da su opet clickable
                        getActivity().findViewById(R.id.nav_profile).setClickable(true);
                        getActivity().findViewById(R.id.nav_swipe).setClickable(true);
                        getActivity().findViewById(R.id.nav_chat).setClickable(true);
                    }

                    //Toast.makeText(getActivity(), "Slika nije dohvacena", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchProfilePicture(){
        int idOfUser = sessionUser.getId_korisnik();
        String number = Integer.toString(idOfUser);
        String nameOfPic = "pic1";
        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic1");

        CircleImageView imageView = (CircleImageView) getView().findViewById(R.id.imageView7);
        try {
            final File localFile = File.createTempFile(nameOfPic, "jpg");
            mStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                            (getView().findViewById(R.id.progressBar2)).setVisibility(View.INVISIBLE);
                            (getView().findViewById(R.id.imageView7)).setVisibility(View.VISIBLE);

                            getActivity().findViewById(R.id.nav_profile).setClickable(true);
                            getActivity().findViewById(R.id.nav_swipe).setClickable(true);
                            getActivity().findViewById(R.id.nav_chat).setClickable(true);

                            ((CircleImageView) getView().findViewById(R.id.imageView7)).setImageBitmap(bitmap);

                        }
                    }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull FileDownloadTask.TaskSnapshot snapshot) {
                    getActivity().findViewById(R.id.nav_profile).setClickable(false);
                    getActivity().findViewById(R.id.nav_swipe).setClickable(false);
                    getActivity().findViewById(R.id.nav_chat).setClickable(false);

                    (getView().findViewById(R.id.imageView7)).setVisibility(View.INVISIBLE);
                    (getView().findViewById(R.id.progressBar2)).setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ((CircleImageView) getView().findViewById(R.id.imageView7)).setImageResource(R.drawable.ic_baseline_person_24);
                    (getView().findViewById(R.id.progressBar2)).setVisibility(View.INVISIBLE);
                    (getView().findViewById(R.id.imageView7)).setVisibility(View.VISIBLE);

                    getActivity().findViewById(R.id.nav_profile).setClickable(true);
                    getActivity().findViewById(R.id.nav_swipe).setClickable(true);
                    getActivity().findViewById(R.id.nav_chat).setClickable(true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}