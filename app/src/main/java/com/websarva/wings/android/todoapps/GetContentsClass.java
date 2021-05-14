package com.websarva.wings.android.todoapps;

import android.content.Context;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;

public class GetContentsClass extends ToDoActivity{
    private final static String TAG = "ToDoActivity";
    private final Context context;

    GetContentsClass(Context context){
        this.context = context;
    }

    void getContents(List<Map<String,String>>ContentsList, ListView contentListView){
        db.collection(/*"users"*/"admin").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
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

    void deleteContent(String title){
        db.collection(/*"users"*/"admin").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .collection("contents").document(title)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"deleteContent:success");
                        Toast.makeText(context,"削除しました",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"deleteContent:failure",e);
                        Toast.makeText(context,"エラーが発生しました",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
