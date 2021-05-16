package com.websarva.wings.android.todoapps;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;
import java.util.Objects;

import static com.websarva.wings.android.todoapps.ToDoActivity.db;

public class DeleteUserClass extends UnsubscribeActivity{
    private final UnsubscribeActivity unsubscribeActivity;
    private final static String TAG = "UnsubscribeActivity";

    DeleteUserClass(UnsubscribeActivity unsubscribeActivity){
        this.unsubscribeActivity = unsubscribeActivity;
    }

    void send(Map<String,Object> todo_lists){
        db.collection("reason").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .set(todo_lists)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"send:success");
                        delete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"send:success");
                        Toast.makeText(unsubscribeActivity,"エラーが発生しました",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void delete(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Objects.requireNonNull(currentUser).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"delete:success");
                            unsubscribeActivity.backIntent();
                            Toast.makeText(unsubscribeActivity,"退会処理が完了しました\nご利用ありがとうございました！",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
