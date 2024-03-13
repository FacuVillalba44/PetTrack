package com.devfacu.pettrack;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

public class Home_Activity extends AppCompatActivity {
    Button registrarRecord,agregarMascota_H,recordatorios_H, verMascotas;
    ImageButton CerrarSesion;
    private int id_usuario;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CerrarSesion = findViewById(R.id.imgBtnCerrarSesion);
        agregarMascota_H = findViewById(R.id.btnRegistrarMascota_H);
        registrarRecord = findViewById(R.id.btnN_R_H);
        verMascotas= findViewById(R.id.btnVerMascotas);

        Intent intent = getIntent();
        id_usuario = intent.getIntExtra("id_ususario", -1);
        Log.d("Home", "UsuarioId obtenido: " + id_usuario);

    //______btn para cerrar sesion
        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Home_Activity.this)
                        .setMessage("¿Estás seguro de que quieres volver cerrar sesion?")
                        .setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Si el usuario confirma, cierra la actividad actual
                                Intent salir = new Intent(Home_Activity.this, LoginActivity.class);
                                finish();
                                startActivity(salir);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Si el usuario cancela, no hace nada
                                dialog.cancel();
                            }
                            // Agrega la implementación del método cancel()
                            public void cancel() {
                                // no realizar ninguna acción específica pero debe estar
                            }
                        })
                        .show();
            }
        });
    //______btn agregar mascota
        agregarMascota_H.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent nuevaMascota = new Intent(Home_Activity.this,RegistrarMascotaActivity.class);
            nuevaMascota.putExtra("id_usuario", id_usuario);
            Log.d("Home", "UsuarioId obtenido: " + id_usuario);
            startActivity(nuevaMascota);
        }
    });
//________btn registrar recordatorio
        registrarRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoRecordatorio= new Intent(Home_Activity.this,RegistrarRecordatorioActivity.class);
                nuevoRecordatorio.putExtra("id_usuario", id_usuario);
                startActivity(nuevoRecordatorio);
        }
    });
        verMascotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verMascotasIntent = new Intent(Home_Activity.this, TusMascotasActivity.class);
                verMascotasIntent.putExtra("id_usuario", id_usuario);
                startActivity(verMascotasIntent);
            }
        });



/*---------------Inicio metodo de manejo del botón del celu--------------------
          obtener el DisPatcher */
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        // Registrar un callback para manejar el botón de "volver"
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(Home_Activity.this)
                        .setMessage("¿Estás seguro de que quieres volver cerrar sesion?")
                        .setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Si el usuario confirma, cierra la actividad actual
                                Intent salir = new Intent(Home_Activity.this, LoginActivity.class);
                                finish();
                                startActivity(salir);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Si el usuario cancela, no hace nada
                                dialog.cancel();
                            }
                            // Agrega la implementación del método cancel()
                            public void cancel() {
                                // no realizar ninguna acción específica pero debe estar
                            }
                        })
                        .show();
            }
        });


    }//<cierre onCreate
}