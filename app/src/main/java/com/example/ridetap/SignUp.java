package com.example.ridetap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private EditText etpassword,etemail,etname;
    private TextView tvsignin;
    private Button btnsignup;
    private ImageView imgkey;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etemail=findViewById(R.id.etemail);
        etname=findViewById(R.id.etname);
        etpassword=findViewById(R.id.etpassword);
        btnsignup=findViewById(R.id.btnsignup);
        tvsignin=findViewById(R.id.tvsignin);
        imgkey=findViewById(R.id.imgkey);

        auth = FirebaseAuth.getInstance();

        tvsignin.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, SignIn.class));
            finish();
        });
        btnsignup.setOnClickListener(v -> {

            String name = etname.getText().toString();
            String email = etemail.getText().toString();
            String password = etpassword.getText().toString();

            final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
            mDialog.setMessage("Creating Account...");
            mDialog.show();

            if (TextUtils.isEmpty(name)) {
                etname.setError("Enter Full Name");
                etname.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                etemail.setError("Enter E-Mail");
                etemail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etpassword.setError("Enter Password");
                etpassword.requestFocus();
                return;
            }
            if (password.length() < 6) {
                mDialog.dismiss();
                etpassword.setError("Enter Strong Password");

            }
//
//            final ProgressDialog mDialog = new ProgressDialog(Signup.this);
//            mDialog.setMessage("Creating Account...");
//            mDialog.show();

            //create user
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {

                        if (task.isSuccessful()) {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "User created successfully!!" , Toast.LENGTH_SHORT).show();
//                            prefManager.setUserName(name);
                            startActivity(new Intent(SignUp.this, Home.class));
                            finish();
                        }
                        else {
                            Toast.makeText(SignUp.this, "Authentication failed." ,
                                    Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    });
        });
    }
}

