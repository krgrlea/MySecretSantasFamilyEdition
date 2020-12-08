package com.practice.secretsanta;

import java.io.Serializable;

public class UserSecretSanta implements Serializable {

    /* renamed from: id */
    String f137id;
    String idOfUserToBeSecretSantaFor;
    SecretSanta secretSanta;
    String secretSantaId;
    String userId;
    String wish;

    public UserSecretSanta() {
    }

    public UserSecretSanta(String str, String str2, SecretSanta secretSanta2) {
        this.f137id = str;
        this.userId = str2;
        this.secretSantaId = secretSanta2.getId();
        this.secretSanta = secretSanta2;
    }

    public String getId() {
        return this.f137id;
    }

    public void setId(String str) {
        this.f137id = str;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public String getSecretSantaId() {
        return this.secretSantaId;
    }

    public void setSecretSantaId(String str) {
        this.secretSantaId = str;
    }

    public SecretSanta getSecretSanta() {
        return this.secretSanta;
    }

    public void setSecretSanta(SecretSanta secretSanta2) {
        this.secretSanta = secretSanta2;
    }

    public String getWish() {
        return this.wish;
    }

    public void setWish(String str) {
        this.wish = str;
    }

    public String getIdOfUserToBeSecretSantaFor() {
        return this.idOfUserToBeSecretSantaFor;
    }

    public void setIdOfUserToBeSecretSantaFor(String str) {
        this.idOfUserToBeSecretSantaFor = str;
    }
}
