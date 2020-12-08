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

public class LogInActivity extends AppCompatActivity {
    Button buttonGoToRegister;
    Button buttonLogIn;
    DatabaseReference dataRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    EditText editTextLogInMail;
    EditText editTextLogInPassword;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    StartHelper startHelper = new StartHelper();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0859R.layout.activity_log_in);
        this.editTextLogInMail = (EditText) findViewById(C0859R.C0862id.editTextLogInMail);
        this.editTextLogInPassword = (EditText) findViewById(C0859R.C0862id.editTextLogInPassword);
        this.buttonLogIn = (Button) findViewById(C0859R.C0862id.buttonLogIn);
        this.buttonGoToRegister = (Button) findViewById(C0859R.C0862id.buttonGoToRegister);
        this.buttonLogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String lowerCase = LogInActivity.this.editTextLogInMail.getText().toString().trim().toLowerCase();
                String trim = LogInActivity.this.editTextLogInPassword.getText().toString().trim();
                if (TextUtils.isEmpty(lowerCase) || TextUtils.isEmpty(trim)) {
                    if (TextUtils.isEmpty(lowerCase)) {
                        LogInActivity.this.editTextLogInMail.setError("Es muss eine Mailadresse eingegeben werden.");
                    }
                    if (TextUtils.isEmpty(trim)) {
                        LogInActivity.this.editTextLogInPassword.setError("Es muss ein Passwort eingegeben werden.");
                    } else if (trim.length() < 8) {
                        LogInActivity.this.editTextLogInPassword.setError("Das Passwort muss mindestens acht Zeichen beinhalten.");
                    }
                } else {
                    LogInActivity.this.fAuth.signInWithEmailAndPassword(lowerCase, trim).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                PreferenceManager.getDefaultSharedPreferences(LogInActivity.this).edit().putBoolean("loggedIn", true).commit();
                                Toast.makeText(LogInActivity.this, "Login erfolgreich!", 0).show();
                                LogInActivity.this.startActivity(new Intent(LogInActivity.this.getApplicationContext(), StartActivity.class));
                                return;
                            }
                            Toast.makeText(LogInActivity.this, "Kein Login möglich!\n" + task.getException().getMessage(), 0).show();
                        }
                    });
                }
            }
        });
        this.buttonGoToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LogInActivity.this.startActivity(new Intent(LogInActivity.this.getApplicationContext(), RegisterActivity.class));
            }
        });
    }
}
