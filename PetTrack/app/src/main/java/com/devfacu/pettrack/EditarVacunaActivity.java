package com.devfacu.pettrack;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class EditarVacunaActivity extends AppCompatActivity {

    private TextView nombre_vacuna;
    private TextView fecha_aplicacion;
    private EditText proxima_aplicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_vacuna);

        nombre_vacuna = findViewById(R.id.textViewNombreVacuna);
        fecha_aplicacion = findViewById(R.id.textViewFechaAplicacion);
        proxima_aplicacion = findViewById(R.id.editTextProximaAplicacion);

        Intent intent = getIntent();
        String nombre_de_vacuna = intent.getStringExtra("nombre_vacuna");
        String fecha_de_aplicacion = intent.getStringExtra("fecha_aplicacion");
        String prox_aplicacion = intent.getStringExtra("proxima_aplicacion");

        nombre_vacuna.setText(nombre_de_vacuna);
        fecha_aplicacion.setText(fecha_de_aplicacion);
        proxima_aplicacion.setText(prox_aplicacion);

        proxima_aplicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(proxima_aplicacion);
            }
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = new DatePickerFragment();

        // Obtener la fecha actual seleccionada en el EditText
        String fechaSeleccionada = editText.getText().toString();
        if (!fechaSeleccionada.isEmpty()) {
            String[] fechaArray = fechaSeleccionada.split("-");
            int year = Integer.parseInt(fechaArray[2]);
            int month = Integer.parseInt(fechaArray[1]) - 1;
            int day = Integer.parseInt(fechaArray[0]);

            newFragment.setInitialDate(year, month, day);
        }

        newFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fecha = dayOfMonth + "-" + (month + 1) + "-" + year;
                editText.setText(fecha);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment {
        private DatePickerDialog.OnDateSetListener mListener;
        private int year, month, day;

        public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
            mListener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();

            // Obtener la fecha actual seleccionada en el EditText (si existe)
            if (year == 0 && month == 0 && day == 0) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }

            return new DatePickerDialog(getActivity(), mListener, year, month, day);
        }

        // Agrega este método para actualizar la fecha seleccionada
        public void setInitialDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }
}