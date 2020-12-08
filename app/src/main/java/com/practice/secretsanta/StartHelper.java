package com.practice.secretsanta;

import android.widget.ArrayAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.Map;

public class StartHelper {
    public void deleteUserSecretSantas(DatabaseReference databaseReference, User user) {
    }

    public void loadUserSecretSantasUser(DatabaseReference databaseReference, String str, final Map<String, UserSecretSanta> map, final ArrayList<SecretSanta> arrayList, final ArrayAdapter<SecretSanta> arrayAdapter) {
        databaseReference.orderByChild("userId").equalTo(str).addChildEventListener(new ChildEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String str) {
            }

            public void onChildAdded(DataSnapshot dataSnapshot, String str) {
                UserSecretSanta userSecretSanta = (UserSecretSanta) dataSnapshot.getValue(UserSecretSanta.class);
                map.put(userSecretSanta.getSecretSanta().getId(), userSecretSanta);
                arrayList.add(userSecretSanta.getSecretSanta());
                arrayAdapter.notifyDataSetChanged();
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String str) {
                UserSecretSanta userSecretSanta = (UserSecretSanta) dataSnapshot.getValue(UserSecretSanta.class);
                map.put(userSecretSanta.getSecretSanta().getId(), userSecretSanta);
                int i = 0;
                while (true) {
                    if (i >= arrayList.size()) {
                        break;
                    } else if (((SecretSanta) arrayList.get(i)).getId().equals(userSecretSanta.getSecretSantaId())) {
                        arrayList.remove(i);
                        break;
                    } else {
                        i++;
                    }
                }
                arrayList.add(userSecretSanta.getSecretSanta());
                arrayAdapter.notifyDataSetChanged();
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserSecretSanta userSecretSanta = (UserSecretSanta) dataSnapshot.getValue(UserSecretSanta.class);
                map.remove(userSecretSanta.getSecretSanta().getId());
                for (int i = 0; i < arrayList.size(); i++) {
                    if (((SecretSanta) arrayList.get(i)).getId().equals(userSecretSanta.getSecretSanta())) {
                        arrayList.remove(i);
                        arrayAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        });
    }

    public void loadAllUsers(DatabaseReference databaseReference, final DatabaseReference databaseReference2, final Map<String, User> map) {
        databaseReference.orderByKey().addChildEventListener(new ChildEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String str) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String str) {
            }

            public void onChildAdded(DataSnapshot dataSnapshot, String str) {
                User user = (User) dataSnapshot.getValue(User.class);
                map.put(user.getId(), user);
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user = (User) dataSnapshot.getValue(User.class);
                for (Map.Entry value : map.entrySet()) {
                    if (((User) value.getValue()).equals(user)) {
                        map.remove(user);
                    }
                }
                StartHelper.this.deleteUserSecretSantas(databaseReference2, user);
            }
        });
    }

    public void loadUsersTobeSecretSantaFor(DatabaseReference databaseReference, String str, final Map<String, UserSecretSanta> map) {
        databaseReference.orderByChild("selectedSecretSantaId").equalTo(str).addChildEventListener(new ChildEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String str) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String str) {
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            public void onChildAdded(DataSnapshot dataSnapshot, String str) {
                UserSecretSanta userSecretSanta = (UserSecretSanta) dataSnapshot.getValue(UserSecretSanta.class);
                map.put(userSecretSanta.getSecretSanta().getId(), userSecretSanta);
            }
        });
    }
}
