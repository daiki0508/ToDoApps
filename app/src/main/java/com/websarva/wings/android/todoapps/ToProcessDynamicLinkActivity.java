package com.websarva.wings.android.todoapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ToProcessDynamicLinkActivity extends AppCompatActivity {
    protected static String mail;
    protected static String pass;
    private byte[] en2 = null;
    private String result = "";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_process_dynamic_link);
    }

    private void DynamicLink(){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null){
                            deepLink = pendingDynamicLinkData.getLink();
                            ToDoIntent();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ToProcessDynamicLinkActivity.this,"認証エラー",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ToDoIntent(){
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (task.isSuccessful() && Objects.requireNonNull(currentUser).isEmailVerified()){
                            Intent intent = new Intent(ToProcessDynamicLinkActivity.this,ToDoActivity.class);
                            finish();
                            overridePendingTransition(0,0);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }else {
                            Toast.makeText(ToProcessDynamicLinkActivity.this,"不正な操作です",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart(){
        super.onStart();

        DynamicLink();
    }

    @Override
    protected void onDestroy(){
        mail = "";
        pass = "";

        super.onDestroy();
    }
}