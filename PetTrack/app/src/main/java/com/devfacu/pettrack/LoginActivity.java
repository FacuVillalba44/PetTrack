package com.devfacu.pettrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void iniciarSesion(View view){
        Intent logearse = new Intent(this, Home_Activity.class);
        finish();
        startActivity(logearse);
    }
    public void volverAInicio(View view){
        Intent volver = new Intent(this, inicioActivity.class);
        finish();
        startActivity(volver);
    }
}