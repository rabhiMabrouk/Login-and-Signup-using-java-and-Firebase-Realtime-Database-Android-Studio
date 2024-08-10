package com.example.fearbaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class loginActivity extends AppCompatActivity {
    EditText loginUser,loginPass;
    TextView signupTxtR;
    Button loginButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButt = findViewById(R.id.login_butt);
        loginPass = findViewById(R.id.login_pass);
        loginUser = findViewById(R.id.login_user);
        signupTxtR = findViewById(R.id.login_txt);

        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUserN()|!validatePass()){

                }else {
                    checkUser();
                }
            }
        });
        signupTxtR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this,signupActivity.class);
                loginActivity.this.startActivity(intent);
            }
        });
    }
    public boolean validateUserN(){
        String val = loginUser.getText().toString();
        if(val.isEmpty()){
            loginUser.setError("username cannot be empty");
            return false;
        }else
            loginUser.setError(null);
        return true;
    }
    public boolean validatePass(){
        String val = loginPass.getText().toString();
        if(val.isEmpty()){
            loginPass.setError("password cannot be empty");
            return false;
        }else {
            loginPass.setError(null);
        }
        return true;
    }
    public void checkUser(){
        String UserValidty = loginUser.getText().toString().trim();
        String  passValidty = loginPass.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query chekUserdb = reference.orderByChild("userName").equalTo(UserValidty);
        chekUserdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    loginUser.setError(null);
                    String passFromdb = snapshot.child(UserValidty).child("password").getValue(String.class);
                    if(!Objects.equals(passFromdb,passValidty)){
                        loginUser.setError(null);
                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                        loginActivity.this.startActivity(intent);
                    }else {
                        loginPass.setError("invalid Credentials");
                        loginPass.requestFocus();
                    }
                }else {
                    loginUser.setError("user does not exist");
                    loginUser.requestFocus();
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}