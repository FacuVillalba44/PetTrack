package com.devfacu.pettrack;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrarDesparasitacionActivity extends AppCompatActivity {
    private static final String TAG = "activity_registrar_desparasitacion";
    private DatePickerDialog.OnDateSetListener fechaAdministrada, siguienteAdministracion;
    EditText marcaDesparasitante;
    Spinner tipoDesparasitante;
    TextView tvfechAdministracion,tvfechProximaAdm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_desparasitacion);
        marcaDesparasitante = findViewById(R.id.etMarcaDesparasitante);
        tipoDesparasitante = findViewById(R.id.spinnerTipoDesparasitante);
        String []tiposDesparasitante={"Comprimido","Gota","Inyectable"};
        ArrayAdapter<String> tiposadapter= new ArrayAdapter<>(RegistrarDesparasitacionActivity.this,R.layout.spinner_custom_commons,tiposDesparasitante);
        tipoDesparasitante.setAdapter(tiposadapter);
        tvfechAdministracion=findViewById(R.id.tvFechaAdministrado);
        tvfechProximaAdm=findViewById(R.id.tvFechaAdministrado2);


    }
}