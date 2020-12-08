package com.practice.secretsanta;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SecretSanta implements Serializable, Comparable<SecretSanta> {
    String id;
    String name;
    double budget;
    String place;
    Date date;
    String creator;
    boolean secretSantasSelected;

    public SecretSanta() {
    }

    public SecretSanta(String id, String name, String place, Date date, String creator) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.date = date;
        this.creator = creator;
        this.secretSantasSelected = false;
    }

    public SecretSanta(String id, String name, double budget, String place, Date date, String creator) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.place = place;
        this.date = date;
        this.creator = creator;
        this.secretSantasSelected = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isSecretSantasSelected() {
        return secretSantasSelected;
    }

    public void setSecretSantasSelected(boolean secretSantasSelected) {
        this.secretSantasSelected = secretSantasSelected;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(SecretSanta secretSanta) {
        return this.name.toUpperCase().compareTo(secretSanta.getName().toUpperCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this.getClass().equals(o.getClass())) {
            SecretSanta secretSanta = (SecretSanta) o;
            if (this.getId().equals(secretSanta.getId())) {
                return true;
            }
        }
        return false;
    }
}
