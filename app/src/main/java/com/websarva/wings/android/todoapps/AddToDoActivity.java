package com.websarva.wings.android.todoapps;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;

public class AddToDoActivity extends AppCompatActivity {
    private EditText title_e;
    private EditText note_e;
    private SaveDataClass sdc;
    protected static String title_str;
    protected static String note_str;
    protected static Map<String, Object> todo_list;
    private String[] enc_data;

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
        enc_data = new String[3];

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
            encrypt(note);

            todo_list = new HashMap<>();
            todo_list.put("title",title);
            todo_list.put("note",enc_data[0]);
            todo_list.put("date",sdf.format(date));
            todo_list.put("iv",enc_data[1]);
            todo_list.put("key",enc_data[2]);

            AddDialogFragment adf = new AddDialogFragment(this);
            adf.show(getSupportFragmentManager(),"AddDialogFragment");
        }else {
            if (title_len == 0){
                toast_str = "??????????????????????????????????????????";
            }else if (title_len > 30){
                toast_str = "?????????????????????????????????????????????30????????????";
            }else if (note_len == 0){
                toast_str = "????????????????????????????????????";
            }else {
                toast_str = "???????????????????????????????????????100????????????";
            }
            Toast.makeText(AddToDoActivity.this,toast_str,Toast.LENGTH_SHORT).show();
        }
    }

    private void encrypt(String note){
        byte[] bytes = new byte[256 / 8];
        byte[] keys = null;
        SecretKeySpec key;

        keys = getAlias().getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < getAlias().length(); i++){
            if (i >= bytes.length){
                break;
            }
            bytes[i] = keys[i];
        }
        key = new SecretKeySpec(bytes,"AES");
        Arrays.fill(bytes, (byte) 0);

        byte[] de = null;
        byte[] iv;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            de = cipher.doFinal(note.getBytes(StandardCharsets.UTF_8));
            iv = cipher.getIV();

            enc_data[0] = Base64.encodeToString(de,Base64.DEFAULT);
            enc_data[1] = Base64.encodeToString(iv,Base64.DEFAULT);
            enc_data[2] = Base64.encodeToString(keys,Base64.DEFAULT);
            Arrays.fill(keys, (byte) 0);
            Arrays.fill(de, (byte) 0);
            Arrays.fill(iv, (byte) 0);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e){
            e.printStackTrace();
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

    private native String getAlias();
    static {
        System.loadLibrary("main");
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();;

        if (currentUser != null && currentUser.isEmailVerified()){

        }else {
            Toast.makeText(AddToDoActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
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