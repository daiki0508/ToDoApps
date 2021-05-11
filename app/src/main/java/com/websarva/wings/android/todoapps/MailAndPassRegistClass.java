package com.websarva.wings.android.todoapps;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class MailAndPassRegistClass extends MailAndPassActivity{
    private final MailAndPassActivity mailAndPassActivity;
    private final static String TAG = "MailAndPassActivity";

    MailAndPassRegistClass(MailAndPassActivity mailAndPassActivity){
        this.mailAndPassActivity = mailAndPassActivity;
    }

    void Rejist(String mail, String pass){
        MainActivity.mAuth.createUserWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(mailAndPassActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"createUserWithEmailAndPassword:success");
                        }else {
                            Log.w(TAG,"createUserWithEmailAndPassword:failure");
                        }
                    }
                });
    }
}
