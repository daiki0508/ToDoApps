package com.websarva.wings.android.todoapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;
import static com.websarva.wings.android.todoapps.MainActivity.mGoogleSignInClient;

public class ToDoActivity extends AppCompatActivity {
    protected static FirebaseFirestore db;
    private GetContentsClass gcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        db = FirebaseFirestore.getInstance();

        ListView contentListView = findViewById(R.id.todoList);
        List<Map<String,String>> ContentsList = new ArrayList<>();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::addTodo);

        gcc = new GetContentsClass(this);
        gcc.getContents(ContentsList,contentListView);
    }

    private void addTodo(View view){
        Intent intent = new Intent(ToDoActivity.this,AddToDoActivity.class);
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

        content.put("title", Objects.requireNonNull(get_toDoLists.get("title")).toString());
        content.put("note", Objects.requireNonNull(get_toDoLists.get("note")).toString());

        ContentsList.add(content);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()){

        }else {
            Toast.makeText(ToDoActivity.this,"不正な操作です",Toast.LENGTH_SHORT).show();
            signOutIntent();
        }
    }
}