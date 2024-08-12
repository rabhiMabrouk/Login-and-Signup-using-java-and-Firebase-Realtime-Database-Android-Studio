package com.example.fearbaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    CheckBox checkBox;
    private static final String fileSavelogin="sharPre";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButt = findViewById(R.id.login_butt);
        loginPass = findViewById(R.id.login_pass);
        loginUser = findViewById(R.id.login_user);
        signupTxtR = findViewById(R.id.login_txt);
        checkBox = findViewById(R.id.remeber_chekbox);


        remamberMeChekbox();

        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateUserN() && validatePass()){
                    checkUser();
                }
            }
        });
        signupTxtR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this,signupActivity.class);
                loginActivity.this.startActivity(intent);
                finish();
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

                    if(Objects.equals(passFromdb,passValidty)){
                        loginUser.setError(null);
                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                        loginActivity.this.startActivity(intent);
                        finish();

                        if(checkBox.isChecked()) {
                            SharedPreferences sharedPreferences = getSharedPreferences(fileSavelogin, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", "true");
                            editor.apply();
                            Toast.makeText(loginActivity.this, "save session", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(loginActivity.this, "bd"+passFromdb+" pass"+passValidty, Toast.LENGTH_SHORT).show();
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
                // التعامل مع الأخطاء التي قد تحدث
                Toast.makeText(loginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void remamberMeChekbox(){
        SharedPreferences sharedPreferences=getSharedPreferences(fileSavelogin,MODE_PRIVATE);
        String check = sharedPreferences.getString("name","");
        if(check.equals("true")){
            Intent intent = new Intent(loginActivity.this,MainActivity.class);
            loginActivity.this.startActivity(intent);
            finish();

        }
    }
}