package com.devfacu.pettrack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.devfacu.pettrack.adaptadores.ListaVacunasAdapter;
import com.devfacu.pettrack.db.DbVacuna;
import com.devfacu.pettrack.entidades.Vacuna;
import java.util.ArrayList;

public class VacunasMascotaActivity extends AppCompatActivity implements ListaVacunasAdapter.OnVacunaEliminadaListener {

    private static final int CODIGO_EDICION = 1;
    private ArrayList<Vacuna> listaArrayVacunas;
    private ListaVacunasAdapter listaVacunasAdapter;
    private int id_mascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacunas_mascota);

        Button botonAgregarVacuna = findViewById(R.id.btnAgregarVacuna);

        Intent intent = getIntent();
        id_mascota = intent.getIntExtra("id_mascota", -1);
        Log.d("VacunasMascotaActivity", "ID de mascota en actividad: " + id_mascota);

        botonAgregarVacuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacunasMascotaActivity.this, AgregarVacunaActivity.class);
                intent.putExtra("id_mascota", id_mascota);
                startActivity(intent);
            }
        });
        // Configuración del RecyclerView
        RecyclerView recyclerViewVacunas = findViewById(R.id.recyclerViewVacunas);
        recyclerViewVacunas.setLayoutManager(new LinearLayoutManager(VacunasMascotaActivity.this));

        // Inicialización del adaptador
        listaArrayVacunas = new ArrayList<>();
        listaVacunasAdapter = new ListaVacunasAdapter(this, listaArrayVacunas, id_mascota);
        listaVacunasAdapter.setOnVacunaEliminadaListener(this);

        recyclerViewVacunas.setAdapter(listaVacunasAdapter);

        cargarVacunas();
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
    public void onVacunaEliminada() {
        cargarVacunas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODIGO_EDICION && resultCode == RESULT_OK) {
            cargarVacunas();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarVacunas();
    }
}