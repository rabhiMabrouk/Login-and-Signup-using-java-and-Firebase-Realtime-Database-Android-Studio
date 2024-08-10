package com.example.fearbaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signupActivity extends AppCompatActivity {
    EditText nameSignup,emailSignup,userNameS,passwordS;
    Button buttSignup;
    TextView txtRedirectLogin;
    FirebaseDatabase Database;
    DatabaseReference referenceDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameSignup=findViewById(R.id.signupName);
        emailSignup=findViewById(R.id.signupEmail);
        userNameS=findViewById(R.id.signupUserName);
        passwordS=findViewById(R.id.signupPass);
        buttSignup=findViewById(R.id.signupButt);
        txtRedirectLogin=findViewById(R.id.login_txtR);
        buttSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database = FirebaseDatabase.getInstance();
                referenceDb=Database.getReference("users");
                String name = nameSignup.getText().toString();
                String email = emailSignup.getText().toString();
                String userN = userNameS.getText().toString();
                String password=passwordS.getText().toString();

                HelperClass helperClass = new HelperClass(name,email,userN,password);
                referenceDb.child(name).setValue(helperClass);
                Toast.makeText(signupActivity.this, "you have signup successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(signupActivity.this,loginActivity.class);
                signupActivity.this.startActivity(intent);
            }
        });
        txtRedirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signupActivity.this,loginActivity.class);
                signupActivity.this.startActivity(intent);
            }
        });

    }
}