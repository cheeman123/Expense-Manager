package com.shashank.expensemanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shashank.expensemanager.HomeActivity;
import com.shashank.expensemanager.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
        // close splash activity
        finish();
    }
}
