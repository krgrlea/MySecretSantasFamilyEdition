package com.practice.secretsanta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextEnterName;
    EditText editTextEnterMail;
    EditText editTextEnterPassword;
    Button buttonRegister;
    Button buttonGoToLogIn;

    // Datenbank
    FirebaseDatabase database = FirebaseDatabase.getInstance();
     DatabaseReference dataRefUser;
    DatabaseReference dataRefUserSecretSanta;
    DatabaseReference dataRefSecretSanta;


    // aktueller User
    FirebaseAuth fAuth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dataRefUser = database.getReference(getResources().getString(R.string.db_user));
        dataRefUserSecretSanta = database.getReference(getResources().getString(R.string.db_user_secret_santa));
        dataRefSecretSanta = database.getReference(getResources().getString(R.string.db_secret_santa));

        // UI-Elemente auslesen
        editTextEnterName = (EditText) findViewById(R.id.editTextEnterName);
        editTextEnterMail = (EditText) findViewById(R.id.editTextEnterMail);
        editTextEnterPassword  = (EditText) findViewById(R.id.editTextEnterPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonGoToLogIn = (Button) findViewById(R.id.buttonGoToLogIn);

        // Registrieren-Button implementieren
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editTextEnterName.getText().toString().trim();
                final String mail = editTextEnterMail.getText().toString().trim().toLowerCase();
                final String password = editTextEnterPassword.getText().toString().trim();

                // Daten validieren
                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)) {
                    if (TextUtils.isEmpty(mail)) {
                        editTextEnterMail.setError("Es nmuss eine Mailadresse eingegeben werden.");
                    }
                    if (TextUtils.isEmpty(password)) {
                        editTextEnterPassword.setError("Es muss ein Passwort eingegeben werden.");
                    } else {
                        if (password.length() < 8) {
                            editTextEnterPassword.setError("Das Passwort muss mindestens acht Zeichen beinhalten.");
                        }}
                    } else {

                        // User registrieren
                        fAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
                                    prefs.edit().putBoolean("loggedIn", true).commit();

                                    // User wurde angelegt und muss jetzt noch in der Datenbank gespeichert werden
                                    User user = new User(fAuth.getCurrentUser().getUid(), name, mail);
                                    dataRefUser.child(fAuth.getCurrentUser().getUid()).setValue(user);
                                    Toast.makeText(RegisterActivity.this, "Der User wurde registriert!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                                    startActivity(intent);
                                } else {
                                    // Fehlermeldung ausgeben
                                    Toast.makeText(RegisterActivity.this, "Es gab einen Fehler bei der Registrierung!\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });
        // GoToLogIn-Button implementieren
        buttonGoToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LogIn-Page aufrufen
                                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                            }});


     }
}