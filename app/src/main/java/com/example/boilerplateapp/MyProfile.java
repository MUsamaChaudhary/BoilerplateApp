package com.example.boilerplateapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.boilerplateapp.Viewmodel.MyProfileViewModel;
import com.example.boilerplateapp.databinding.ActivityMyProfileBinding;
import com.example.boilerplateapp.model.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyProfile extends AppCompatActivity {

    private ActivityMyProfileBinding activityMyProfileBinding;
    private Profile profileModel;
    private MyProfileClickListner myProfileClickListner;
    private GoogleSignInClient mGoogleSignInClient;
    private MyProfileViewModel myProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        activityMyProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
        myProfileClickListner = new MyProfileClickListner(this);
        myProfileViewModel = ViewModelProviders.of(this).get(MyProfileViewModel.class);
        profileModel = new Profile();
        activityMyProfileBinding.setProfileModel(profileModel);
        activityMyProfileBinding.setClickHandler(myProfileClickListner);
        setSupportActionBar(activityMyProfileBinding.toolbar);
        activityMyProfileBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        activityMyProfileBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (account != null) {
            String email = account.getEmail();
            if (email != null) {
                Toast.makeText(this, email, Toast.LENGTH_SHORT).show();

                myProfileViewModel.getEmailUserProfileData(email);
                myProfileViewModel.getUserprofile().observeForever(new Observer<Profile>() {
                    @Override
                    public void onChanged(Profile profile) {
                        if (profile != null) {
                            Toast.makeText(MyProfile.this, "Profile is not Empty", Toast.LENGTH_SHORT).show();
                            activityMyProfileBinding.edtPEmail.setEnabled(false);
                            profileModel.setPicture_url(profile.getPicture_url());
                            Glide.with(getApplicationContext())
                                    .load(profileModel.getPicture_url())
                                    .placeholder(R.drawable.profile_image)
                                    .into(activityMyProfileBinding.circularImageView);
                            profileModel.setNumber(profile.getNumber());
                            profileModel.setEmail(profile.getEmail());
                            profileModel.setName(profile.getName());
                            profileModel.setId(profile.getId());

                        } else {
                            Toast.makeText(MyProfile.this, "Profile is Empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } else {

            }
        } else if (user != null) {
            String phonenumber = user.getPhoneNumber();
            if (phonenumber != null) {
                Toast.makeText(this, phonenumber, Toast.LENGTH_SHORT).show();
                myProfileViewModel.getNumberUSerProfileData(phonenumber);
                myProfileViewModel.getUserprofile().observeForever(new Observer<Profile>() {
                    @Override
                    public void onChanged(Profile profile) {

                        Toast.makeText(MyProfile.this, "Profile is not Empty", Toast.LENGTH_SHORT).show();
                        activityMyProfileBinding.edtPPhoneNum.setEnabled(false);
                        profileModel.setPicture_url(profile.getPicture_url());
                        Glide.with(getApplicationContext())
                                .load(profileModel.getPicture_url())
                                .placeholder(R.drawable.profile_image)
                                .into(activityMyProfileBinding.circularImageView);
                        profileModel.setNumber(profile.getNumber());
                        profileModel.setEmail(profile.getEmail());
                        profileModel.setName(profile.getName());
                        profileModel.setId(profile.getId());

                    }
                });


            }
        }
    }

    public class MyProfileClickListner {
        Context context;

        public MyProfileClickListner(Context context) {
            this.context = context;
        }

        public void UpdateProfileButtonClick(View view) {
            Toast.makeText(context, "Update Click", Toast.LENGTH_SHORT).show();
            myProfileViewModel.getUpdatedProfile(profileModel, profileModel.getId());

        }

    }


}
