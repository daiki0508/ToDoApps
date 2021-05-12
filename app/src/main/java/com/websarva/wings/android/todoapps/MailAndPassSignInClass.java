package com.websarva.wings.android.todoapps;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MailAndPassSignInClass extends MailAndPassActivity{
    private final MailAndPassActivity mailAndPassActivity;
    private final static String TAG = "MailAndPassActivity";

    MailAndPassSignInClass(MailAndPassActivity mailAndPassActivity){
        this.mailAndPassActivity = mailAndPassActivity;
    }

    void SignIn(String mail, String pass){
        MainActivity.mAuth.signInWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(mailAndPassActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser currentUser = MainActivity.mAuth.getCurrentUser();
                        if (task.isSuccessful() && Objects.requireNonNull(currentUser).isEmailVerified()){
                            Log.d(TAG,"signInWithEmailAndPassword:success");
                            mailAndPassActivity.ToDoIntent();
                        }else {
                            Log.w(TAG,"signInWithEmailAndPassword:failure");
                            Toast.makeText(mailAndPassActivity,"認証エラー",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
