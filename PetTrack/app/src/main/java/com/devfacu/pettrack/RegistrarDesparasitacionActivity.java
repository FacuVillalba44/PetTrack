package com.devfacu.pettrack;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Objects;

public class RegistrarDesparasitacionActivity extends AppCompatActivity {

    private static final String TAG = "activity_registrar_desparasitacion";
    private DatePickerDialog.OnDateSetListener fechaAdministrada, siguienteAdministracion;
    EditText marcaDesparasitante;
    Spinner spTipoDesparasitante;
    TextView tvfechAdministracion,tvfechProximaAdm;
    ImageButton imgBtnVolver;
    Button btnRegistrar,btnCancelarRegistro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_desparasitacion);
        imgBtnVolver=findViewById(R.id.imgBtnVolverAHome_D);
        marcaDesparasitante = findViewById(R.id.etMarcaDesparasitante);
        spTipoDesparasitante = findViewById(R.id.spinnerTipoDesparasitante);
        String []tiposDesparasitante={"Comprimido","Gota","Inyectable"};
        ArrayAdapter<String> tiposadapter= new ArrayAdapter<>(RegistrarDesparasitacionActivity.this,R.layout.spinner_custom_commons,tiposDesparasitante);
        spTipoDesparasitante.setAdapter(tiposadapter);
        tvfechAdministracion=findViewById(R.id.tvFechaAdministrado);
        tvfechProximaAdm=findViewById(R.id.tvFechaAdministrado2);
        btnRegistrar=findViewById(R.id.btnRegistraDesparasitacion);
        btnCancelarRegistro=findViewById(R.id.btnCanelarRegistroD);




//________manejo fecha de que se le dio el desparasitante
        tvfechAdministracion.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(
                    RegistrarDesparasitacionActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    fechaAdministrada,
                    year,month,day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        fechaAdministrada = (view, anio, mes, dia) -> {
            mes = mes+1;
            Log.d(TAG,"onDateSet: dd/mm/yyy:" + dia +"/"+ mes + "/" + anio);
            String date = dia +"/"+mes+"/"+anio;//<--aca le doy formato y guardo la fecha
            tvfechAdministracion.setText(date);//<--Cambio el text del textView por la fecha seleccionada
        };
        //________manejo fecha de siguiente desparasitacion
        tvfechProximaAdm.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(
                    RegistrarDesparasitacionActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    siguienteAdministracion,
                    year,month,day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        siguienteAdministracion = (view, anio, mes, dia) -> {
            mes = mes+1;
            Log.d(TAG,"onDateSet: dd/mm/yyy:" + dia +"/"+ mes + "/" + anio);
            String date = dia +"/"+mes+"/"+anio;//<--aca le doy formato y guardo la fecha
            tvfechProximaAdm.setText(date);//<--Cambio el text del textView por la fecha seleccionada
        };

    //_____btn Registrar desparasitación
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarDatos(marcaDesparasitante,tvfechAdministracion,tvfechProximaAdm)){
                    String marcaDesp=  marcaDesparasitante.getText().toString();
                    String motivo=spTipoDesparasitante.getSelectedItem().toString();
                    String fechAdm= tvfechAdministracion.getText().toString();
                    String fechSigAdm= tvfechProximaAdm.getText().toString();
                    Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_LONG).show();
                }
            }
        });

        //_____Boton cancelar
        btnCancelarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent volverAHome = new Intent(RegistrarDesparasitacionActivity.this, Home_Activity.class);
                startActivity(volverAHome);
                finish();
            }
        });


        //_____Inicio de manejo del botón  de la barra de nativa para volver: Obtener el Dispatcher para el botón de "volver"
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finaliza la actividad actual
                setResult(RESULT_OK); // Envía un resultado indicando que se finalizó correctamente
                finish();
            }
        });

    }//<--llave del onCreate
    private boolean validarDatos(EditText marca,TextView fecha1,TextView fecha2) {
        String marcaD= marca.getText().toString().trim();
        String aplicacion = fecha1.getText().toString();
        String proximaAplicacion = fecha2.getText().toString();
        String validarFecha = "Click aquí y Seleccione una fecha";
        if(marcaD.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese una marca", Toast.LENGTH_LONG).show();
            return false;
        } else if (aplicacion.equals(validarFecha)||proximaAplicacion.equals(validarFecha)) {
            Toast.makeText(getApplicationContext(), "Seleccione una fecha", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            // Los campos están completos y correctos
            return true;
        }
    }
}