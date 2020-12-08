package com.practice.secretsanta;

import java.io.Serializable;

public class User implements Serializable {

    /* renamed from: id */
    String f136id;
    String mail;
    String name;

    public User() {
    }

    public User(String str, String str2, String str3) {
        this.f136id = str;
        this.name = str2;
        this.mail = str3;
    }

    public String getId() {
        return this.f136id;
    }

    public void setId(String str) {
        this.f136id = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String str) {
        this.mail = str;
    }

    public String toString() {
        return this.name;
    }
}
