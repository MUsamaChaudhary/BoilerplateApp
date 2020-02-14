package com.example.boilerplateapp.Viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.boilerplateapp.Repository.ProfileAddRepo;
import com.example.boilerplateapp.model.Profile;

public class ProfileAddViewModel extends AndroidViewModel {

    private ProfileAddRepo addProfileRepo;

    private MutableLiveData<String> isLoadingDialogbox;
    private MutableLiveData<String> message;


    public ProfileAddViewModel(@NonNull Application application) {
        super(application);
        addProfileRepo = new ProfileAddRepo();
        isLoadingDialogbox = new MutableLiveData<String>();
        message = new MutableLiveData<String>();
    }

    public MutableLiveData<String> getIsLoadingDialogbox() {
        return isLoadingDialogbox;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public void insertProfile(Profile profile) {
        isLoadingDialogbox.setValue("Not");

        String pofileurl = profile.getPicture_url();

        if (pofileurl != null) {
            if (profile.getPicture_url().contains("https")) {
                addProfileRepo.insertProfile(profile);
            } else {
                addProfileRepo.uploadProfilePicture(profile);
            }

        }

        addProfileRepo.getMessage().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                message.setValue(s);

                if (s != null && s.equalsIgnoreCase("Succeesfully Save Profile")) {
                    isLoadingDialogbox.setValue("Yes");
                } else {

                }

            }
        });


    }

}
