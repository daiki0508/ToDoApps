package com.websarva.wings.android.todoapps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;
import static com.websarva.wings.android.todoapps.MainActivity.mGoogleSignInClient;

public class ToDoActivity extends AppCompatActivity {
    protected static FirebaseFirestore db;
    private GetContentsClass gcc;
    private List<Map<String,String>> ContentsList;
    protected static String title_str = "";
    protected static String note_str = "";
    private String result;
    private byte[] iv_decode = null;
    private byte[] en2 = null;
    private SecretKeySpec key;
    private byte[] bytes =null;
    private byte[] keys = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        db = FirebaseFirestore.getInstance();
        saveUID(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        ListView contentListView = findViewById(R.id.todoList);
        registerForContextMenu(contentListView);
        contentListView.setOnItemClickListener(new ListItemClickListener());
        ContentsList = new ArrayList<>();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::addTodo);

        gcc = new GetContentsClass(this);
        gcc.getContents(ContentsList,contentListView);
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
            detailList(item);
        }
    }

    private void detailList(Map<String,String> content){
        Intent intent = new Intent(ToDoActivity.this,DetailContentActivity.class);
        intent.putExtra("title",content.get("title"));
        intent.putExtra("note",content.get("note"));
        intent.putExtra("date",content.get("date"));
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    private void delete(Map<String,String> content){
        title_str = content.get("title");
        note_str = content.get("note");
        ToDoDiaLogFragment tdf = new ToDoDiaLogFragment(this);
        tdf.show(getSupportFragmentManager(),"ToDoDiaLogFragment");
    }

    protected void onFragmentResult(){
        gcc.deleteContent(title_str);
        Intent intent = new Intent(getIntent());
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    private void addTodo(View view){
        Intent intent = new Intent(ToDoActivity.this,AddToDoActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    private void UnsubscribeIntent(){
        Intent intent = new Intent(ToDoActivity.this,UnsubscribeActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    private void signOutIntent(){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();

        Intent intent = new Intent(ToDoActivity.this,MainActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    void updateUI(List<Map<String,String>>ContentsList,Map<String, Object> get_toDoLists){
        Map<String,String> content = new HashMap<>();
        decrypt(Objects.requireNonNull(get_toDoLists.get("note")).toString(), Objects.requireNonNull(get_toDoLists.get("iv")).toString(), Objects.requireNonNull(get_toDoLists.get("key")).toString());

        content.put("title", Objects.requireNonNull(get_toDoLists.get("title")).toString());
       // content.put("note", Objects.requireNonNull(get_toDoLists.get("note")).toString());
        content.put("note",result);
        content.put("date", Objects.requireNonNull(get_toDoLists.get("date")).toString());

        ContentsList.add(content);
    }

    private void decrypt(String note, String iv, String keys_d){
        bytes = new byte[256 / 8];
        keys = Base64.decode(keys_d,Base64.DEFAULT);

        for (int i = 0; i < new String(keys).length(); i++){
            if (i >= bytes.length){
                break;
            }
            bytes[i] = keys[i];
        }
        key = new SecretKeySpec(bytes,"AES");

        iv_decode = Base64.decode(iv,Base64.DEFAULT);
        Log.d("encrypt_iv",iv);

        try {
            IvParameterSpec ips = new IvParameterSpec(iv_decode);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,key,ips);
            en2 = cipher.doFinal(Base64.decode(note.getBytes(StandardCharsets.UTF_8),Base64.DEFAULT));

            result = new String(en2,StandardCharsets.UTF_8);
            Log.d("encrypt_re",result);
            Log.d("encrypt_alias",keys_d);
            Log.d("encrypt_note",note);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e){
            e.printStackTrace();
        }
    }

    void afterUpdateUI(Context context, ListView contentListView,List<Map<String,String>> ContentsList){
        String[] from = {"title","note"};
        int[] to = {android.R.id.text1,android.R.id.text2};

        SimpleAdapter adapter = new SimpleAdapter(context,ContentsList, android.R.layout.simple_list_item_2,from,to);
        adapter.notifyDataSetChanged();
        contentListView.setAdapter(adapter);
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

        if (itemID == R.id.signout){
            signOutIntent();
        }else if (itemID == R.id.unsubscribe){
            UnsubscribeIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context_menu_lists,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        Map<String,String> content = ContentsList.get(listPosition);

        int itemID = item.getItemId();

        if (itemID == R.id.delete){
            delete(content);
        }

        return super.onContextItemSelected(item);
    }

    private native void saveUID(String uid);
    static {
        System.loadLibrary("main");
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()){

        }else {
            Toast.makeText(ToDoActivity.this,"不正な操作です",Toast.LENGTH_SHORT).show();
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