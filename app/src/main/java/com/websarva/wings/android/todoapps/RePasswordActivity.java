package com.websarva.wings.android.todoapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RePasswordActivity extends AppCompatActivity {
    private EditText mail_e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_password);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        mail_e = findViewById(R.id.mail_e);
        Button repassword = findViewById(R.id.send_b);
        repassword.setOnClickListener(this::execute);
    }

    private void execute(View view){
        String mail_str = mail_e.getText().toString();
        if (view.getId() == R.id.send_b){
            checkWords(mail_str);
        }
    }

    private void checkWords(String mail_str){
        int mail_len = mail_str.length();
        String toast_str = "";

        if (mail_len > 0 && mail_len <= 50){
            sendMail(mail_str);
        }else{
            if (mail_len == 0) {
                toast_str = "mail,もしくはpassが未入力です。";
            }else{
                toast_str = "mailの入力可能文字数は50文字以内です。";
            }
            Toast.makeText(RePasswordActivity.this,toast_str,Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMail(String mail){
        FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RePasswordActivity.this,"再設定用のメールを送信しました。\nご確認ください",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_menu_lists,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemID = item.getItemId();

        if (itemID == android.R.id.home){
            Intent intent = new Intent(RePasswordActivity.this,MailAndPassActivity.class);
            finish();
            overridePendingTransition(0,0);
            startActivity(intent);
            overridePendingTransition(0,0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop(){
        finish();

        super.onStop();
    }
}