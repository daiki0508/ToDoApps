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
                            if (getUrl().equals(deepLink)){
                                ToDoIntent();
                            }
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
        MainActivity.mAuth.signInWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser currentUser = MainActivity.mAuth.getCurrentUser();
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

    private String getUrl(){
        byte[] bytes = new byte[256 / 8];
        byte[] keys = Base64.decode(getAESData(1),Base64.DEFAULT);

        for (int i = 0; i < new String(keys).length(); i++){
            if (i >= bytes.length){
                break;
            }
            bytes[i] = keys[i];
        }
        SecretKeySpec key = new SecretKeySpec(bytes,"AES");

        byte[] iv_decode = Base64.decode(getAESData(2),Base64.DEFAULT);

        try {
            IvParameterSpec ips = new IvParameterSpec(iv_decode);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,key,ips);
            en2 = cipher.doFinal(Base64.decode(getAESData(0).getBytes(StandardCharsets.UTF_8),Base64.DEFAULT));

            result = new String(en2,StandardCharsets.US_ASCII);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e){
            Log.e("error_decode",e.getMessage());
        }

        Arrays.fill(bytes,(byte) 0);
        Arrays.fill(keys,(byte) 0);
        Arrays.fill(iv_decode,(byte) 0);
        Arrays.fill(en2,(byte) 0);

        return result;
    }

    private native String getAESData(int flag);

    static {
        System.loadLibrary("main");
    }

    protected void onStart(){
        super.onStart();

        DynamicLink();
    }
}