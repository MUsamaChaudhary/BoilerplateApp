package com.example.boilerplateapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.boilerplateapp.Application.GlobalApplication;
import com.example.boilerplateapp.Viewmodel.ProfileAddViewModel;
import com.example.boilerplateapp.databinding.ActivityProfileAddInfoBinding;
import com.example.boilerplateapp.model.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;

public class ProfileAddInfo extends AppCompatActivity {

    private ActivityProfileAddInfoBinding infoBinding;
    private ProfileAddInfoClickListner clickListner;
    private ProfileAddViewModel profileAddViewModel;
    public Profile profile;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    //ImagePicker
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private String mobileNumber;
    private String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_add_info);
        infoBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_add_info);
        profileAddViewModel = ViewModelProviders.of(this).get(ProfileAddViewModel.class);
        clickListner = new ProfileAddInfoClickListner(this);
        infoBinding.setClickHandler(clickListner);
        profile = new Profile();
        infoBinding.setProfile(profile);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mobileNumber = extras.getString("mobileNumber");

            if (mobileNumber != null) {
                profile.setNumber(mobileNumber);
            }

        }

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (acct != null) {

            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            emailAddress = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            profile.setName(personName);
            profile.setEmail(emailAddress);

            if (personPhoto != null) {
                profile.setPicture_url(personPhoto.toString());
                Glide.with(this).
                        load(personPhoto)
                        .placeholder(R.drawable.profile_image)
                        .into(infoBinding.circularImageView);
            }

        }


        profileAddViewModel.getIsLoadingDialogbox().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s != null && s.equalsIgnoreCase("Not")) {
                    builder = new AlertDialog.Builder(ProfileAddInfo.this);
                    builder.setMessage("Uplaoding");
                    builder.setCancelable(false);
                    alertDialog = builder.create();
                    alertDialog.show();


                } else {
                    alertDialog.dismiss();
                }
            }
        });

        profileAddViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s != null && s.equalsIgnoreCase("Succeesfully Save Profile")) {
                    Toast.makeText(ProfileAddInfo.this, "Hooo Record Save", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileAddInfo.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("profileEmail", emailAddress);
                    intent.putExtra("profileNumber", mobileNumber);
                    startActivity(intent);
                    GlobalApplication.getInstance().createLoginSession();
                    GlobalApplication.getInstance().createGoogleLoginSession();


                } else {
                    Toast.makeText(ProfileAddInfo.this, "Failed to Insert Data", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public class ProfileAddInfoClickListner {

        Context context;

        public ProfileAddInfoClickListner(Context context) {
            this.context = context;
        }

        public void SaveBtnClick(View view) {
            insertProfile(profile);
        }

        public void ProfileImageclick(View view) {
            Toast.makeText(context, "Profile Image click", Toast.LENGTH_SHORT).show();
            chooseImage();
        }
    }

    private void insertProfile(Profile profile) {

        profileAddViewModel.insertProfile(profile);

    }


    private void chooseImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {

                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

                }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            profile.setPicture_url(String.valueOf(data.getData()));
            Toast.makeText(this, "Hi there", Toast.LENGTH_SHORT).show();

        }
    }


}
