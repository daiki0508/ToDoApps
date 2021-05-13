package com.websarva.wings.android.todoapps;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;

public class GetContentsClass extends ToDoActivity{
    private final static String TAG = "ToDoActivity";
    private final Context context;

    GetContentsClass(Context context){
        this.context = context;
    }

    void getContents(List<Map<String,String>>ContentsList, ListView contentListView){
        db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("contents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"getContents:success");
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                updateUI(ContentsList,document.getData());
                            }
                            afterUpdateUI(context,contentListView,ContentsList);
                        }else {
                            Log.w(TAG,"getContents:failure",task.getException());
                        }
                    }
                });
    }
}
