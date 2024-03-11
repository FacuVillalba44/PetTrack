package com.devfacu.pettrack;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.devfacu.pettrack.db.DbVacuna;

import java.util.Calendar;

public class AgregarVacunaActivity extends AppCompatActivity {

    private EditText editTextNombreVacuna;
    private EditText editTextFechaAplicacion;
    private EditText editTextProximaAplicacion;
    private Button btnAgregarVacuna;

    DbVacuna dbVacuna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vacuna);

        // Inicializar vistas
        editTextNombreVacuna = findViewById(R.id.editTextNombreVacuna);
        editTextFechaAplicacion = findViewById(R.id.editTextFechaAplicacion);
        editTextProximaAplicacion = findViewById(R.id.editTextProximaAplicacion);
        btnAgregarVacuna = findViewById(R.id.btnAgregarVacuna);
        editTextFechaAplicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextFechaAplicacion);
            }
        });

        editTextProximaAplicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextProximaAplicacion);
            }
        });

        btnAgregarVacuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarVacuna();
            }
        });
    }

    private void agregarVacuna() {
        String nombreVacuna = editTextNombreVacuna.getText().toString();
        String fechaAplicacion = editTextFechaAplicacion.getText().toString();
        String proximaAplicacion = editTextProximaAplicacion.getText().toString();

        Intent intent = getIntent();
        int id_mascota = intent.getIntExtra("id_mascota", -1);
        String nombre_mascota = intent.getStringExtra("nombre_mascota");

        dbVacuna = new DbVacuna(AgregarVacunaActivity.this);
        SQLiteDatabase db = dbVacuna.getWritableDatabase();

        int id_vacuna = dbVacuna.crearVacuna(nombreVacuna, fechaAplicacion, proximaAplicacion, id_mascota);

        if (id_vacuna > 0) {
            String mensaje = "Vacuna agregada: " +
                    "Nombre: " + nombreVacuna +
                    ", Fecha de Aplicación: " + fechaAplicacion +
                    ", Próxima Aplicación: " + proximaAplicacion;

            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            Intent intentTusVacunas = new Intent(AgregarVacunaActivity.this, VacunasMascotaActivity.class);
            intentTusVacunas.putExtra("id_vacuna", id_vacuna);
            intentTusVacunas.putExtra("id_mascota", id_mascota);
            intentTusVacunas.putExtra("nombre_mascota", nombre_mascota);
            startActivity(intentTusVacunas);
            finish();
        }
        else {
            Toast.makeText(this, "Error al intentar registrar una nueva vacuna", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setOnDateSetListener(new DatePickerFragment.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fecha = dayOfMonth + "-" + (month + 1) + "-" + year;
                editText.setText(fecha);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private OnDateSetListener mListener;

        public void setOnDateSetListener(OnDateSetListener listener) {
            mListener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (mListener != null) {
                mListener.onDateSet(view, year, month, dayOfMonth);
            }
        }

        public interface OnDateSetListener {
            void onDateSet(DatePicker view, int year, int month, int dayOfMonth);
        }
    }
}