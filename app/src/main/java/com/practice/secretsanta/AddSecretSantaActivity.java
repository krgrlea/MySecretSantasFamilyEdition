package com.practice.secretsanta;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddSecretSantaActivity extends AppCompatActivity {
    Button buttonAddSecretSanta;
    DatabaseReference dataRefUser;
    DatabaseReference dataRefUserSecretSanta;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    EditText editTextEnterBudget;
    EditText editTextEnterDate;
    EditText editTextEnterNameSecretSanta;
    EditText editTextEnterPlace;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    SecretSantaHelper secretSantaHelper = new SecretSantaHelper();
    StartHelper startHelper = new StartHelper();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0859R.layout.activity_add_secret_santa);
        this.dataRefUser = this.database.getReference(getResources().getString(C0859R.string.db_user));
        this.dataRefUserSecretSanta = this.database.getReference(getResources().getString(C0859R.string.db_user_secret_santa));
        this.editTextEnterNameSecretSanta = (EditText) findViewById(C0859R.C0862id.editTextEnterNameSecretSanta);
        this.editTextEnterBudget = (EditText) findViewById(C0859R.C0862id.editTextEnterBudget);
        this.editTextEnterPlace = (EditText) findViewById(C0859R.C0862id.editTextEnterPlace);
        this.editTextEnterDate = (EditText) findViewById(C0859R.C0862id.editTextEnterDate);
        this.buttonAddSecretSanta = (Button) findViewById(C0859R.C0862id.buttonAddNewSecretSanta);
        final Calendar instance = Calendar.getInstance();
        final C08531 r0 = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                instance.set(1, i);
                instance.set(2, i2);
                instance.set(5, i3);
                AddSecretSantaActivity.this.editTextEnterDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(instance.getTime()));
            }
        };
        this.editTextEnterDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(AddSecretSantaActivity.this, r0, instance.get(1), instance.get(2), instance.get(5)).show();
            }
        });
        final HashMap hashMap = new HashMap();
        this.startHelper.loadAllUsers(this.dataRefUser, this.dataRefUserSecretSanta, hashMap);
        this.buttonAddSecretSanta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    AddSecretSantaActivity addSecretSantaActivity = AddSecretSantaActivity.this;
                    UserSecretSanta addSecretSanta = addSecretSantaActivity.addSecretSanta(addSecretSantaActivity.fAuth.getCurrentUser().getUid());
                    if (addSecretSanta != null) {
                        Intent intent = new Intent(AddSecretSantaActivity.this, SecretSantaActivity.class);
                        intent.putExtra("secretSanta", addSecretSanta.getSecretSanta());
                        intent.putExtra("users", (Serializable) hashMap);
                        intent.putExtra("userSecretSanta", addSecretSanta);
                        intent.putExtra("userSecretSantaToBeSecretSantaFor", (Serializable) null);
                        AddSecretSantaActivity.this.startActivity(intent);
                    }
                } catch (NumberFormatException unused) {
                    Toast.makeText(AddSecretSantaActivity.this, "Bitte gib einen gültigen Wert für das Budget ein!", 1).show();
                } catch (ParseException unused2) {
                    Toast.makeText(AddSecretSantaActivity.this, "Bitte gib einen gültigen Wert für das Datum ein!", 1).show();
                }
            }
        });
    }

    public UserSecretSanta addSecretSanta(String str) throws ParseException, NumberFormatException {
        String trim = this.editTextEnterNameSecretSanta.getText().toString().trim();
        String trim2 = this.editTextEnterBudget.getText().toString().trim();
        String trim3 = this.editTextEnterPlace.getText().toString().trim();
        String trim4 = this.editTextEnterDate.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            this.editTextEnterNameSecretSanta.setError("Bitte gib einen Namen ein!");
            return null;
        }
        try {
            Double valueOf = Double.valueOf(0.0d);
            if (!TextUtils.isEmpty(trim2) && !TextUtils.isEmpty(trim2)) {
                valueOf = Double.valueOf(Double.parseDouble(trim2));
            }
            String str2 = TextUtils.isEmpty(trim3) ? null : trim3;
            Date parse = !TextUtils.isEmpty(trim4) ? new SimpleDateFormat("dd.MM.yyyy").parse(trim4) : null;
            String key = this.dataRefUserSecretSanta.push().getKey();
            UserSecretSanta userSecretSanta = new UserSecretSanta(key, str, new SecretSanta(key, trim, valueOf.doubleValue(), str2, parse, this.fAuth.getCurrentUser().getUid()));
            this.dataRefUserSecretSanta.child(key).setValue(userSecretSanta);
            return userSecretSanta;
        } catch (NumberFormatException e) {
            this.editTextEnterBudget.setText("");
            throw e;
        } catch (ParseException e2) {
            this.editTextEnterDate.setText("");
            throw e2;
        }
    }
}
