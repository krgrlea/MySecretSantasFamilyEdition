package com.practice.secretsanta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// start of the app
public class LogInActivity extends AppCompatActivity {

    // UI-elements
    EditText editTextLogInMail;
    EditText editTextLogInPassword;
    Button buttonLogIn;
    Button buttonGoToRegister;

    // current user
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    // database connection
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dataRef;

    // help class
    StartHelper startHelper = new StartHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // UI-Elemente auslesen
        editTextLogInMail = (EditText) findViewById(R.id.editTextLogInMail);
        editTextLogInPassword = (EditText) findViewById(R.id.editTextLogInPassword);
        buttonLogIn = (Button) findViewById(R.id.buttonLogIn);
        buttonGoToRegister = (Button) findViewById(R.id.buttonGoToRegister);


        // login
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = editTextLogInMail.getText().toString().trim().toLowerCase();
                String password = editTextLogInPassword.getText().toString().trim();

                // validate data
                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)) {
                    if (TextUtils.isEmpty(mail)) {
                        editTextLogInMail.setError("Es muss eine Mailadresse eingegeben werden.");
                    }
                    if (TextUtils.isEmpty(password)) {
                        editTextLogInPassword.setError("Es muss ein Passwort eingegeben werden.");
                    } else {
                        if (password.length() < 8) {
                            editTextLogInPassword.setError("Das Passwort muss mindestens acht Zeichen beinhalten.");
                        }
                    }
                } else {
                    // authentificate user
                    fAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LogInActivity.this);
                                prefs.edit().putBoolean("loggedIn", true).commit();

                                // user logged in
                                Toast.makeText(LogInActivity.this, "Login erfolgreich!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                                startActivity(intent);

                            } else {

                                // error while login
                                Toast.makeText(LogInActivity.this, "Kein Login möglich!\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        // go to register
        buttonGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LogIn-Page aufrufen
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }});

    }

}