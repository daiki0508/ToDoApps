package com.websarva.wings.android.todoapps;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;
import java.util.Objects;

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;
import static com.websarva.wings.android.todoapps.ToDoActivity.db;

public class SaveDataClass{
    private AddToDoActivity addToDoActivity;
    private DetailContentActivity detailContentActivity;
    private final static String TAG = "FirebaseFirestore";

    SaveDataClass(AddToDoActivity addToDoActivity){
        this.addToDoActivity = addToDoActivity;
    }

    SaveDataClass(DetailContentActivity detailContentActivity){
        this.detailContentActivity = detailContentActivity;
    }

    void SaveData(Map<String,Object> todo_lists){
        db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("contents").document(Objects.requireNonNull(Objects.requireNonNull(todo_lists.get("title")).toString()))
                .set(todo_lists, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"saveData:success");
                        Toast.makeText(addToDoActivity,"登録しました",Toast.LENGTH_SHORT).show();
                        addToDoActivity.backIntent();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"saveData:failure");
                        Toast.makeText(addToDoActivity,"登録に失敗しました",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void update(Map<String,Object> todo_lists){
        db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("contents").document(Objects.requireNonNull(Objects.requireNonNull(todo_lists.get("title")).toString()))
                .set(todo_lists)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"saveData:success");
                        Toast.makeText(detailContentActivity,"更新しました",Toast.LENGTH_SHORT).show();
                        detailContentActivity.backIntent();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"saveData:failure");
                        Toast.makeText(detailContentActivity,"更新に失敗しました",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
