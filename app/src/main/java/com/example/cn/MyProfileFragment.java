package com.example.cn;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.Editable;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cn.Utils.SquareImageView;
import com.example.cn.Utils.SwipeImageView;
import com.example.cn.helpers.GlideApp;
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
    private final String FOLDER_NAME = "volarevic";
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
        fetchImage(pic1, deleteButton1, 1);
        fetchImage(pic2, deleteButton2,2);
        fetchImage(pic3, deleteButton3,3);
        fetchImage(pic4, deleteButton4,4);

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
                GlideApp.with(getActivity())
                        .load(R.drawable.ic_baseline_image_search_24)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(pic1);
                deleteButton1.setVisibility(View.GONE);
            }
        });

        deleteButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idOfUser = sessionUser.getId_korisnik();
                mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic2");
                mStorageReference.delete();
                filePath2 = null;
                GlideApp.with(getActivity())
                        .load(R.drawable.ic_baseline_image_search_24)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(pic2);
                deleteButton2.setVisibility(View.GONE);
            }
        });

        deleteButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idOfUser = sessionUser.getId_korisnik();
                mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic3");
                mStorageReference.delete();
                filePath3 = null;
                GlideApp.with(getActivity())
                        .load(R.drawable.ic_baseline_image_search_24)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(pic3);
                deleteButton3.setVisibility(View.GONE);
            }
        });

        deleteButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idOfUser = sessionUser.getId_korisnik();
                mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic4");
                mStorageReference.delete();
                filePath4 = null;
                GlideApp.with(getActivity())
                        .load(R.drawable.ic_baseline_image_search_24)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(pic4);
                deleteButton4.setVisibility(View.GONE);
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
                GlideApp.with(getActivity())
                        .load(bitmap)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(pic1);
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
                GlideApp.with(getActivity())
                        .load(bitmap)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(pic2);
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
                GlideApp.with(getActivity())
                        .load(bitmap)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(pic3);
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
                GlideApp.with(getActivity())
                        .load(bitmap)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(pic4);
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

            int idOfUser = sessionUser.getId_korisnik();

            StorageReference ref = storageReference.child("images/"+FOLDER_NAME+"/usr"+ idOfUser + "/pic" + num);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            cnt++;
                            /*if(cnt == 4){
                                // kad se sve 4 slike uploadaju, stavi sve buttone da su opet clickable
                                getActivity().findViewById(R.id.nav_profile).setClickable(true);
                                getActivity().findViewById(R.id.nav_swipe).setClickable(true);
                                getActivity().findViewById(R.id.nav_chat).setClickable(true);
                                fetchProfilePicture();
                            }*/

                            if(num == 1) fetchProfilePicture();
                            progressDialog.dismiss();
                            //Toast.makeText(MyProfileFragment.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            cnt++;
                            /*if(cnt == 4){
                                // kad se sve 4 slike uploadaju, stavi sve buttone da su opet clickable
                                getActivity().findViewById(R.id.nav_profile).setClickable(true);
                                getActivity().findViewById(R.id.nav_swipe).setClickable(true);
                                getActivity().findViewById(R.id.nav_chat).setClickable(true);
                                fetchProfilePicture();
                            }*/

                            progressDialog.dismiss();
                            // Toast.makeText(MyProfileFragment.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            /*if(cnt == 0){
                                // kad se pocnu uploadati slike, stavi sve buttone da nisu clickable
                                getActivity().findViewById(R.id.nav_profile).setClickable(false);
                                getActivity().findViewById(R.id.nav_swipe).setClickable(false);
                                getActivity().findViewById(R.id.nav_chat).setClickable(false);
                            }*/

                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    };

    private void fetchImage(SquareImageView imageView, ImageButton imageButton, int num) {
        // Ime slike sam stavila usr + broj jer za ime filea mora biti najmanje duzine 3
        int idOfUser = sessionUser.getId_korisnik();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/" + FOLDER_NAME + "/usr" + idOfUser + "/pic" + num);

        GlideApp.with(this)
                .load(mStorageReference)
                .error(R.drawable.ic_baseline_image_search_24)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageButton.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageButton.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);

        imageView.setVisibility(View.VISIBLE);

    }

    public void fetchProfilePicture(){
        int idOfUser = sessionUser.getId_korisnik();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+FOLDER_NAME+"/usr" + idOfUser + "/pic1");

        CircleImageView imageView = (CircleImageView) getView().findViewById(R.id.imageView7);

        GlideApp.with(this)
                .load(mStorageReference)
                .error(R.drawable.ic_baseline_person_24)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);

        (getView().findViewById(R.id.imageView7)).setVisibility(View.VISIBLE);

    }
}
