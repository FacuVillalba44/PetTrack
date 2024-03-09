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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class RegistrarMascotaActivity extends AppCompatActivity {

    private static final String TAG = "activity_registrar_macota";
    private TextView fechaNacimiento;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RadioButton rMacho,rHembra;
    private EditText nombreMascota, razaMascota;
    private Spinner spinnerEspecie;
    private Button registrarMascota;
    private RadioGroup rGroupSex;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_mascota);
    //_____________Declaración de variables__________________________________
        nombreMascota = findViewById(R.id.etNombreMascota);

        spinnerEspecie = findViewById(R.id.spinnerEspecie);
        String []especies={"Canina","Felina"};
        ArrayAdapter<String> especiesadapter= new ArrayAdapter<>(RegistrarMascotaActivity.this,R.layout.spinner_custom_commons,especies);
        spinnerEspecie.setAdapter(especiesadapter);
        razaMascota = findViewById(R.id.etRazaMascota);
        fechaNacimiento = findViewById(R.id.tvFechaNacimiento);//<variable que castea la fecha

        rGroupSex=findViewById(R.id.radioGroupSex);
        rHembra=findViewById(R.id.rBtnHembra);
        rHembra=findViewById(R.id.rBtnMacho);

        registrarMascota=findViewById(R.id.btnRegistrarMascota_F);


//__________________________Inicio de calendario______________________________________
        fechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        RegistrarMascotaActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int dia, int mes, int anio) {
                mes = mes+1;
                Log.d(TAG,"onDateSet: dd/mm/yyy:" + dia +"/"+ mes + "/" + anio);
                String date = dia + "/" +mes+ "/" +anio;
                fechaNacimiento.setText(date);//<esta variable tiene la fecha
            }
        };
//__________________________Fin de calendario______________________________________
        //_____________Boton de registrar mascota
        registrarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarDatosMascota(nombreMascota, razaMascota,fechaNacimiento)){
                    String nombreM = nombreMascota.getText().toString();
                    String fechaNac = fechaNacimiento.getText().toString();
                    String especieM = spinnerEspecie.getSelectedItem().toString();

                    int selectedId = rGroupSex.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String sexo = selectedRadioButton.getText().toString();

                    Toast.makeText(getApplicationContext(), "Datos guardados", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Fallo al guardar datos", Toast.LENGTH_LONG).show();
                }
            }
        });






        //_____Inicio de manejo del botón  de la barra de nativa para volver: Obtener el Dispatcher para el botón de "volver"
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        //_____Registrar un callback para manejar el botón de "volver"
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent volverAHome = new Intent(RegistrarMascotaActivity.this, Home_Activity.class);
                startActivity(volverAHome);
                finish();
            }
        });

        
    }//<llave onCreate
    private boolean validarDatosMascota(EditText nombreMascota, EditText RazaMascota,TextView nacimiento) {
        String NombreMascota = nombreMascota.getText().toString().trim();
        String razaMascota = RazaMascota.getText().toString().trim();
        String Nacimiento = nacimiento.getText().toString();
        String validarNacimiento = "Click aquí y Seleccione una fecha";
        if (NombreMascota.isEmpty()) {
            // Mostrar mensaje de error
            Toast.makeText(getApplicationContext(), "Por favor, complete el nombre", Toast.LENGTH_LONG).show();
            return false;
        } else if (razaMascota.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, complete el campo de raza", Toast.LENGTH_LONG).show();
            return false;
        } else if (Nacimiento.equals(validarNacimiento)) {
            Toast.makeText(getApplicationContext(), "Seleccione una fecha", Toast.LENGTH_LONG).show();
            return false;
        } else {
            // Los campos están completos y correctos
            return true;
        }
    }// < Llave cierre validarMascotas
}




