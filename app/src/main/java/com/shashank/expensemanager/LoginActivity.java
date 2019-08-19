package com.shashank.expensemanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    EditText email,password;
    Button login;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(email.getText().toString()))
                {
                    Toast.makeText(LoginActivity.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                }else
                    {
                        if (TextUtils.isEmpty(password.getText().toString()))
                        {
                            Toast.makeText(LoginActivity.this, "Enter your Password", Toast.LENGTH_SHORT).show();
                        }else
                            {
                                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                                progressDialog.setMessage("Loging in");
                                progressDialog.show();

                               mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                   @Override
                                   public void onComplete(Task<AuthResult> task) {


                                       if (task.isSuccessful())
                                       {
                                           Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                           startActivity(intent);

                                       }else
                                           {
                                               Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                           }

                                   }
                               }) ;
                            }
                    }
            }
        });

    }

    public void onregister(View view)
    {
        Intent intent = new Intent(LoginActivity.this,Register.class);
        startActivity(intent);
    }
}
