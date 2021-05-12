package com.websarva.wings.android.todoapps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;

public class AddToDoActivity extends AppCompatActivity {
    private EditText title_e;
    private EditText note_e;
    private SaveDataClass sdc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        Button add_b = findViewById(R.id.add_b);
        add_b.setOnClickListener(this::execute);
        Button clear_b = findViewById(R.id.clear_b);
        clear_b.setOnClickListener(this::execute);

        title_e = findViewById(R.id.title_edit);
        note_e = findViewById(R.id.note_edit);

        sdc = new SaveDataClass(this);
    }

    private void execute(View view){
        String title_str = title_e.getText().toString();
        String note_str = note_e.getText().toString();

        if (view.getId() == R.id.add_b){
            checkWords(title_str,note_str);
        }else if (view.getId() == R.id.clear_b){
            title_e.setText("");
            note_e.setText("");
        }
    }

    private void checkWords(String title, String note){
        int title_len = title.length();
        int note_len = note.length();
        String toast_str = "";

        if (title_len > 0 && note_len > 0 && title_len <= 30 && note_len <= 100){
            Map<String, String> todo_list = new HashMap<>();
            todo_list.put("title",title);
            todo_list.put("note",note);
            sdc.SaveData(todo_list);
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

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();;

        if (currentUser != null && currentUser.isEmailVerified()){

        }else {
            Toast.makeText(AddToDoActivity.this,"不正な操作です",Toast.LENGTH_SHORT).show();
        }
    }
}