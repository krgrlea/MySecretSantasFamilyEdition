package com.practice.secretsanta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// start of the app
public class StartActivity extends AppCompatActivity {

    // UI
    TextView textViewHeaderMain;
    Button buttonLogOut;

    // listView for joined secret santas
    ListView listViewJoinedSecretSantas;
    Button buttonAddSecretSanta;

    // user
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    // database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dataRefUser;
    DatabaseReference dataRefUserSecretSanta;

    // instances of helper classes
    SecretSantaHelper secretSantaHelper = new SecretSantaHelper();
    StartHelper startHelper = new StartHelper();

    SharedPreferences prefs;

    // called while starting activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // call activity_start.xml
        setContentView(R.layout.activity_start);


        // references for database
        dataRefUser = database.getReference(getResources().getString(R.string.db_user));
        dataRefUserSecretSanta = database.getReference(getResources().getString(R.string.db_user_secret_santa));


        // UI-elements
        textViewHeaderMain = (TextView) findViewById(R.id.textViewHeaderMain);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        listViewJoinedSecretSantas = (ListView) findViewById(R.id.listViewJoinedSecretSantas);
        buttonAddSecretSanta = (Button) findViewById(R.id.buttonAddSecretSanta);


        // check if user is signed in
        prefs = PreferenceManager.getDefaultSharedPreferences(StartActivity.this);
        boolean loggedIn = prefs.getBoolean("loggedIn", false);
        //prefs.edit().putBoolean("loggedIn", false).commit();

        if (loggedIn) {

            // logout possible
            buttonLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fAuth.signOut();
                    prefs.edit().putBoolean("loggedIn", false).commit();
                    // go back to login
                    Intent intent = new Intent(StartActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                }
            });


            // load all users for adding users to the secret santa in the next step
            final Map<String, User> users = new HashMap<>();
            startHelper.loadAllUsers(dataRefUser, dataRefUserSecretSanta, users);


            // userSecretSantas of the User
            final Map<String, UserSecretSanta> userSecretSantasUser = new HashMap<>();

            // secret santas the user is in for the listView
            final ArrayList<SecretSanta> secretSantas = new ArrayList<>();

            // add adapter for joined secret santas
            final ArrayAdapter listViewArrayAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, secretSantas);
            listViewJoinedSecretSantas.setAdapter(listViewArrayAdapter);

            // load userSecretSanta
            startHelper.loadUserSecretSantasUser(dataRefUserSecretSanta, fAuth.getCurrentUser().getUid(), userSecretSantasUser, secretSantas, listViewArrayAdapter);


            // create new secret santa
            buttonAddSecretSanta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StartActivity.this, AddSecretSantaActivity.class);
                    startActivity(intent);
                }
            });


            // UserSecretSantas of the users the current user is secret santa for with id of secret santa as key
            final Map<String, UserSecretSanta> selectedSecretSantas = new HashMap<>();
            // load users to be secret santa for
            startHelper.loadUsersTobeSecretSantaFor(dataRefUserSecretSanta, fAuth.getCurrentUser().getUid(), selectedSecretSantas);


            // open secret santa by click
            listViewJoinedSecretSantas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    // get selected secret santa
                    SecretSanta secretSanta = (SecretSanta) adapterView.getItemAtPosition(i);

                    // call SecretSantaActivity
                    Intent intent = new Intent(StartActivity.this, SecretSantaActivity.class);
                    intent.putExtra("secretSanta", secretSanta);

                    // get suitable userSecretSanta for selected secret santa
                    intent.putExtra("users", (Serializable) users);

                    // get userSecretSanta of the current user for the selected secret santa
                    intent.putExtra("userSecretSanta", userSecretSantasUser.get(secretSanta.getId()));

                    // call activity
                    startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent(StartActivity.this, LogInActivity.class);

            // call activity
            startActivity(intent);
        }

    }
}