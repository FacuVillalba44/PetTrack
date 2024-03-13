package com.devfacu.pettrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
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

        Intent intent = getIntent();
        int id_usuario = intent.getIntExtra("id_usuario", -1);
        Log.d("Home activity", "UsuarioId obtenido: " + id_usuario);

        //ir a pantalla de login
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iniciarSesion = new Intent(inicioActivity.this, LoginActivity.class);
                iniciarSesion.putExtra("id_usuario", id_usuario);

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