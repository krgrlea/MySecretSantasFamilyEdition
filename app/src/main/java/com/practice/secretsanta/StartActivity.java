package com.practice.secretsanta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StartActivity extends AppCompatActivity {
    Button buttonAddSecretSanta;
    Button buttonLogOut;
    DatabaseReference dataRefUser;
    DatabaseReference dataRefUserSecretSanta;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    ListView listViewJoinedSecretSantas;
    SharedPreferences prefs;
    SecretSantaHelper secretSantaHelper = new SecretSantaHelper();
    StartHelper startHelper = new StartHelper();
    TextView textViewHeaderMain;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0859R.layout.activity_start);
        this.dataRefUser = this.database.getReference(getResources().getString(C0859R.string.db_user));
        this.dataRefUserSecretSanta = this.database.getReference(getResources().getString(C0859R.string.db_user_secret_santa));
        this.textViewHeaderMain = (TextView) findViewById(C0859R.C0862id.textViewHeaderMain);
        this.buttonLogOut = (Button) findViewById(C0859R.C0862id.buttonLogOut);
        this.listViewJoinedSecretSantas = (ListView) findViewById(C0859R.C0862id.listViewJoinedSecretSantas);
        this.buttonAddSecretSanta = (Button) findViewById(C0859R.C0862id.buttonAddSecretSanta);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefs = defaultSharedPreferences;
        if (defaultSharedPreferences.getBoolean("loggedIn", false)) {
            this.buttonLogOut.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    StartActivity.this.fAuth.signOut();
                    StartActivity.this.prefs.edit().putBoolean("loggedIn", false).commit();
                    StartActivity.this.startActivity(new Intent(StartActivity.this, LogInActivity.class));
                    StartActivity.this.finish();
                }
            });
            final HashMap hashMap = new HashMap();
            this.startHelper.loadAllUsers(this.dataRefUser, this.dataRefUserSecretSanta, hashMap);
            final HashMap hashMap2 = new HashMap();
            ArrayList arrayList = new ArrayList();
            C08772 r8 = new ArrayAdapter<SecretSanta>(this, 17367043, arrayList) {
                public View getView(int i, View view, ViewGroup viewGroup) {
                    View view2 = super.getView(i, view, viewGroup);
                    TextView textView = (TextView) view2.findViewById(16908308);
                    textView.setTextColor(StartActivity.this.getResources().getColor(C0859R.C0860color.colorFont));
                    textView.setTextAppearance(StartActivity.this, 16973892);
                    return view2;
                }
            };
            this.listViewJoinedSecretSantas.setAdapter(r8);
            this.startHelper.loadUserSecretSantasUser(this.dataRefUserSecretSanta, this.fAuth.getCurrentUser().getUid(), hashMap2, arrayList, r8);
            this.buttonAddSecretSanta.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    StartActivity.this.startActivity(new Intent(StartActivity.this, AddSecretSantaActivity.class));
                }
            });
            this.startHelper.loadUsersTobeSecretSantaFor(this.dataRefUserSecretSanta, this.fAuth.getCurrentUser().getUid(), new HashMap());
            this.listViewJoinedSecretSantas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    SecretSanta secretSanta = (SecretSanta) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(StartActivity.this, SecretSantaActivity.class);
                    intent.putExtra("secretSanta", secretSanta);
                    intent.putExtra("users", (Serializable) hashMap);
                    intent.putExtra("userSecretSanta", (Serializable) hashMap2.get(secretSanta.getId()));
                    StartActivity.this.startActivity(intent);
                }
            });
            return;
        }
        startActivity(new Intent(this, LogInActivity.class));
    }
}
