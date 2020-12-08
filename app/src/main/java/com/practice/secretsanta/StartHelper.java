package com.practice.secretsanta;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

// helper class for StartActivity
public class StartHelper {


    public void loadUserSecretSantasUser(DatabaseReference dataRefUserSecretSanta, String userId, final Map<String, UserSecretSanta> userSecretSantasUser, final ArrayList<SecretSanta> secretSantas, final ArrayAdapter<SecretSanta> listViewArrayAdapter) {
        dataRefUserSecretSanta.orderByChild("userId").equalTo(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserSecretSanta userSecretSanta = dataSnapshot.getValue(UserSecretSanta.class);

                userSecretSantasUser.put(userSecretSanta.getSecretSanta().getId(), userSecretSanta);
                // add secret santa to listview
                secretSantas.add(userSecretSanta.getSecretSanta());
                listViewArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserSecretSanta userSecretSanta = dataSnapshot.getValue(UserSecretSanta.class);

                userSecretSantasUser.put(userSecretSanta.getSecretSanta().getId(), userSecretSanta);

                // remove unchanged secret santa from listview
                for (int i = 0; i < secretSantas.size(); i++) {
                    if (secretSantas.get(i).getId().equals(userSecretSanta.getSecretSantaId())) {
                        secretSantas.remove(i);
                        break;
                    }
                }

                // add changed secret santa to listview
                secretSantas.add(userSecretSanta.getSecretSanta());
                listViewArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                UserSecretSanta userSecretSanta = dataSnapshot.getValue(UserSecretSanta.class);
                userSecretSantasUser.remove(userSecretSanta.getSecretSanta().getId());
                // remove secret santa from listview
                for (int i = 0; i < secretSantas.size(); i++) {
                    if (secretSantas.get(i).getId().equals(userSecretSanta.getSecretSanta())) {
                        secretSantas.remove(i);
                        listViewArrayAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void   loadAllUsers(DatabaseReference dataRefUser, final DatabaseReference dataRefUserSecretSanta, final Map<String, User> users) {
        // teilnehmende User in die usersSecretSanta laden
        dataRefUser.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { User user = dataSnapshot.getValue(User.class);
                users.put(user.getId(), user);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // remove user from users
                User user = dataSnapshot.getValue(User.class);
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    if (entry.getValue().equals(user)) {
                        users.remove(user);
                    }
                }
                // delete userSecretSantas
                deleteUserSecretSantas(dataRefUserSecretSanta, user);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deleteUserSecretSantas(DatabaseReference dataRefUserSecretSanta, User user) {
        // not yet implemented
    }

    // load the userSecretSanta objects of the users the current user has to be secret santa for into userSecretSantas with secret santa id as key
    public void loadUsersTobeSecretSantaFor(DatabaseReference dataRefUserSecretSanta, String userId, final Map<String, UserSecretSanta> selectedSecretSantas) {
        dataRefUserSecretSanta.orderByChild("selectedSecretSantaId").equalTo(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // add userSecretSanta to selectedSecretSantas
                UserSecretSanta userSecretSanta = dataSnapshot.getValue(UserSecretSanta.class);
                selectedSecretSantas.put(userSecretSanta.getSecretSanta().getId(), userSecretSanta);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // not yet implemented

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
