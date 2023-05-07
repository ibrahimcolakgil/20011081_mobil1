package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private String emailStr, passwordStr;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        MaterialButton loginBtn = (MaterialButton) findViewById(R.id.loginbtn);

        //Dont have an account text handler.
        TextView dontHaveAccount = (TextView) findViewById(R.id.donthaveaccount);

        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        
        mAuth = FirebaseAuth.getInstance();
    }

    public void logIn(View v){
        emailStr = email.getText().toString();
        passwordStr = password.getText().toString();

        if(!TextUtils.isEmpty(emailStr) && !TextUtils.isEmpty(passwordStr)){
            mAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                    .addOnSuccessListener(MainActivity.this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {//Login is successful.
                            mUser = mAuth.getCurrentUser();

                            Toast.makeText(MainActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();

                            //Login to the app.
                            startActivity(new Intent(MainActivity.this,AppActivity.class));
                        }
                    }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {//Login is unsuccessful.
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(MainActivity.this, "Please fill the mandatory fields!", Toast.LENGTH_SHORT).show();
        }
    }
}