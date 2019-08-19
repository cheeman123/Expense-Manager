package com.shashank.expensemanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText email,password;
    Button login;
    FirebaseAuth mAuth;
    Spinner spinner;
    String[] cc =  {"Status","Married","Single"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.register);
        spinner = findViewById(R.id.status);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(Register.this, cc[i].toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,cc);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(email.getText().toString()))
                {
                    Toast.makeText(Register.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                }else
                {
                    if (TextUtils.isEmpty(password.getText().toString()))
                    {
                        Toast.makeText(Register.this, "Enter your Password", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        ProgressDialog progressDialog = new ProgressDialog(Register.this);
                        progressDialog.setMessage("Loging in");
                        progressDialog.show();

                        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (task.isSuccessful())
                                {
                                    Intent intent = new Intent(Register.this,HomeActivity.class);
                                    startActivity(intent);

                                }else
                                {
                                    Toast.makeText(Register.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }) ;
                    }
                }
            }
        });



    }
    }