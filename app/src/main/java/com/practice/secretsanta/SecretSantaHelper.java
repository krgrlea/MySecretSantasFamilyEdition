package com.practice.secretsanta;

import android.text.TextUtils;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecretSantaHelper {

    public void addUserToSecretSanta(DatabaseReference dataRefUserSecretSanta, String userId, SecretSanta secretSanta) {
        // get a new unique key
        final String id = dataRefUserSecretSanta.push().getKey();
        //add user to secret santa
        UserSecretSanta userSecretSanta = new UserSecretSanta(id, userId, secretSanta);
        dataRefUserSecretSanta.child(id).setValue(userSecretSanta);
    }


    public void removeUserFromSecretSanta(DatabaseReference dataRefUserSecretSanta, final String userId, Map<String, UserSecretSanta> userSecretSantaUsers) {
        String userSecretSantaId = userSecretSantaUsers.get(userId).getId();
        // remove user from secret santa
        dataRefUserSecretSanta.child(userSecretSantaId).removeValue();
    }

    public void getAllUserSecretSantaUsers(DatabaseReference dataRefUserSecretSanta, SecretSanta secretSanta, final Map<String, User> users, final ArrayList<User> usersSecretSanta, final Map<String, UserSecretSanta> userSecretSantaUsers, final ArrayAdapter<User> listViewArrayAdapter) {
        dataRefUserSecretSanta.orderByChild("secretSantaId").equalTo(secretSanta.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserSecretSanta userSecretSanta = dataSnapshot.getValue(UserSecretSanta.class);
                userSecretSantaUsers.put(userSecretSanta.getUserId(), userSecretSanta);
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    if (entry.getKey().equals(userSecretSanta.getUserId())) {
                        usersSecretSanta.add(entry.getValue());
                        // update listview with users
                        listViewArrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserSecretSanta userSecretSanta = dataSnapshot.getValue(UserSecretSanta.class);
                userSecretSantaUsers.put(userSecretSanta.getUserId(), userSecretSanta);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                UserSecretSanta userSecretSanta = dataSnapshot.getValue(UserSecretSanta.class);
                userSecretSantaUsers.remove(userSecretSanta.getUserId());
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    if (entry.getKey().equals(userSecretSanta.getUserId())) {
                        usersSecretSanta.remove(entry.getValue());
                        listViewArrayAdapter.notifyDataSetChanged();
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

    // select secret santas
    public void selectSecretSantas(Map<String, UserSecretSanta> userSecretSantaUsers, DatabaseReference dataRefUserSecretSanta) {

        // put userSecretSanta into array
        UserSecretSanta[] userSecretSantaArray = new UserSecretSanta[userSecretSantaUsers.size()];
        int counter = 0;
        for (Map.Entry<String, UserSecretSanta> entry : userSecretSantaUsers.entrySet()) {
            userSecretSantaArray[counter] = entry.getValue();
            counter++;
        }

        // put userSecretSantas into list
        ArrayList<UserSecretSanta> usersSelect = new ArrayList<>();
        for (int i = 0; i < userSecretSantaArray.length; i++) {
            usersSelect.add(userSecretSantaArray[i]);
        }

        // array for secret santas
        UserSecretSanta[] secretSantas = new UserSecretSanta[userSecretSantaArray.length];

        // go through userSercetSantas
        for (int i = 0; i < userSecretSantaArray.length; i++) {

            // get a random userSecretSanta
            Double d = (Math.random() * usersSelect.size());
            int number = d.intValue();
            UserSecretSanta selectedSecretSanta = usersSelect.get(number);

            // check if the selected one isnt the same as the user
            if (selectedSecretSanta.equals(userSecretSantaArray[i])) {

                // if both are same go one step back
                i--;

                // check if it was the pre last selection
                if (i == (userSecretSantaArray.length - 1)) {

                    // go back to start
                    i = 0;
                    usersSelect = new ArrayList<>();
                    for (int j = 0; j < userSecretSantaArray.length; j++) {
                        usersSelect.add(userSecretSantaArray[j]);
                    }
                }

            } else {

                // put selected secret santa into secretSantas
                secretSantas[i] = selectedSecretSanta;

                // remove the selected selected secret santa from the list of possible secret santas
                usersSelect.remove(number);
                ArrayList<UserSecretSanta> usersSelectHelp = new ArrayList<>();
                for (int j = 0; j < usersSelect.size(); j++) {
                    if (usersSelect.get(j) != null) {
                        usersSelectHelp.add(usersSelect.get(j));
                    }
                }

                // put the users who are not secret santas already into the list of possible secret santas
                usersSelect = new ArrayList<>();
                for (UserSecretSanta user : usersSelectHelp) {
                    usersSelect.add(user);
                }

            }
        }

        // save secret santa selection
        for (int i = 0; i < secretSantas.length; i++) {
            // secret santa
            UserSecretSanta selectedSecretSanta = secretSantas[i];

            // user to be secret santa for
            UserSecretSanta userSecretSantaToBeSecretSantaFor = userSecretSantaArray[i];

            selectedSecretSanta.getSecretSanta().setSecretSantasSelected(true);

            dataRefUserSecretSanta.child(selectedSecretSanta.getId()).setValue(selectedSecretSanta);
        }
    }
}
