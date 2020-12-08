package com.practice.secretsanta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddSecretSantaActivity extends AppCompatActivity {

    // UI-elements
    EditText editTextEnterNameSecretSanta;
    EditText editTextEnterBudget;
    EditText editTextEnterPlace;
    EditText editTextEnterDate;
    Button buttonAddSecretSanta;

    // database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dataRefUser;
    DatabaseReference dataRefUserSecretSanta;

    // current user
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    // helper class
    StartHelper startHelper = new StartHelper();
    SecretSantaHelper secretSantaHelper = new SecretSantaHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_secret_santa);

        // database connection
        dataRefUser = database.getReference(getResources().getString(R.string.db_user));
        dataRefUserSecretSanta = database.getReference(getResources().getString(R.string.db_user_secret_santa));

        // get UI elements
        editTextEnterNameSecretSanta = (EditText) findViewById(R.id.editTextEnterNameSecretSanta);
        editTextEnterBudget = (EditText) findViewById(R.id.editTextEnterBudget);
        editTextEnterPlace = (EditText) findViewById(R.id.editTextEnterPlace);
        editTextEnterDate = (EditText) findViewById(R.id.editTextEnterDate);
        buttonAddSecretSanta = (Button) findViewById(R.id.buttonAddNewSecretSanta);

        // date picker
        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                editTextEnterDate.setText(format.format(calendar.getTime()));
            }
        };
        // Date-Picker bei Klick öffnen
        editTextEnterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AddSecretSantaActivity.this, datePicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                //Toast.makeText(AddSecretSantaActivity.this, editTextEnterDate.getHint(), Toast.LENGTH_LONG).show();
            }
        });

        // load users
        final Map<String, User> users = new HashMap<>();
        startHelper.loadAllUsers(dataRefUser, dataRefUserSecretSanta, users);


        // create secret santa
        buttonAddSecretSanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    UserSecretSanta userSecretSanta = addSecretSanta(fAuth.getCurrentUser().getUid());
                    if (userSecretSanta != null) {
                        // go to secretSantaActivty
                        Intent intent = new Intent(AddSecretSantaActivity.this, SecretSantaActivity.class);
                        intent.putExtra("secretSanta", userSecretSanta.getSecretSanta());

                        // get suitable userSecretSanta for selected secret santa
                        intent.putExtra("users", (Serializable)  users);

                        // get userSecretSanta of the current user for the selected secret santa
                        intent.putExtra("userSecretSanta", userSecretSanta);

                        UserSecretSanta userSecretSantaToBeSecretSantaFor = null;
                        intent.putExtra("userSecretSantaToBeSecretSantaFor", userSecretSantaToBeSecretSantaFor);

                        startActivity(intent);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(AddSecretSantaActivity.this, "Bitte gib einen gültigen Wert für das Budget ein!", Toast.LENGTH_LONG).show();
                } catch (ParseException e) {
                    Toast.makeText(AddSecretSantaActivity.this, "Bitte gib einen gültigen Wert für das Datum ein!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public UserSecretSanta addSecretSanta(String userId) throws ParseException, NumberFormatException {
        String name = editTextEnterNameSecretSanta.getText().toString().trim();
        String budgetString = editTextEnterBudget.getText().toString().trim();
        String place = editTextEnterPlace.getText().toString().trim();
        String dateString = editTextEnterDate.getText().toString().trim();

        UserSecretSanta userSecretSanta = null;

        if (TextUtils.isEmpty(name)) {
            editTextEnterNameSecretSanta.setError("Bitte gib einen Namen ein!");
        } else {
            try {
                // budget
                Double budget = 0.0;
                if (!TextUtils.isEmpty(budgetString)) {
                    if (!TextUtils.isEmpty(budgetString)) {
                        budget = Double.parseDouble(budgetString);
                    }
                }


                // place
                if (TextUtils.isEmpty(place)) {
                    place = null;
                }

                // date
                Date date = null;
                if (!TextUtils.isEmpty(dateString)) {
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                    date = format.parse(dateString);
                }

                // create userSecretSanta
                final String id = dataRefUserSecretSanta.push().getKey();
                SecretSanta secretSanta = new SecretSanta(id, name, budget, place, date, fAuth.getCurrentUser().getUid());
                userSecretSanta = new UserSecretSanta(id, userId, secretSanta);
                dataRefUserSecretSanta.child(id).setValue(userSecretSanta);

            } catch (NumberFormatException e) {
                editTextEnterBudget.setText("");
                throw e;

            } catch (ParseException e) {
                editTextEnterDate.setText("");
                throw e;
            }
        }
        return userSecretSanta;
    }


}