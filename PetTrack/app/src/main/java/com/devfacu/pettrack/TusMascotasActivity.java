package com.devfacu.pettrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devfacu.pettrack.adaptadores.ListaMascotasAdapter;
import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.entidades.Mascota;

import java.util.ArrayList;

public class TusMascotasActivity extends AppCompatActivity {

    private DbMascota dbMascota;
    private ArrayList<Mascota> listaArrayMascotas;
    private ListaMascotasAdapter listaMascotasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tus_mascotas);

        Button botonRegistrarMascota = findViewById(R.id.buttonRegistrarMascota);
        RecyclerView recyclerViewMascotas = findViewById(R.id.recyclerViewMascotas);

        recyclerViewMascotas.setLayoutManager(new LinearLayoutManager(TusMascotasActivity.this));

        dbMascota = new DbMascota(this);
        listaArrayMascotas = new ArrayList<>();

        // Asegúrate de manejar el caso en el que listarMascotasSimplificado() devuelve null
        ArrayList<Mascota> mascotasObtenidas = dbMascota.listarMascotasSimplificado();
        if (mascotasObtenidas != null) {
            listaArrayMascotas.addAll(mascotasObtenidas);
        }

        // Crea una instancia del adaptador y configúralo con la lista de mascotas
        listaMascotasAdapter = new ListaMascotasAdapter(this, listaArrayMascotas);

        // Establece el adaptador en el RecyclerView
        recyclerViewMascotas.setAdapter(listaMascotasAdapter);

        botonRegistrarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TusMascotasActivity.this, RegistrarMascotaActivity.class);
                startActivity(intent);
            }
        });
    }
}