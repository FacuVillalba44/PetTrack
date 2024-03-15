package com.devfacu.pettrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devfacu.pettrack.adaptadores.ListaVacunasAdapter;
import com.devfacu.pettrack.db.DbVacuna;
import com.devfacu.pettrack.entidades.Vacuna;

import java.util.ArrayList;

public class vacunas_mascota_Activity extends AppCompatActivity {

    private static final int CODIGO_EDICION = 1;
    private ArrayList<Vacuna> listaArrayVacunas;
    private ListaVacunasAdapter listaVacunasAdapter;
    private int id_mascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacunas_mascota);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        id_mascota = intent.getIntExtra("id_mascota", -1);
        Log.d("VacunasMascotaActivity", "ID de mascota en actividad: " + id_mascota);

        // Configuración del RecyclerView
        RecyclerView recyclerViewVacunas = findViewById(R.id.recyclerViewVacunas);
        recyclerViewVacunas.setLayoutManager(new LinearLayoutManager(vacunas_mascota_Activity.this));

        // Inicialización del adaptador
        listaArrayVacunas = new ArrayList<>();
        listaVacunasAdapter = new ListaVacunasAdapter(this, listaArrayVacunas, id_mascota);

        recyclerViewVacunas.setAdapter(listaVacunasAdapter);

        cargarVacunas();

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent volverAHome = new Intent(vacunas_mascota_Activity.this, TusMascotasActivity.class);
                startActivity(volverAHome);
                finish();
            }
        });
    }

    private void cargarVacunas() {
        DbVacuna dbVacuna = new DbVacuna(this);
        ArrayList<Vacuna> vacunasObtenidas = dbVacuna.listarVacunasPorMascota(id_mascota);

        if (vacunasObtenidas != null) {
            listaArrayVacunas.clear();
            listaArrayVacunas.addAll(vacunasObtenidas);
            listaVacunasAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODIGO_EDICION && resultCode == Activity.RESULT_OK) {
            cargarVacunas();
        }
    }
}