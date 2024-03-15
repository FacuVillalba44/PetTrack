package com.devfacu.pettrack;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class RegistrarRecordatorioActivity extends AppCompatActivity {
    ImageButton volverAHome;
    private static final String TAG = "activity_agregar_Recordatorio";
    private DatePickerDialog.OnDateSetListener fechaRecordatorio;
    TextView fechaRSeleccion;
    private Spinner spinnerMascotaR,spinnerMotivoR;
    private Button btnRegistrarRecordatorio, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_recordatorios);

        volverAHome=findViewById(R.id.imgBtnVolverAHome_R);
        btnRegistrarRecordatorio=findViewById(R.id.btnAgregar_N_R);
        btnCancelar=findViewById(R.id.btnCanelar_N_R);
        fechaRSeleccion=findViewById(R.id.tvFechaRecordatorio);
    //carga los motivos que se guardan en la bd
        spinnerMotivoR=findViewById(R.id.spinnerMotivoR);
        String []motivo={"Desparasitasión","Vacunación"};
        ArrayAdapter<String> motivosadapter= new ArrayAdapter<>(RegistrarRecordatorioActivity.this,R.layout.spinner_custom_commons,motivo);
        spinnerMotivoR.setAdapter(motivosadapter);
    //manejar spinner mascota con los nombres de las mascotas registradas
        spinnerMascotaR=findViewById(R.id.spinnerMascotaR);

    // Logica para recuperar el usuario al ingresar a la app
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.myapp.PREFERENCES", Context.MODE_PRIVATE);
        int id_usuario_guardado = sharedPreferences.getInt("id_usuario", -1);

        if (id_usuario_guardado != -1) {
            // El ID de usuario se recuperó correctamente
            Log.d("Home_Activity", "ID de usuario recuperado de las preferencias: " + id_usuario_guardado);
        } else {
            // No se pudo recuperar el ID de usuario de las preferencias
            Log.d("Home_Activity", "No se pudo recuperar el ID de usuario de las preferencias");
        }


    //______ImageButton para volver a home
        volverAHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volverAHome = new Intent(RegistrarRecordatorioActivity.this, Home_Activity.class);
                startActivity(volverAHome);
                finish();
            }
        });

    //_______Manejo de calendario-____

        fechaRSeleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        RegistrarRecordatorioActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        fechaRecordatorio,
                        year,month,day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        fechaRecordatorio = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                mes = mes+1;
                Log.d(TAG,"onDateSet: dd/mm/yyy:" + dia +"/"+ mes + "/" + anio);
                String date = dia +"/"+mes+"/"+anio;
                fechaRSeleccion.setText(date);//<esta variable tiene la fecha y la muestra
            }
        };


    //_______Boton registrar recordatorio
        btnRegistrarRecordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarRecordatorio(fechaRSeleccion)){
                    String fechaElegida=fechaRSeleccion.getText().toString();
                    String motivo=spinnerMotivoR.getSelectedItem().toString();
                //    String mascotaAplicada= spinnerMascotaR.getSelectedItem().toString();
                    Intent volverAHome = new Intent(RegistrarRecordatorioActivity.this, Home_Activity.class);
                    Toast.makeText(getApplicationContext(), "Recordatorio guardado correctamente", Toast.LENGTH_LONG).show();
                    startActivity(volverAHome);

                }
            }
        });

    //_____Boton cancelar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent volverAHome = new Intent(RegistrarRecordatorioActivity.this, Home_Activity.class);
                startActivity(volverAHome);
                finish();
            }
        });

    //_____Inicio de manejo del botón  de la barra de nativa para volver: Obtener el Dispatcher para el botón de "volver"
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
    //_____Registrar un callback para manejar el botón de "volver"
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent volverAHome = new Intent(RegistrarRecordatorioActivity.this, Home_Activity.class);
                startActivity(volverAHome);
                finish();
            }
        });

    }//<Cierre de onCreate

 //______Función externa que se llama para validar datos
    private boolean validarRecordatorio(TextView fechaRecordatorio) {
        String recordatorio = fechaRecordatorio.getText().toString();
        String validarRecordatorio = "Click aquí y Seleccione una fecha";
        if (recordatorio.equals(validarRecordatorio)) {
            Toast.makeText(getApplicationContext(), "Seleccione una fecha", Toast.LENGTH_LONG).show();
            return false;
        } else {
            // Los campos están completos y correctos
            return true;
        }
    }


}