package com.devfacu.pettrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.devfacu.pettrack.db.BaseDeDatos;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonTusMascotas = findViewById(R.id.btn_tus_mascotas);
        Button buttonTusRecordatorios = findViewById(R.id.btn_recordatorio);
        Button buttonAgregarMascota = findViewById(R.id.btn_agregar_mascota);
        Button buttonPerfil = findViewById(R.id.button_perfil);

        Intent intent = getIntent();
        long idUsuario = intent.getLongExtra("id_usuario", -1);
        Log.d("MainActivity", "Valor de idUsuario: " + idUsuario);
        Log.d("MainActivity", "Después de obtener idUsuario: " + idUsuario);


            buttonPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
                    intent.putExtra("id_usuario", idUsuario);
                    Log.d("MainActivity", "antes de ir a perfil: " + idUsuario);
                    startActivity(intent);
                }
            });

            buttonTusMascotas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, TusMascotasActivity.class);
                    intent.putExtra("id_usuario", idUsuario);
                    startActivity(intent);
                }
            });

            buttonTusRecordatorios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, RecordatoriosActivity.class);
                    intent.putExtra("id_usuario", idUsuario);
                    startActivity(intent);
                }
            });

            buttonAgregarMascota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AgregarMascotaActivity.class);
                    intent.putExtra("id_usuario", idUsuario);
                    startActivity(intent);
                }
            });
        }
    }
