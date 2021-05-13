package com.websarva.wings.android.todoapps;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;

public class MailAndPassRegistClass extends MailAndPassActivity{
    private final MailAndPassActivity mailAndPassActivity;
    private final static String TAG = "MailAndPassActivity";
    private byte[] en2 = null;
    private String result = "";

    MailAndPassRegistClass(MailAndPassActivity mailAndPassActivity){
        this.mailAndPassActivity = mailAndPassActivity;
    }

    void Rejist(String mail, String pass){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"createUserWithEmailAndPassword:success");
                            sendMail();
                        }else {
                            Log.w(TAG,"createUserWithEmailAndPassword:failure");
                            Toast.makeText(mailAndPassActivity,"認証エラー",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendMail(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(getUrl())
                .setAndroidPackageName("com.websarva.wings.android.todoapps",false,null)
                .build();

        Objects.requireNonNull(currentUser).sendEmailVerification(actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mailAndPassActivity,"確認メールを送信しました",Toast.LENGTH_SHORT).show();
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
        }catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
                | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e){
            Log.e("Error_decode",e.getMessage());
        }

        Arrays.fill(bytes,(byte) 0);
        Arrays.fill(keys, (byte) 0);
        Arrays.fill(iv_decode,(byte) 0);
        Arrays.fill(en2,(byte) 0);

        return result;
    }

    private native String getAESData(int flag);

    static {
        System.loadLibrary("main");
    }
}
