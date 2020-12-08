package com.practice.secretsanta;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class UserSecretSanta implements Serializable {

    String id;
    String userId;
    String secretSantaId;
    SecretSanta secretSanta;
    String wish;
    boolean ready;
    String idOfUserToBeSecretSantaFor;

    public UserSecretSanta() {
    }

    public UserSecretSanta(String id, String userId, SecretSanta secretSanta) {
        this.id = id;
        this.userId = userId;
        this.secretSantaId = secretSanta.getId();
        this.secretSanta = secretSanta;
        this.ready = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecretSantaId() {
        return secretSantaId;
    }

    public void setSecretSantaId(String secretSantaId) {
        this.secretSantaId = secretSantaId;
    }

    public SecretSanta getSecretSanta() {
        return secretSanta;
    }

    public void setSecretSanta(SecretSanta secretSanta) {
        this.secretSanta = secretSanta;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public String getIdOfUserToBeSecretSantaFor() {
        return idOfUserToBeSecretSantaFor;
    }

    public void setIdOfUserToBeSecretSantaFor(String idOfUserToBeSecretSantaFor) {
        this.idOfUserToBeSecretSantaFor = idOfUserToBeSecretSantaFor;
    }
}