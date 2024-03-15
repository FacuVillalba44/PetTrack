package com.devfacu.pettrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devfacu.pettrack.adaptadores.ListaMascotasAdapter;
import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.entidades.Mascota;

import java.util.ArrayList;

public class TusMascotasActivity extends AppCompatActivity {

    private static final int TU_CODIGO_DE_EDICION = 1;
    private ArrayList<Mascota> listaArrayMascotas;
    private ListaMascotasAdapter listaMascotasAdapter;
    private ImageButton volverAHome_TM;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tus_mascotas);
        volverAHome_TM=findViewById(R.id.imgBtnVolver_TM);
        // Cargar la lista de mascotas
        cargarListaMascotas();



        volverAHome_TM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volver = new Intent(TusMascotasActivity.this, Home_Activity.class);
                startActivity(volver);
                finish();
            }
        });

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent volverAHome = new Intent(TusMascotasActivity.this, Home_Activity.class);
                startActivity(volverAHome);
                finish();
            }
        });

    }

    // Actualizar la lista
    private void cargarListaMascotas() {
        listaArrayMascotas = new ArrayList<>();

        DbMascota dbMascota = new DbMascota(this);
        ArrayList<Mascota> mascotasObtenidas = dbMascota.listarMascotas();

        if (mascotasObtenidas != null) {
            listaArrayMascotas.addAll(mascotasObtenidas);
        }

        // Nuevo adapter
        listaMascotasAdapter = new ListaMascotasAdapter(this, listaArrayMascotas);

        RecyclerView recyclerViewMascotas = findViewById(R.id.recyclerViewMascotas);
        recyclerViewMascotas.setLayoutManager(new LinearLayoutManager(TusMascotasActivity.this));
        recyclerViewMascotas.setAdapter(listaMascotasAdapter);
    }

    // Manejo de resultados después de la edición
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TU_CODIGO_DE_EDICION && resultCode == RESULT_OK) {
            cargarListaMascotas();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarListaMascotas();
    }
}