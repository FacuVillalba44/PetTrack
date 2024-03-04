package com.devfacu.pettrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void salir(View view) {
        Intent salir = new Intent(this, inicioActivity.class);
        finish();
        startActivity(salir);
    }

    public void menuMascotas(View view) {
        Intent menuMascotas = new Intent(this, MenuMascotasActivity.class);
        finish();
        startActivity(menuMascotas);
    }

}