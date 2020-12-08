package com.practice.secretsanta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.secretsanta.WishDialog;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SecretSantaActivity extends AppCompatActivity implements WishDialog.WishDialogListener {
    Button buttonAddParticipants;
    Button buttonAddWish;
    Button buttonLogOut;
    Button buttonSelectSecretSantas;
    DatabaseReference dataRefUser;
    DatabaseReference dataRefUserSecretSanta;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    ListView listViewUsersSecretSanta;
    SecretSantaHelper secretSantaHelper = new SecretSantaHelper();
    TextView textViewHeaderSecretSanta;
    UserSecretSanta userSecretSanta;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0859R.layout.activity_secret_santa);
        this.dataRefUser = this.database.getReference(getResources().getString(C0859R.string.db_user));
        this.dataRefUserSecretSanta = this.database.getReference(getResources().getString(C0859R.string.db_user_secret_santa));
        this.buttonLogOut = (Button) findViewById(C0859R.C0862id.buttonLogOut);
        this.textViewHeaderSecretSanta = (TextView) findViewById(C0859R.C0862id.textViewHeaderSecretSanta);
        this.buttonAddWish = (Button) findViewById(C0859R.C0862id.buttonAddWish);
        Button button = (Button) findViewById(C0859R.C0862id.buttonAddParticipants);
        this.buttonAddParticipants = button;
        button.setVisibility(4);
        this.listViewUsersSecretSanta = (ListView) findViewById(C0859R.C0862id.listViewUsersSecretSanta);
        this.buttonSelectSecretSantas = (Button) findViewById(C0859R.C0862id.buttonSelectSecretSantas);
        this.userSecretSanta = (UserSecretSanta) getIntent().getSerializableExtra("userSecretSanta");
        final Map map = (Map) getIntent().getSerializableExtra("users");
        this.buttonLogOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SecretSantaActivity.this.fAuth.signOut();
                SecretSantaActivity.this.startActivity(new Intent(SecretSantaActivity.this, LogInActivity.class));
                SecretSantaActivity.this.finish();
            }
        });
        this.textViewHeaderSecretSanta.setText(this.userSecretSanta.getSecretSanta().toString());
        this.textViewHeaderSecretSanta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SecretSantaActivity.this);
                builder.setCancelable(true);
                SecretSanta secretSanta = SecretSantaActivity.this.userSecretSanta.getSecretSanta();
                if (secretSanta.getBudget() <= 0.0d && secretSanta.getPlace() == null && secretSanta.getDate() == null) {
                    builder.setTitle("Es gibt keine Informationen!");
                } else {
                    builder.setTitle("Hier stehen alle Infos!");
                    EditText editText = new EditText(builder.getContext());
                    editText.setEnabled(false);
                    editText.setText("Name: " + secretSanta.getName() + "\n");
                    if (secretSanta.getBudget() > 0.0d) {
                        editText.append("Budget: " + new DecimalFormat("##0.00").format(secretSanta.getBudget()) + " €\n");
                    }
                    if (secretSanta.getPlace() != null) {
                        editText.append("Ort: " + secretSanta.getPlace() + "\n");
                    }
                    if (secretSanta.getDate() != null) {
                        editText.append("Datum: " + new SimpleDateFormat("dd.MM.yyyy").format(secretSanta.getDate()));
                    }
                    builder.setView(editText);
                }
                AlertDialog create = builder.create();
                create.setCanceledOnTouchOutside(true);
                create.show();
            }
        });
        final ArrayList arrayList = new ArrayList();
        final HashMap hashMap = new HashMap();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367043, arrayList);
        this.listViewUsersSecretSanta.setAdapter(arrayAdapter);
        this.secretSantaHelper.getAllUserSecretSantaUsers(this.dataRefUserSecretSanta, this.userSecretSanta.getSecretSanta(), map, arrayList, hashMap, arrayAdapter);
        this.buttonAddWish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WishDialog wishDialog = new WishDialog(SecretSantaActivity.this.userSecretSanta.getWish(), SecretSantaActivity.this.userSecretSanta.getSecretSanta().isSecretSantasSelected());
                wishDialog.show(SecretSantaActivity.this.getSupportFragmentManager(), "wish");
                wishDialog.setCancelable(true);
            }
        });
        if (this.userSecretSanta.getSecretSanta().isSecretSantasSelected()) {
            this.buttonSelectSecretSantas.setText(getResources().getString(C0859R.string.button_show_selected_secret_santa));
            this.buttonSelectSecretSantas.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    UserSecretSanta userSecretSanta = (UserSecretSanta) hashMap.get(SecretSantaActivity.this.userSecretSanta.getIdOfUserToBeSecretSantaFor());
                    SelectedSecretSantaDialog selectedSecretSantaDialog = new SelectedSecretSantaDialog(((User) map.get(userSecretSanta.getUserId())).getName(), userSecretSanta.getWish());
                    selectedSecretSantaDialog.show(SecretSantaActivity.this.getSupportFragmentManager(), "selectedSecretSanta");
                    selectedSecretSantaDialog.setCancelable(true);
                }
            });
        } else if (this.userSecretSanta.getSecretSanta().getCreator().equals(this.fAuth.getCurrentUser().getUid())) {
            this.buttonAddParticipants.setVisibility(0);
            this.buttonAddParticipants.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SecretSantaActivity.this);
                    builder.setCancelable(true);
                    SecretSantaActivity secretSantaActivity = SecretSantaActivity.this;
                    Map<User, Boolean> determineUserNamesAndSelected = secretSantaActivity.determineUserNamesAndSelected(secretSantaActivity.fAuth.getCurrentUser().getUid(), map, arrayList);
                    final String[] userIds = SecretSantaActivity.this.getUserIds(determineUserNamesAndSelected);
                    String[] userNames = SecretSantaActivity.this.getUserNames(determineUserNamesAndSelected);
                    final boolean[] userNamesSelected = SecretSantaActivity.this.getUserNamesSelected(determineUserNamesAndSelected);
                    if (arrayList.size() != 0) {
                        builder.setTitle("Füge einen User hinzu");
                    } else {
                        builder.setTitle("Es müssen sich erst weitere User anmelden!");
                    }
                    builder.setMultiChoiceItems(userNames, userNamesSelected, new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i, boolean z) {
                            if (z) {
                                SecretSantaActivity.this.secretSantaHelper.addUserToSecretSanta(SecretSantaActivity.this.dataRefUserSecretSanta, userIds[i], SecretSantaActivity.this.userSecretSanta.getSecretSanta());
                            } else {
                                SecretSantaActivity.this.secretSantaHelper.removeUserFromSecretSanta(SecretSantaActivity.this.dataRefUserSecretSanta, userIds[i], hashMap);
                            }
                            userNamesSelected[i] = z;
                        }
                    });
                    builder.setPositiveButton("Schließen", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog create = builder.create();
                    create.setCanceledOnTouchOutside(true);
                    create.show();
                }
            });
            this.buttonSelectSecretSantas.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    UserSecretSanta userSecretSanta = (UserSecretSanta) hashMap.get(SecretSantaActivity.this.fAuth.getCurrentUser().getUid());
                    if (userSecretSanta.getWish() == null) {
                        Toast.makeText(SecretSantaActivity.this, "Du musst zuerst einen Wunsch eintragen!", 1).show();
                    } else if (hashMap.size() == 1) {
                        Toast.makeText(SecretSantaActivity.this, "Du musst zuerst Teilnehmer hinzufügen!", 1).show();
                    } else {
                        String str = "";
                        boolean z = true;
                        for (Map.Entry entry : hashMap.entrySet()) {
                            if (((UserSecretSanta) entry.getValue()).getWish() == null && !((String) entry.getKey()).equals(userSecretSanta.getUserId())) {
                                z = false;
                                str = str + ((User) map.get(((UserSecretSanta) entry.getValue()).getUserId())).getName() + " (" + ((User) map.get(((UserSecretSanta) entry.getValue()).getUserId())).getMail() + ")\n";
                            }
                        }
                        new HashMap();
                        if (z) {
                            SecretSantaActivity.this.secretSantaHelper.selectSecretSantas(hashMap, SecretSantaActivity.this.dataRefUserSecretSanta);
                            Toast.makeText(SecretSantaActivity.this, "Die Secret Santas wurden ausgelost!", 1).show();
                            Intent intent = new Intent(SecretSantaActivity.this, SecretSantaActivity.class);
                            intent.putExtra("secretSanta", userSecretSanta.getSecretSanta());
                            intent.putExtra("users", (Serializable) map);
                            intent.putExtra("userSecretSanta", userSecretSanta);
                            SecretSantaActivity.this.startActivity(intent);
                            return;
                        }
                        Toast.makeText(SecretSantaActivity.this, "Folgende User müssen noch einen Wunsch eintragen:\n" + str, 1).show();
                    }
                }
            });
        } else {
            this.buttonSelectSecretSantas.setText(getResources().getString(C0859R.string.button_ready_for_select_secret_santas));
            this.buttonSelectSecretSantas.setVisibility(4);
        }
    }

    public Map<User, Boolean> determineUserNamesAndSelected(String str, Map<String, User> map, ArrayList<User> arrayList) {
        HashMap hashMap = new HashMap();
        for (Map.Entry next : map.entrySet()) {
            if (!((String) next.getKey()).equals(str)) {
                hashMap.put(next.getValue(), false);
                Iterator<User> it = arrayList.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (((String) next.getKey()).equals(it.next().getId())) {
                            hashMap.put(next.getValue(), true);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return hashMap;
    }

    public String[] getUserNames(Map<User, Boolean> map) {
        String[] strArr = new String[map.size()];
        int i = 0;
        for (Map.Entry next : map.entrySet()) {
            strArr[i] = ((User) next.getKey()).getName() + "\n" + ((User) next.getKey()).getMail();
            i++;
        }
        return strArr;
    }

    public boolean[] getUserNamesSelected(Map<User, Boolean> map) {
        boolean[] zArr = new boolean[map.size()];
        int i = 0;
        for (Map.Entry<User, Boolean> value : map.entrySet()) {
            zArr[i] = ((Boolean) value.getValue()).booleanValue();
            i++;
        }
        return zArr;
    }

    public String[] getUserIds(Map<User, Boolean> map) {
        String[] strArr = new String[map.size()];
        int i = 0;
        for (Map.Entry<User, Boolean> key : map.entrySet()) {
            strArr[i] = ((User) key.getKey()).getId();
            i++;
        }
        return strArr;
    }

    public void saveWish(String str) {
        this.userSecretSanta.setWish(str);
        this.dataRefUserSecretSanta.child(this.userSecretSanta.getId()).setValue(this.userSecretSanta);
    }
}
