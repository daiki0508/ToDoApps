package com.websarva.wings.android.todoapps;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import java.util.Map;
import java.util.Objects;

import static com.websarva.wings.android.todoapps.MainActivity.mGoogleSignInClient;

public class UnsubscribeActivity extends AppCompatActivity {
    private EditText reason_e;
    protected static Map<String, Object> todo_list;
    private DeleteUserClass duc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsubscribe);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        reason_e = findViewById(R.id.reason_e);
        Button send_b = findViewById(R.id.send_b);
        send_b.setOnClickListener(this::send);

        duc = new DeleteUserClass(this);
    }

    private void send(View view){
        String reason_str = reason_e.getText().toString();
        if (view.getId() == R.id.send_b){
            checkWords(reason_str);
        }
    }

    private void checkWords(String reason){
        int reason_len = reason.length();

        if (reason_len <= 250){

            todo_list = new HashMap<>();
            todo_list.put("reason",reason);

            UnsubscribeFragment uf = new UnsubscribeFragment(this);
            uf.show(getSupportFragmentManager(),"AddDialogFragment");
        }else {
            Toast.makeText(UnsubscribeActivity.this,"送信可能文字数は250文字以下です。",Toast.LENGTH_SHORT).show();
        }
    }

    protected void onFragmentResult(){
        duc.send(todo_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemID = item.getItemId();

        if (itemID == android.R.id.home){
            backIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void backIntent(){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();
        Intent intent = new Intent(UnsubscribeActivity.this,MainActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()){

        }else {
            Toast.makeText(this,"不正な操作です",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}