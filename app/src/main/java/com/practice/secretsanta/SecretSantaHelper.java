package com.practice.secretsanta;

import android.widget.ArrayAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SecretSantaHelper {
    public void addUserToSecretSanta(DatabaseReference databaseReference, String str, SecretSanta secretSanta) {
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(new UserSecretSanta(key, str, secretSanta));
    }

    public void removeUserFromSecretSanta(DatabaseReference databaseReference, String str, Map<String, UserSecretSanta> map) {
        databaseReference.child(map.get(str).getId()).removeValue();
    }

    public void getAllUserSecretSantaUsers(DatabaseReference databaseReference, SecretSanta secretSanta, Map<String, User> map, ArrayList<User> arrayList, Map<String, UserSecretSanta> map2, ArrayAdapter<User> arrayAdapter) {
        final Map<String, UserSecretSanta> map3 = map2;
        final Map<String, User> map4 = map;
        final ArrayList<User> arrayList2 = arrayList;
        final ArrayAdapter<User> arrayAdapter2 = arrayAdapter;
        databaseReference.orderByChild("secretSantaId").equalTo(secretSanta.getId()).addChildEventListener(new ChildEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String str) {
            }

            public void onChildAdded(DataSnapshot dataSnapshot, String str) {
                UserSecretSanta userSecretSanta = (UserSecretSanta) dataSnapshot.getValue(UserSecretSanta.class);
                map3.put(userSecretSanta.getUserId(), userSecretSanta);
                for (Map.Entry entry : map4.entrySet()) {
                    if (((String) entry.getKey()).equals(userSecretSanta.getUserId())) {
                        arrayList2.add(entry.getValue());
                        arrayAdapter2.notifyDataSetChanged();
                    }
                }
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String str) {
                UserSecretSanta userSecretSanta = (UserSecretSanta) dataSnapshot.getValue(UserSecretSanta.class);
                map3.put(userSecretSanta.getUserId(), userSecretSanta);
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserSecretSanta userSecretSanta = (UserSecretSanta) dataSnapshot.getValue(UserSecretSanta.class);
                map3.remove(userSecretSanta.getUserId());
                for (Map.Entry entry : map4.entrySet()) {
                    if (((String) entry.getKey()).equals(userSecretSanta.getUserId())) {
                        arrayList2.remove(entry.getValue());
                        arrayAdapter2.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void selectSecretSantas(Map<String, UserSecretSanta> map, DatabaseReference databaseReference) {
        int size = map.size();
        UserSecretSanta[] userSecretSantaArr = new UserSecretSanta[size];
        int i = 0;
        for (Map.Entry<String, UserSecretSanta> value : map.entrySet()) {
            userSecretSantaArr[i] = (UserSecretSanta) value.getValue();
            i++;
        }
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < size; i2++) {
            arrayList.add(userSecretSantaArr[i2]);
        }
        UserSecretSanta[] userSecretSantaArr2 = new UserSecretSanta[size];
        int i3 = 0;
        while (i3 < size) {
            double random = Math.random();
            double size2 = (double) arrayList.size();
            Double.isNaN(size2);
            int intValue = Double.valueOf(random * size2).intValue();
            UserSecretSanta userSecretSanta = (UserSecretSanta) arrayList.get(intValue);
            if (userSecretSanta.equals(userSecretSantaArr[i3])) {
                i3--;
                if (i3 == size - 1) {
                    arrayList = new ArrayList();
                    for (int i4 = 0; i4 < size; i4++) {
                        arrayList.add(userSecretSantaArr[i4]);
                    }
                    i3 = 0;
                }
            } else {
                userSecretSantaArr2[i3] = userSecretSanta;
                arrayList.remove(intValue);
                ArrayList arrayList2 = new ArrayList();
                for (int i5 = 0; i5 < arrayList.size(); i5++) {
                    if (arrayList.get(i5) != null) {
                        arrayList2.add(arrayList.get(i5));
                    }
                }
                arrayList = new ArrayList();
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    arrayList.add((UserSecretSanta) it.next());
                }
            }
            i3++;
        }
        for (int i6 = 0; i6 < size; i6++) {
            UserSecretSanta userSecretSanta2 = userSecretSantaArr2[i6];
            userSecretSanta2.setIdOfUserToBeSecretSantaFor(userSecretSantaArr[i6].getUserId());
            userSecretSanta2.getSecretSanta().setSecretSantasSelected(true);
            databaseReference.child(userSecretSanta2.getId()).setValue(userSecretSanta2);
        }
    }
}
