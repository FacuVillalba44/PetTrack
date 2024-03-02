package com.devfacu.pettrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class inicioActivity extends AppCompatActivity {
    public Button btnIniciar, btnRegistrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        btnIniciar = findViewById(R.id.btnIniciaSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
    }
    public void iniciaSesion(View view){
        Intent logearse = new Intent(this, LoginActivity.class);
        startActivity(logearse);
        finish();
    }
}