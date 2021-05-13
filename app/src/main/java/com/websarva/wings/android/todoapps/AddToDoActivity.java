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

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;

public class AddToDoActivity extends AppCompatActivity {
    private EditText title_e;
    private EditText note_e;
    private SaveDataClass sdc;
    protected static String title_str;
    protected static String note_str;
    protected static Map<String, Object> todo_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        Button add_b = findViewById(R.id.add_b);
        add_b.setOnClickListener(this::execute);
        Button clear_b = findViewById(R.id.clear_b);
        clear_b.setOnClickListener(this::execute);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        title_e = findViewById(R.id.title_edit);
        note_e = findViewById(R.id.note_edit);

        sdc = new SaveDataClass(this);
    }

    private void execute(View view){
        title_str = title_e.getText().toString();
        note_str = note_e.getText().toString();

        if (view.getId() == R.id.add_b){
            checkWords(title_str,note_str);
        }else if (view.getId() == R.id.clear_b){
            title_e.setText("");
            note_e.setText("");
        }
    }

    protected void onFragmentResult(){
        sdc.SaveData(todo_list);
    }

    private void checkWords(String title, String note){
        int title_len = title.length();
        int note_len = note.length();
        String toast_str = "";

        if (title_len > 0 && note_len > 0 && title_len <= 30 && note_len <= 100){
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

            todo_list = new HashMap<>();
            todo_list.put("title",title);
            todo_list.put("note",note);
            todo_list.put("date",sdf.format(date));

            AddDialogFragment adf = new AddDialogFragment(this);
            adf.show(getSupportFragmentManager(),"AddDialogFragment");
        }else {
            if (title_len == 0){
                toast_str = "タイトルが入力されていません";
            }else if (title_len > 30){
                toast_str = "タイトルの最大入力可能文字数は30文字です";
            }else if (note_len == 0){
                toast_str = "メモが入力されていません";
            }else {
                toast_str = "メモの最大入力可能文字数葉100文字です";
            }
            Toast.makeText(AddToDoActivity.this,toast_str,Toast.LENGTH_SHORT).show();
        }
    }

    protected void backIntent(){
        Intent intent = new Intent(AddToDoActivity.this,ToDoActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemID = item.getItemId();

        if (itemID == android.R.id.home){
            backIntent();
        }

        return  super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();;

        if (currentUser != null && currentUser.isEmailVerified()){

        }else {
            Toast.makeText(AddToDoActivity.this,"不正な操作です",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy(){
        title_str = "";
        note_str = "";

        super.onDestroy();
    }
}