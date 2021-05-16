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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
import static com.websarva.wings.android.todoapps.MainActivity.mGoogleSignInClient;

public class DetailContentActivity extends AppCompatActivity {
    private EditText title_edit;
    private EditText note_edit;
    protected static String title_str;
    protected static String note_str;
    private String title_origin = "";
    private String note_origin = "";
    private SaveDataClass sdc;
    protected static Map<String, Object> todo_list;
    private String[] enc_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_content);

        enc_data = new String[3];

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        title_edit = findViewById(R.id.title_d);
        note_edit = findViewById(R.id.note_d);
        TextView date_textview = findViewById(R.id.date_d);

        Button update_b = findViewById(R.id.update_b);
        update_b.setOnClickListener(this::update);
        Button clear_b = findViewById(R.id.clear_b);
        clear_b.setOnClickListener(this::update);

        Intent intent = getIntent();
        title_edit.setText(intent.getStringExtra("title"));
        title_origin = intent.getStringExtra("title");

        note_edit.setText(intent.getStringExtra("note"));
        note_origin = intent.getStringExtra("note");

        date_textview.setText(intent.getStringExtra("date"));

        sdc = new SaveDataClass(this);
    }

    private void update(View view){
        title_str = title_edit.getText().toString();
        note_str = note_edit.getText().toString();

        if (view.getId() == R.id.update_b){
            checkWords(title_str,note_str);
        }else if (view.getId() == R.id.clear_b){
            title_edit.setText("");
            note_edit.setText("");
        }
    }

    protected void onFragmentResult(){
        sdc.update(todo_list);
        backIntent();
    }

    private void checkWords(String title, String note){
        int title_len = title.length();
        int note_len = note.length();
        String toast_str = "";

        if (title_len > 0 && note_len > 0 && title_len <= 30 && note_len <= 100 && !title.equals(title_origin) && !note.equals(note_origin)){
            sdc.deleteContent(title_origin);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
            encrypt(note);

            todo_list = new HashMap<>();
            todo_list.put("title",title);
            todo_list.put("note",enc_data[0]);
            todo_list.put("date",sdf.format(date));
            todo_list.put("iv",enc_data[1]);
            todo_list.put("key",enc_data[2]);

            DetailContentDialogFragment dcdf = new DetailContentDialogFragment(this);
            dcdf.show(getSupportFragmentManager(),"DetailContentDialogFragment");
        }else {
            if (title_len == 0){
                toast_str = "タイトルが入力されていません";
            }else if (title_len > 30){
                toast_str = "タイトルの最大入力可能文字数は30文字です";
            }else if (note_len == 0){
                toast_str = "メモが入力されていません";
            }else if (note_len > 100){
                toast_str = "メモの最大入力可能文字数は100文字です";
            }
            else {
                toast_str = "内容が更新されてません";
            }
            Toast.makeText(DetailContentActivity.this,toast_str,Toast.LENGTH_SHORT).show();
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
            Arrays.fill(iv, (byte) 0);
            Arrays.fill(de, (byte) 0);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e){
            e.printStackTrace();
        }
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();
        backIntent();
    }

    protected void backIntent(){
        Intent intent = new Intent(DetailContentActivity.this,MainActivity.class);
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

        return super.onOptionsItemSelected(item);
    }

    private native String getAlias();
    static {
        System.loadLibrary("main");
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()){

        }else {
            signOut();
            Toast.makeText(DetailContentActivity.this,"不正な操作です",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy(){
        title_str = "";
        note_str = "";

        super.onDestroy();
    }
}