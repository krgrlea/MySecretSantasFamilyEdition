package com.practice.secretsanta;

import java.io.Serializable;
import java.util.Date;

public class SecretSanta implements Serializable, Comparable<SecretSanta> {
    double budget;
    String creator;
    Date date;

    /* renamed from: id */
    String f135id;
    String name;
    String place;
    boolean secretSantasSelected;

    public SecretSanta() {
    }

    public SecretSanta(String str, String str2, String str3, Date date2, String str4) {
        this.f135id = str;
        this.name = str2;
        this.place = str3;
        this.date = date2;
        this.creator = str4;
        this.secretSantasSelected = false;
    }

    public SecretSanta(String str, String str2, double d, String str3, Date date2, String str4) {
        this.f135id = str;
        this.name = str2;
        this.budget = d;
        this.place = str3;
        this.date = date2;
        this.creator = str4;
        this.secretSantasSelected = false;
    }

    public String getId() {
        return this.f135id;
    }

    public void setId(String str) {
        this.f135id = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public double getBudget() {
        return this.budget;
    }

    public void setBudget(double d) {
        this.budget = d;
    }

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String str) {
        this.place = str;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date2) {
        this.date = date2;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String str) {
        this.creator = str;
    }

    public boolean isSecretSantasSelected() {
        return this.secretSantasSelected;
    }

    public void setSecretSantasSelected(boolean z) {
        this.secretSantasSelected = z;
    }

    public String toString() {
        return this.name;
    }

    public int compareTo(SecretSanta secretSanta) {
        return this.name.toUpperCase().compareTo(secretSanta.getName().toUpperCase());
    }

    public boolean equals(Object obj) {
        return getClass().equals(obj.getClass()) && getId().equals(((SecretSanta) obj).getId());
    }
}
