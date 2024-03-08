package com.devfacu.pettrack;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
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

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class RegistrarRecordatorioActivity extends AppCompatActivity {
    ImageButton volverAHome;
    private static final String TAG = "activity_agregar_Recordatorio";
    private DatePickerDialog.OnDateSetListener fechaRecordatorio;
    private TextView fechaRSeleccion;
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
                String fechaElegida=fechaRSeleccion.getText().toString();
                String motivo=spinnerMotivoR.getSelectedItem().toString();

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




}