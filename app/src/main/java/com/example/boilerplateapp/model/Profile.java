package com.example.boilerplateapp.model;

import android.net.Uri;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Profile extends BaseObservable {


    public String id;
    public String name;
    public String email;
    public String number;
    public String picture_url;
    public String picture_ref;


    public Profile() {

        this.id = "";
        this.name = "";
        this.email = "";
        this.number = "";
        this.picture_url = null;
        this.picture_ref = "";

    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }


    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        notifyPropertyChanged(BR.number);
    }

    @Bindable
    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
        notifyPropertyChanged(BR.picture_url);
    }

    @Bindable
    public String getPicture_ref() {
        return picture_ref;
    }

    public void setPicture_ref(String picture_ref) {
        this.picture_ref = picture_ref;

    }
}

