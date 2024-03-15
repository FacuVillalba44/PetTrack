package com.devfacu.pettrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

import com.devfacu.pettrack.db.BaseDeDatos;
import com.devfacu.pettrack.db.DbUsuario;

public class inicioActivity extends AppCompatActivity {
    Button btnIniciar, btnRegistrarse;
    DbUsuario dbUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        btnIniciar = findViewById(R.id.btnIniciaSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        DbUsuario db = new DbUsuario(this);
        SQLiteDatabase basededatos = db.getWritableDatabase();


        Intent intent = getIntent();
        int id_usuario = intent.getIntExtra("id_usuario", -1);



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