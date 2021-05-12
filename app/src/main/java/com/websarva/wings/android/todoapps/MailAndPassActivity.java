package com.websarva.wings.android.todoapps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;

public class MailAndPassActivity extends AppCompatActivity {
    private boolean flag;
    private Button execute_b;
    private Button back_b;
    private EditText mail_e;
    private EditText pass_e;
    private MailAndPassSignInClass sic;
    private MailAndPassRegistClass rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_and_pass);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch change_mode = findViewById(R.id.change_mode);
        change_mode.setOnCheckedChangeListener(new SignUpSwitchListener());

        execute_b = findViewById(R.id.execute_b);
        execute_b.setOnClickListener(this::execute);
        back_b = findViewById(R.id.back_b);
        back_b.setOnClickListener(this::execute);

        mail_e = findViewById(R.id.mail_e);
        pass_e = findViewById(R.id.password_edit);

        sic = new MailAndPassSignInClass(this);
        rc = new MailAndPassRegistClass(this);
    }

    private void execute(View view){

        if (view.getId() == R.id.execute_b){
            String mail_str = mail_e.getText().toString();
            String pass_str = pass_e.getText().toString();
            ToProcessDynamicLinkActivity.mail = mail_str;
            ToProcessDynamicLinkActivity.pass = pass_str;

            checkWords(mail_str,pass_str,flag);
        }else if (view.getId() == R.id.back_b){
            backIntent();
        }
    }

    private void checkWords(String mail_str, String pass_str, boolean flag){
        int mail_len = mail_str.length();
        int pass_len = pass_str.length();
        String toast_str = "";

        if (mail_len > 0 && mail_len <= 50 && pass_len > 8 && pass_len <= 25){
            if (flag){
                rc.Rejist(mail_str,pass_str);
            }else {
                sic.SignIn(mail_str,pass_str);
            }
        }else{
            if (mail_len == 0 && pass_len == 0){
                toast_str = "mail,もしくはpassが未入力です。";
            }else if (pass_len > 25){
                toast_str = "passの入力可能文字数は8文字以上25文字以下です。";
            }else if (mail_len > 50){
                toast_str = "mailの入力可能文字数は50文字以内です。";
            }
            Toast.makeText(MailAndPassActivity.this,toast_str,Toast.LENGTH_SHORT).show();
        }
    }

    private class SignUpSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            flag = isChecked;

            if (flag){
                execute_b.setText(getString(R.string.mail_pass_signup));
            }else {
                execute_b.setText(getString(R.string.mail_pass_signin));
            }
        }
    }

    protected void ToDoIntent(){
        Intent intent = new Intent(MailAndPassActivity.this,ToDoActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    private void backIntent(){
        Intent intent = new Intent(MailAndPassActivity.this,MainActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()){
            ToDoIntent();
        }
    }
}