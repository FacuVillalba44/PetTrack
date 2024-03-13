package com.devfacu.pettrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.entidades.Mascota;


public class PerfilMascotaActivity extends AppCompatActivity {
    Button btnEditar, btnVacunaN,btnDesparasitacionN,btnVerVacunas,btnVerDesparasitaciones;
    TextView  nombre,nacimiento,sexo;
    ImageButton btnVolver;
    ImageView fotoPerfil;
    int id_mascota;
    Mascota mascota;
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
        btnVerVacunas=findViewById(R.id.btn_ver_vacunas_P);
        btnVerDesparasitaciones=findViewById(R.id.btn_ver_desparasitaciones_P);

        Intent intent= getIntent();
        id_mascota = intent.getIntExtra("id_mascota", -1);
        byte[] imgBytes = intent.getByteArrayExtra("imagen_blob");

        if (id_mascota != -1){
            DbMascota dbMascota = new DbMascota(this);
            mascota = dbMascota.verDetallesMascota(id_mascota);

            if (mascota != null){
                nombre.setText(mascota.getNombre());
                nacimiento.setText("Fecha Nacimiento "+mascota.getFecha_nacimiento());
                sexo.setText("Sexo "+mascota.getSexo());

                int id_ususario = mascota.getId_usuario();

                if (imgBytes != null && imgBytes.length > 0) {
                    cargarImagenDesdeBlob(imgBytes);
                } else {
                    fotoPerfil.setImageResource(R.drawable.placeholder);
                }
            }
        }



        //<----btn editar perfil de mascota
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent editarMascota = new Intent(PerfilMascotaActivity.this, EditarMAscotaActivity.class);
//                editarMascota.putExtra("id_mascota", id_mascota);

            }
        });
        //<-----btn Registrar vacuna
        btnVacunaN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vacunaN = new Intent(PerfilMascotaActivity.this, RegistrarVacunacionActivity.class);
                vacunaN.putExtra("id_mascota", id_mascota);
                String nombreMascota = mascota.getNombre();
                vacunaN.putExtra("nombre_mascota", nombreMascota);
                finish();
                startActivity(vacunaN);
            }
        });
        //<----btn Registrar desparasitación
        btnDesparasitacionN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent desparasitacionN = new Intent(PerfilMascotaActivity.this, RegistrarDesparasitacionActivity.class);
                desparasitacionN.putExtra("id_mascota", id_mascota);
                finish();
                startActivity(desparasitacionN);
            }
        });
        //<---btn ver vacunas
        btnVerVacunas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verVacunas = new Intent(PerfilMascotaActivity.this, vacunas_mascota_Activity.class);
                verVacunas.putExtra("id_mascota", id_mascota);
                startActivity(verVacunas);
                finish();
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

    private void cargarImagenDesdeBlob(byte[] imgBytes) {
        try {
            Glide.with(this)
                    .load(imgBytes)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(fotoPerfil);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PerfilMascotaActivity", "Error al cargar la imagen desde BLOB con Glide: " + e.getMessage());
        }

    }
}