package com.example.boilerplateapp.Viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.boilerplateapp.Repository.ProfileAddRepo;
import com.example.boilerplateapp.model.Profile;

public class MyProfileViewModel extends AndroidViewModel {

    private ProfileAddRepo profileAddRepo;
    private MutableLiveData<Profile> userprofile = new MutableLiveData<>();

    public MutableLiveData<Profile> getUserprofile() {
        return userprofile;
    }

    public MyProfileViewModel(@NonNull Application application) {
        super(application);
        profileAddRepo = new ProfileAddRepo();
    }

    public void getEmailUserProfileData(String email) {
        profileAddRepo.getUserProfileData(email);
        profileAddRepo.getUserprofile().observeForever(new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                if (profile != null) {
                    userprofile.setValue(profile);
                }
            }
        });

    }

    public void getNumberUSerProfileData(String phonenumber) {
        profileAddRepo.getNumberUserProfileData(phonenumber);
        profileAddRepo.getUserprofile().observeForever(new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {

                if (profile != null) {
                    userprofile.setValue(profile);
                }

            }
        });
    }


    public void getUpdatedProfile(Profile profile, String profileid) {
        profileAddRepo.updatebothprofile(profile, profileid);

    }


}
