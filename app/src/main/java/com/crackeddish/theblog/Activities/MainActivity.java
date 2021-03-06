package com.crackeddish.theblog.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crackeddish.theblog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText emailField;
    private EditText passwordField;
    private Button loginBtn;
    private TextView createAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailField=findViewById(R.id.emailEd);
        passwordField=findViewById(R.id.loginPasswordEd);
        loginBtn=findViewById(R.id.loginButton);
        createAct=findViewById(R.id.createAccount);
        mAuth=FirebaseAuth.getInstance();
        createAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateAccountActivity.class));
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailField.getText().toString()) && !TextUtils.isEmpty(passwordField.getText().toString())){
                    String email=emailField.getText().toString();
                    String pwd=passwordField.getText().toString();
                    login(email,pwd);
                }else{

                }
            }
        });
    }

    private void login(String email, String pwd) {
        mAuth.signInWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Signed in",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this,PostListActivity.class));
                            finish();
                        }else {
                            Toast.makeText(MainActivity.this,"Signed in Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_signout){

            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser=mAuth.getCurrentUser();
        if (mUser!=null){
            Toast.makeText(MainActivity.this,"Signed in....",Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this,PostListActivity.class));
            finish();
        }else {
            Toast.makeText(MainActivity.this,"Not signed in....",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
