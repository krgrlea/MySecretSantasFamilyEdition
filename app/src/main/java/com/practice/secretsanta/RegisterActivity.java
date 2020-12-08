package com.practice.secretsanta;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    Button buttonGoToLogIn;
    Button buttonRegister;
    DatabaseReference dataRefSecretSanta;
    DatabaseReference dataRefUser;
    DatabaseReference dataRefUserSecretSanta;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    EditText editTextEnterMail;
    EditText editTextEnterName;
    EditText editTextEnterPassword;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0859R.layout.activity_register);
        this.dataRefUser = this.database.getReference(getResources().getString(C0859R.string.db_user));
        this.dataRefUserSecretSanta = this.database.getReference(getResources().getString(C0859R.string.db_user_secret_santa));
        this.dataRefSecretSanta = this.database.getReference(getResources().getString(C0859R.string.db_secret_santa));
        this.editTextEnterName = (EditText) findViewById(C0859R.C0862id.editTextEnterName);
        this.editTextEnterMail = (EditText) findViewById(C0859R.C0862id.editTextEnterMail);
        this.editTextEnterPassword = (EditText) findViewById(C0859R.C0862id.editTextEnterPassword);
        this.buttonRegister = (Button) findViewById(C0859R.C0862id.buttonRegister);
        this.buttonGoToLogIn = (Button) findViewById(C0859R.C0862id.buttonGoToLogIn);
        this.buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String trim = RegisterActivity.this.editTextEnterName.getText().toString().trim();
                final String lowerCase = RegisterActivity.this.editTextEnterMail.getText().toString().trim().toLowerCase();
                String trim2 = RegisterActivity.this.editTextEnterPassword.getText().toString().trim();
                if (TextUtils.isEmpty(lowerCase) || TextUtils.isEmpty(trim2)) {
                    if (TextUtils.isEmpty(lowerCase)) {
                        RegisterActivity.this.editTextEnterMail.setError("Es nmuss eine Mailadresse eingegeben werden.");
                    }
                    if (TextUtils.isEmpty(trim2)) {
                        RegisterActivity.this.editTextEnterPassword.setError("Es muss ein Passwort eingegeben werden.");
                    } else if (trim2.length() < 8) {
                        RegisterActivity.this.editTextEnterPassword.setError("Das Passwort muss mindestens acht Zeichen beinhalten.");
                    }
                } else {
                    RegisterActivity.this.fAuth.createUserWithEmailAndPassword(lowerCase, trim2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this).edit().putBoolean("loggedIn", true).commit();
                                RegisterActivity.this.dataRefUser.child(RegisterActivity.this.fAuth.getCurrentUser().getUid()).setValue(new User(RegisterActivity.this.fAuth.getCurrentUser().getUid(), trim, lowerCase));
                                Toast.makeText(RegisterActivity.this, "Der User wurde registriert!", 0).show();
                                RegisterActivity.this.startActivity(new Intent(RegisterActivity.this.getApplicationContext(), StartActivity.class));
                                return;
                            }
                            Toast.makeText(RegisterActivity.this, "Es gab einen Fehler bei der Registrierung!\n" + task.getException().getMessage(), 0).show();
                        }
                    });
                }
            }
        });
        this.buttonGoToLogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RegisterActivity.this.startActivity(new Intent(RegisterActivity.this.getApplicationContext(), LogInActivity.class));
            }
        });
    }
}
