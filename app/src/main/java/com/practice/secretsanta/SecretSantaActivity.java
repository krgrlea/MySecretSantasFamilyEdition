package com.practice.secretsanta;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class SecretSantaActivity extends AppCompatActivity {

    // UI elements
    Button buttonLogOut;
    TextView textViewHeaderSecretSanta;
    Button buttonAddWish;
    Button buttonAddParticipants;
    ListView listViewUsersSecretSanta;
    Button buttonSelectSecretSantas;

    // database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dataRefUser;
    DatabaseReference dataRefUserSecretSanta;

    // current user
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    // helper class
    SecretSantaHelper secretSantaHelper = new SecretSantaHelper();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_santa);


        // database connection
        dataRefUser = database.getReference(getResources().getString(R.string.db_user));
        dataRefUserSecretSanta = database.getReference(getResources().getString(R.string.db_user_secret_santa));


        // get UI elements
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        textViewHeaderSecretSanta = (TextView) findViewById(R.id.textViewHeaderSecretSanta);
        buttonAddWish = (Button) findViewById(R.id.buttonAddWish);
        buttonAddParticipants = (Button) findViewById(R.id.buttonAddParticipants);
        buttonAddParticipants.setVisibility(View.INVISIBLE);
        listViewUsersSecretSanta = (ListView) findViewById(R.id.listViewUsersSecretSanta);
        buttonSelectSecretSantas = (Button) findViewById(R.id.buttonSelectSecretSantas);


        // get userSecretSanta of current user
        final UserSecretSanta userSecretSanta = (UserSecretSanta) getIntent().getSerializableExtra("userSecretSanta");

        // get all users
        final Map<String, User> users = (Map<String, User>) getIntent().getSerializableExtra("users");


        // logout possible
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                // zur Login-Seite springen
                Intent intent = new Intent(SecretSantaActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // name of secret santa as header
        textViewHeaderSecretSanta.setText(userSecretSanta.getSecretSanta().toString());

        // show secret santa information by click on header
        textViewHeaderSecretSanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // dialog for showing information about secret santa
                AlertDialog.Builder builder = new AlertDialog.Builder(SecretSantaActivity.this);
                builder.setCancelable(true);

                SecretSanta secretSanta = userSecretSanta.getSecretSanta();

                if (secretSanta.getBudget() > 0 || secretSanta.getPlace() != null || secretSanta.getDate() != null) {
                    builder.setTitle("Hier stehen alle Infos!");
                    // textView for information about selected secret santa
                    EditText editTextInfo = new EditText(builder.getContext());

                    // set editText disabled
                    editTextInfo.setEnabled(false);

                    // add information about secret santa
                    editTextInfo.setText("Name: " + secretSanta.getName() + "\n");
                    if (secretSanta.getBudget() > 0) {
                        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
                        String budgetString = decimalFormat.format(secretSanta.getBudget());
                        editTextInfo.append("Budget: " + budgetString + " €\n");
                    }
                    if (secretSanta.getPlace() != null) {
                        editTextInfo.append("Ort: " + secretSanta.getPlace() + "\n");
                    }
                    if (secretSanta.getDate() != null) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        String dateString = simpleDateFormat.format(secretSanta.getDate());
                        editTextInfo.append("Datum: " + dateString);
                    }

                    // add editText with information to dialog
                    builder.setView(editTextInfo);

                } else {
                    builder.setTitle("Es gibt keine Informationen!");
                }


                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });


        // user of the selected secret santa
        final ArrayList<User> usersSecretSanta = new ArrayList<>();
        final Map<String, UserSecretSanta> userSecretSantaUsers = new HashMap<>();

        // listView for the participants
        final ArrayAdapter listViewArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, usersSecretSanta);
        listViewUsersSecretSanta.setAdapter(listViewArrayAdapter);

        // load users into listView
        secretSantaHelper.getAllUserSecretSantaUsers(dataRefUserSecretSanta, userSecretSanta.getSecretSanta(), users, usersSecretSanta, userSecretSantaUsers, listViewArrayAdapter);


        // add Wish
        buttonAddWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog for adding wish
                final AlertDialog.Builder builder = new AlertDialog.Builder(SecretSantaActivity.this);
                builder.setCancelable(true);

                builder.setTitle("Wunsch hinzufügen");

                final UserSecretSanta userSecretSanta = userSecretSantaUsers.get(fAuth.getCurrentUser().getUid());
                final EditText editTextEnterWish = new EditText(SecretSantaActivity.this);
                editTextEnterWish.setHint("Bitte gib deinen Wunsch hier ein!");

                if (userSecretSanta.getWish() != null) {
                    editTextEnterWish.setText(userSecretSanta.getWish());
                }

                // set editTextEnterWish disabled if secret santas are already selected
                if (userSecretSanta.getSecretSanta().isSecretSantasSelected()) {
                    editTextEnterWish.setEnabled(false);
                }


                builder.setView(editTextEnterWish);

                builder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!(userSecretSanta.getWish() == null && TextUtils.isEmpty(editTextEnterWish.getText()))) {
                            // update Wish
                            userSecretSanta.setWish(editTextEnterWish.getText().toString().trim());
                            dataRefUserSecretSanta.child(userSecretSanta.getId()).setValue(userSecretSanta);
                        }
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });


        if (userSecretSanta.getSecretSanta().isSecretSantasSelected()) {
            // check if the secret santas are already selected
            buttonSelectSecretSantas.setText(getResources().getString(R.string.button_show_selected_secret_santa));
            buttonSelectSecretSantas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // dialog for showing user to be secret santa for
                    AlertDialog.Builder builder = new AlertDialog.Builder(SecretSantaActivity.this);
                    builder.setCancelable(true);

                    UserSecretSanta userSecretSanta = userSecretSantaUsers.get(fAuth.getCurrentUser().getUid());
                    UserSecretSanta userSecretSantaOfUserToBeSecretSantaFor = userSecretSantaUsers.get(userSecretSanta.getIdOfUserToBeSecretSantaFor());
                    User userToBeSecretSantaFor = users.get(userSecretSantaOfUserToBeSecretSantaFor.getUserId());

                    builder.setTitle("Du bist der Secret Santa für...");
                    // textView for information about selected secret santa
                    TextView textViewInfo = new TextView(builder.getContext());

                    // add name of selected secret santa
                    textViewInfo.setText("..." + userToBeSecretSantaFor.getName() + " und dein Wichtel wünscht sich:\n");
                    textViewInfo.append(userSecretSantaOfUserToBeSecretSantaFor.getWish() + "!\n");

                    // add editText with information to dialog
                    builder.setView(textViewInfo);

                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            });

        } else if (userSecretSanta.getSecretSanta().getCreator().equals(fAuth.getCurrentUser().getUid())) {
            // check if the current user is the creator of the secret santa
            // implement addParticipants
            buttonAddParticipants.setVisibility(View.VISIBLE);
            buttonAddParticipants.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Dialog zum Teilnehmer hinzufügen erstellen
                    AlertDialog.Builder builder = new AlertDialog.Builder(SecretSantaActivity.this);
                    // abbrechen möglich
                    builder.setCancelable(true);

                    // bestimmen, welche User bereits am Wichteln teilnehmen
                    final Map<User, Boolean> userNamesAndSelected = determineUserNamesAndSelected(fAuth.getCurrentUser().getUid(), users, usersSecretSanta);

                    // UserNames für den Dialog auslesen
                    final String[] userIds = getUserIds(userNamesAndSelected);
                    final String[] userNames = getUserNames(userNamesAndSelected);
                    final boolean[] userNamesSelected = getUserNamesSelected(userNamesAndSelected);

                    // Titel festlegen
                    if (usersSecretSanta.size() != 0) {
                        builder.setTitle("Füge einen User hinzu");
                    } else {
                        builder.setTitle("Es müssen sich erst weitere User anmelden!");
                    }
                    builder.setMultiChoiceItems(userNames, userNamesSelected, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                            if (b) {
                                // ausgewählten User zum Wichteln hinzufügen
                                secretSantaHelper.addUserToSecretSanta(dataRefUserSecretSanta, userIds[i], userSecretSanta.getSecretSanta());
                            } else {
                                // ausgewählten User vom Wichteln entfernen
                                secretSantaHelper.removeUserFromSecretSanta(dataRefUserSecretSanta, userIds[i], userSecretSantaUsers);
                            }
                            // User zum Wichteln hinzufügen oder entfernen
                            userNamesSelected[i] = b;
                        }
                    });
                    builder.setPositiveButton("Schließen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            });

            // creator can select secret santas if all users are ready
            buttonSelectSecretSantas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserSecretSanta userSecretSanta = userSecretSantaUsers.get(fAuth.getCurrentUser().getUid());

                    if (userSecretSanta.getWish() == null) {
                        // a user has to give a wish first
                        Toast.makeText(SecretSantaActivity.this, "Du musst zuerst einen Wunsch eintragen!", Toast.LENGTH_LONG).show();

                    } else {

                        // check if there is more than one user
                        if (userSecretSantaUsers.size() == 1) {
                            Toast.makeText(SecretSantaActivity.this, "Du musst zuerst Teilnehmer hinzufügen!", Toast.LENGTH_LONG).show();
                        } else {

                        // select secret santas if all users are ready
                            String usersNotReady = "";
                            boolean allReady = true;
                            for (Map.Entry<String, UserSecretSanta> entry : userSecretSantaUsers.entrySet()) {
                                if (!entry.getValue().isReady() && !entry.getKey().equals(userSecretSanta.getUserId())) {
                                    allReady = false;
                                    usersNotReady += users.get(entry.getValue().getUserId()).getName() + " (" + users.get(entry.getValue().getUserId()).getMail() + ")\n";
                                }
                            }
                            Map<String, UserSecretSanta> selectedSecretSantas = new HashMap<>();

                            if (allReady) {
                                secretSantaHelper.selectSecretSantas(userSecretSantaUsers, dataRefUserSecretSanta);
                                Toast.makeText(SecretSantaActivity.this, "Die Secret Santas wurden ausgelost!", Toast.LENGTH_LONG).show();


                                // load again activity to apply changes
                                Intent intent = new Intent(SecretSantaActivity.this, SecretSantaActivity.class);

                                // selected secret santa
                                intent.putExtra("secretSanta", userSecretSanta.getSecretSanta());

                                // all users
                                intent.putExtra("users", (Serializable) users);

                                // userSecretSanta of the current user for the selected secret santa
                                intent.putExtra("userSecretSanta", userSecretSanta);

                                // call activity
                                startActivity(intent);

                            } else {

                                // show who still has to add a wish
                                Toast.makeText(SecretSantaActivity.this, "Folgende User müssen noch einen Wunsch eintragen:\n" + usersNotReady, Toast.LENGTH_LONG).show();
                            }
                    }}
                }
            });
        } else {

            // other participants can only say that they are ready, it is possible to select the secret santas if all participants are ready
            buttonSelectSecretSantas.setText(getResources().getString(R.string.button_ready_for_select_secret_santas));

            // disable button if user is already ready
            if (userSecretSanta.isReady()) {
                buttonSelectSecretSantas.setEnabled(false);
            }

            // 
            buttonSelectSecretSantas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // check if the user has already added a wish
                    if (userSecretSanta.getWish() != null) {
                        // set user to ready
                        userSecretSanta.setReady(true);

                        // load again activity to apply changes
                        Intent intent = new Intent(SecretSantaActivity.this, SecretSantaActivity.class);

                        // selected secret santa
                        intent.putExtra("secretSanta", userSecretSanta.getSecretSanta());

                        // all users
                        intent.putExtra("users", (Serializable) users);

                        // userSecretSanta of the current user for the selected secret santa
                        intent.putExtra("userSecretSanta", userSecretSanta);

                        // call activity
                        startActivity(intent);

                    } else {

                        Toast.makeText(SecretSantaActivity.this, "Du musst zuerst einen Wunsch eintragen!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }

    // get the users with flag if already in this secret santa or not
    public Map<User, Boolean> determineUserNamesAndSelected(String userId, Map<String, User> users, ArrayList<User> usersSecretSanta) {

        HashMap<User, Boolean> userNamesAndSelected = new HashMap<>();

        for (Map.Entry<String, User> entry : users.entrySet()) {
            if (!entry.getKey().equals(userId)) {
                userNamesAndSelected.put(entry.getValue(), false);
                for (User userSecretSanta : usersSecretSanta) {
                    if (entry.getKey().equals(userSecretSanta.getId())) {
                        // user is participant
                        userNamesAndSelected.put(entry.getValue(), true);
                        break;
                    }
                }
            }
        }
        return userNamesAndSelected;
    }

    public String[] getUserNames(Map<User, Boolean> userNamesAndSelected) {

        String[] userNames = new String[userNamesAndSelected.size()];
        int counter = 0;
        for (Map.Entry<User, Boolean> entry : userNamesAndSelected.entrySet()) {
            userNames[counter] = entry.getKey().getName() + "\n" + entry.getKey().getMail();
            counter++;
        }
        return userNames;
    }

    public boolean[] getUserNamesSelected(Map<User, Boolean> userNamesAndSelected) {
        boolean[] userNamesSelected = new boolean[userNamesAndSelected.size()];
        int counter = 0;
        for (Map.Entry<User, Boolean> entry : userNamesAndSelected.entrySet()) {
            userNamesSelected[counter] = entry.getValue();
            counter++;
        }
        return userNamesSelected;
    }

    public String[] getUserIds(Map<User, Boolean> userNamesAndSelected) {
        String[] userIds = new String[userNamesAndSelected.size()];
        int counter = 0;
        for (Map.Entry<User, Boolean> entry : userNamesAndSelected.entrySet()) {
            userIds[counter] = entry.getKey().getId();
            counter++;
        }
        return userIds;
    }
}