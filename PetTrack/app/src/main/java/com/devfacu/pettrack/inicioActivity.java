package com.devfacu.pettrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class inicioActivity extends AppCompatActivity {
    Button btnIniciar, btnRegistrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        btnIniciar = findViewById(R.id.btnIniciaSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        //ir a pantalla de login
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iniciarSesion = new Intent(inicioActivity.this, LoginActivity.class);
                startActivity(iniciarSesion);
            }
        });

        //ir a pantalla de login
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrarse = new Intent(inicioActivity.this, RegistrarseActivity.class);
                startActivity(registrarse);
            }
        });


    }//<llave de onCreate

}