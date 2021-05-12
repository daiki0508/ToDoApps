package com.websarva.wings.android.todoapps;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;
import java.util.Objects;

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;

public class SaveDataClass extends AddToDoActivity{
    private final AddToDoActivity addToDoActivity;
    private FirebaseFirestore db;
    private final static String TAG = "FirebaseFirestore";

    SaveDataClass(AddToDoActivity addToDoActivity){
        this.addToDoActivity = addToDoActivity;
        this.db = FirebaseFirestore.getInstance();
    }

    void SaveData(Map<String,String> todo_lists){
        db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("contents").document(Objects.requireNonNull(todo_lists.get("title")))
                .set(todo_lists, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"saveData:success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"saveData:failure");
                    }
                });
    }
}
