package com.devfacu.pettrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devfacu.pettrack.adaptadores.ListaMascotasAdapter;
import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.entidades.Mascota;

import java.util.ArrayList;

public class TusMascotasActivity extends AppCompatActivity {

    private static final int TU_CODIGO_DE_EDICION = 1; // Puedes elegir cualquier número aquí
    private ArrayList<Mascota> listaArrayMascotas;
    private ListaMascotasAdapter listaMascotasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tus_mascotas);

        // Cargar la lista de mascotas
        cargarListaMascotas();

        Button botonRegistrarMascota = findViewById(R.id.buttonRegistrarMascota);

        botonRegistrarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                int idUsuario = intent.getIntExtra("id_usuario", -1);

                Intent intent1 = new Intent(TusMascotasActivity.this, RegistrarMascotaActivity.class);
                intent1.putExtra("id_usuario", idUsuario);
                startActivity(intent1);
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







//package com.devfacu.pettrack;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.devfacu.pettrack.adaptadores.ListaMascotasAdapter;
//import com.devfacu.pettrack.db.DbMascota;
//import com.devfacu.pettrack.entidades.Mascota;
//
//import java.util.ArrayList;
//
//public class TusMascotasActivity extends AppCompatActivity {
//
//    private ArrayList<Mascota> listaArrayMascotas;
//    private ListaMascotasAdapter listaMascotasAdapter;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tus_mascotas);
//
////        Intent intent = new Intent();
////        intent.getIntExtra("id_usuario", id_usuario);
//
//        // Cargar la lista de mascotas
//        cargarListaMascotas();
//
//        Button botonRegistrarMascota = findViewById(R.id.buttonRegistrarMascota);
//        //RecyclerView recyclerViewMascotas = findViewById(R.id.recyclerViewMascotas);
//
//        botonRegistrarMascota.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = getIntent();
//                int idUsuario = intent.getIntExtra("id_usuario", -1);
//                Intent intent1 = new Intent(TusMascotasActivity.this, RegistrarMascotaActivity.class);
//                intent1.putExtra("id_usuario", idUsuario);
//                startActivity(intent1);
//            }
//        });
//    }
//
//    // Actualizar la lista
//    private void cargarListaMascotas() {
//        listaArrayMascotas = new ArrayList<>();
//
//        DbMascota dbMascota = new DbMascota(this);
//        ArrayList<Mascota> mascotasObtenidas = dbMascota.listarMascotas();
//
//        if (mascotasObtenidas != null) {
//            listaArrayMascotas.addAll(mascotasObtenidas);
//        }
//
//        // Nuevo adapter
//        listaMascotasAdapter = new ListaMascotasAdapter(this, listaArrayMascotas);
//
//        RecyclerView recyclerViewMascotas = findViewById(R.id.recyclerViewMascotas);
//        recyclerViewMascotas.setLayoutManager(new LinearLayoutManager(TusMascotasActivity.this));
//        recyclerViewMascotas.setAdapter(listaMascotasAdapter);
//    }
//}

























//package com.devfacu.pettrack;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import com.devfacu.pettrack.adaptadores.ListaMascotasAdapter;
//import com.devfacu.pettrack.db.DbMascota;
//import com.devfacu.pettrack.entidades.Mascota;
//
//import java.util.ArrayList;
//
//public class TusMascotasActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tus_mascotas);
//
//        Button botonRegistrarMascota = findViewById(R.id.buttonRegistrarMascota);
//        RecyclerView recyclerViewMascotas = findViewById(R.id.recyclerViewMascotas);
//
//        recyclerViewMascotas.setLayoutManager(new LinearLayoutManager(TusMascotasActivity.this));
//
//        DbMascota dbMascota = new DbMascota(this);
//        ArrayList<Mascota> listaArrayMascotas = new ArrayList<>();
//
//        ArrayList<Mascota> mascotasObtenidas = dbMascota.listarMascotas();
//        if (mascotasObtenidas != null) {
//            listaArrayMascotas.addAll(mascotasObtenidas);
//        }
//        ListaMascotasAdapter listaMascotasAdapter = new ListaMascotasAdapter(this, listaArrayMascotas);
//
//        // Establece el adaptador en el RecyclerView
//        recyclerViewMascotas.setAdapter(listaMascotasAdapter);
//
//        botonRegistrarMascota.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TusMascotasActivity.this, RegistrarMascotaActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//}