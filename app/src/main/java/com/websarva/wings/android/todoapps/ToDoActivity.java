package com.websarva.wings.android.todoapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.websarva.wings.android.todoapps.MainActivity.mAuth;
import static com.websarva.wings.android.todoapps.MainActivity.mGoogleSignInClient;

public class ToDoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::addTodo);
    }

    private void addTodo(View view){
        Intent intent = new Intent(ToDoActivity.this,AddToDoActivity.class);
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