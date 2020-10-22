package com.example.ridetap;

import androidx.appcompat.app.AlertDialog;
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

public class SignIn extends AppCompatActivity {
private EditText etpassword,etemail;
private TextView tvsignup;
private Button btnsignin;
private ImageView imgkey;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etemail=findViewById(R.id.etemail);
        etpassword=findViewById(R.id.etpassword);
        btnsignin=findViewById(R.id.btnsignin);
        tvsignup=findViewById(R.id.tvsignup);
        imgkey=findViewById(R.id.imgkey);
        auth = FirebaseAuth.getInstance();

        tvsignup.setOnClickListener(v -> {
            startActivity(new Intent(SignIn.this, SignUp.class));
            finish();
        });

        btnsignin.setOnClickListener(v -> {

            String email = etemail.getText().toString();
            final String password = etpassword.getText().toString();
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

            final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
            mDialog.setMessage("Signing In...");
            mDialog.show();

            //authenticate user
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignIn.this, task -> {

                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                etpassword.setError("Password too short");
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "Authentication failed, check your email and password or sign up", Toast.LENGTH_LONG).show();
                            }

                        } else {
//                            if(email.equalsIgnoreCase("administrator@gmail.com")){
//                                startActivity(new Intent(SignIn.this, OrdersActivity.class));
//                                finish();}
                            mDialog.dismiss();
                            Intent intent = new Intent(SignIn.this, Home.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        });
    }

//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
//        builder.setTitle(R.string.app_name);
//        builder.setIcon(R.mipmap.ic_launcher);
//        builder.setMessage("Do you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", (dialog, id) -> finish())
//                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
//        AlertDialog alert = builder.create();
//        alert.show();
//
//    }

}
