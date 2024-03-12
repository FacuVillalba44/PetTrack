package com.devfacu.pettrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;


public class PerfilMascotaActivity extends AppCompatActivity {
    Button btnEditar, btnVacunaN,btnDesparasitacionN,btnVerVacunas,btnVerDesparasitaciones;
    TextView  nombre,nacimiento,sexo;
    ImageButton btnVolver;
    ImageView fotoPerfil;
    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);
        btnVolver=findViewById(R.id.imgBtnVolver_P);
        fotoPerfil=findViewById(R.id.foto_perfil_P);
        btnEditar=findViewById(R.id.btn_editar_P);
        nombre=findViewById(R.id.tvNombre_P);
        nacimiento=findViewById(R.id.tvNacimiento_P);
        sexo=findViewById(R.id.tvSexo_P);
        btnVacunaN=findViewById(R.id.btn_nueva_vacuna_P);
        btnDesparasitacionN=findViewById(R.id.btn_nueva_desparasitacion_P);
        btnVerVacunas=findViewById(R.id.btn_ver_desparasitaciones_P);
        btnVerDesparasitaciones=findViewById(R.id.btn_ver_desparasitaciones_P);

        //<----btn editar perfil de mascota
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //<-----btn Registrar vacuna
        btnVacunaN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vacunaN = new Intent(PerfilMascotaActivity.this, RegistrarVacunacionActivity.class);
                finish();
                startActivity(vacunaN);
            }
        });
        //<----btn Registrar desparasitación
        btnDesparasitacionN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent desparasitacionN = new Intent(PerfilMascotaActivity.this, RegistrarDesparasitacionActivity.class);
                finish();
                startActivity(desparasitacionN);
            }
        });
        //<---btn ver vacunas
        btnVerVacunas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //<---btn ver desparasitaciones
        btnVerDesparasitaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //<---manejo del boton "volver" nativo
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent volverAHome = new Intent(PerfilMascotaActivity.this, inicioActivity.class);
                startActivity(volverAHome);
                finish();
            }
        });

    }//<--llave onCreate
}