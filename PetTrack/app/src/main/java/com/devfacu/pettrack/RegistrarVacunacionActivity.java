package com.devfacu.pettrack;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class RegistrarVacunacionActivity extends AppCompatActivity {
    private static final String TAG = "activity_registrar_vacunacion";
    private DatePickerDialog.OnDateSetListener fechaAplicado, siguienteAplicacion;
    TextView fechaAplicacion, proximaAplicacion;
    Button registrarVacuna,cancelarRegistro;
    EditText nombreVacuna;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registrar_vacunacion);
        nombreVacuna = findViewById(R.id.etNombreVacuna);
        registrarVacuna = findViewById(R.id.btnRegistraVacuna);
        cancelarRegistro=findViewById(R.id.btnCanelarRegistroV);
        fechaAplicacion = findViewById(R.id.tvFechaAplicacion);
        proximaAplicacion=findViewById(R.id.tvFechaAplicacion2);


    //________manejo fecha de aplicación de vacuna
        fechaAplicacion.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(
                    RegistrarVacunacionActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    fechaAplicado,
                    year,month,day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        fechaAplicado = (view, anio, mes, dia) -> {
            mes = mes+1;
            Log.d(TAG,"onDateSet: dd/mm/yyy:" + dia +"/"+ mes + "/" + anio);
            String date = dia +"/"+mes+"/"+anio;//<--aca le doy formato y guardo la fecha
            fechaAplicacion.setText(date);//<--Cambio el text del textView por la fecha seleccionada
        };
        //________manejo fecha de siguiente vacuna
        proximaAplicacion.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(
                    RegistrarVacunacionActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    siguienteAplicacion,
                    year,month,day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        siguienteAplicacion = (view, anio, mes, dia) -> {
            mes = mes+1;
            Log.d(TAG,"onDateSet: dd/mm/yyy:" + dia +"/"+ mes + "/" + anio);
            String date = dia +"/"+mes+"/"+anio;//<--aca le doy formato y guardo la fecha
            proximaAplicacion.setText(date);//<--Cambio el text del textView por la fecha seleccionada
        };

        registrarVacuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFechas(fechaAplicacion,proximaAplicacion)){
                    String fechaAplicada=fechaAplicacion.getText().toString();
                    String siguienteAplicacion=proximaAplicacion.getText().toString();
                    String nombreV=nombreVacuna.getText().toString();

                    //Intent volverAHome = new Intent(RegistrarRecordatorioActivity.this, Home_Activity.class);
                    Toast.makeText(getApplicationContext(), "Registro guardado correctamente", Toast.LENGTH_LONG).show();
                    //startActivity(volverAHome);

                }
            }
        });

        //_____Inicio de manejo del botón  de la barra de nativa para volver
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent volverAHome = new Intent(RegistrarVacunacionActivity.this, Home_Activity.class);
                startActivity(volverAHome);
                finish();
            }
        });

    }//<--llave onCreate

    private boolean validarFechas(TextView fecha1,TextView fecha2) {
        String aplicacion = fecha1.getText().toString();
        String proximaAplicacion = fecha2.getText().toString();
        String validarFecha = "Click aquí y Seleccione una fecha";
        if (aplicacion.equals(validarFecha)||proximaAplicacion.equals(validarFecha)) {
            Toast.makeText(getApplicationContext(), "Seleccione una fecha", Toast.LENGTH_LONG).show();
            return false;
        } else {
            // Los campos están completos y correctos
            return true;
        }
    }

}