package com.devfacu.pettrack;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.db.DbVacuna;
import com.devfacu.pettrack.entidades.Vacuna;

import java.util.Calendar;

public class EditarVacunaActivity extends AppCompatActivity {
    private TextView nombre, fecha_aplicacion;

    private EditText proxima_aplicacion;
    private Button botonEditarVacuna, botonEliminarVacuna;
    private DbVacuna dbVacuna;
    private int id_vacuna;
    private int id_mascota;

    private Vacuna vacuna;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_vacuna);
        nombre = findViewById(R.id.textViewNombre);
        fecha_aplicacion = findViewById(R.id.textViewFechaAplicacion);
        proxima_aplicacion = findViewById(R.id.editTextProximaAplicacion);
        botonEditarVacuna = findViewById(R.id.btnEditarVacuna);
        botonEliminarVacuna = findViewById(R.id.btnEliminarVacuna);


        dbVacuna = new DbVacuna(this);
        Intent intent = getIntent();
        String nombreVacuna = intent.getStringExtra("nombre_vacuna");
        String fecha_de_aplicacion = intent.getStringExtra("fecha_aplicacion");
        String prox_aplicacion = intent.getStringExtra("proxima_aplicacion");
        this.id_mascota = intent.getIntExtra("id_mascota", -1);
        this.id_vacuna = intent.getIntExtra("id_vacuna", -1);
        Log.d("EditarMascota", "ID de vacuna a eliminar: " + id_vacuna);

        Vacuna vacuna = dbVacuna.obtenerVacunaPorId(id_vacuna);


        nombre.setText(nombreVacuna);
        fecha_aplicacion.setText(fecha_de_aplicacion);
        proxima_aplicacion.setText(prox_aplicacion);

        proxima_aplicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(proxima_aplicacion);
            }
        });

        botonEditarVacuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbVacuna = new DbVacuna(EditarVacunaActivity.this);

                int id_vacunaEditada = dbVacuna.editarVacuna(id_vacuna, nombreVacuna, fecha_de_aplicacion, proxima_aplicacion.getText().toString(), id_mascota);

                if (id_vacunaEditada > 0) {
                    Toast.makeText(EditarVacunaActivity.this, "Vacuna editada", Toast.LENGTH_LONG).show();

                    Intent volverListaVacunas = new Intent(EditarVacunaActivity.this, TusMascotasActivity.class);
                    volverListaVacunas.putExtra("id_mascota", id_mascota);
                    startActivity(volverListaVacunas);
                } else {
                    Toast.makeText(EditarVacunaActivity.this, "Error al editar vacuna", Toast.LENGTH_LONG).show();
                }
            }
        });

        botonEliminarVacuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vacuna vacuna = dbVacuna.obtenerVacunaPorId(id_vacuna);
                if (vacuna != null) {
                    mostrarDialogoConfirmacionBorrar(vacuna);
                } else {
                    Toast.makeText(EditarVacunaActivity.this, "No se encontró la vacuna", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        private int year, month, day;

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

        public void setInitialDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }
    private void mostrarDialogoConfirmacionBorrar(Vacuna vacuna) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        id_vacuna = vacuna.getId_vacuna();
        Log.d("EditarMascota", "ID de vacuna a eliminar: " + id_vacuna);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta vacuna?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarVacuna(id_vacuna);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void eliminarVacuna(int id_vacuna) {
        DbVacuna dbVacuna = new DbVacuna(EditarVacunaActivity.this);

        if (id_vacuna > 0) {
            int rowsAffected = dbVacuna.eliminarVacuna(id_vacuna);

            if (rowsAffected > 0) {
                Toast.makeText(EditarVacunaActivity.this, "Vacuna eliminada", Toast.LENGTH_SHORT).show();
                Intent volverListaVacunas = new Intent(EditarVacunaActivity.this, vacunas_mascota_Activity.class);
                volverListaVacunas.putExtra("id_mascota", id_mascota);
                startActivity(volverListaVacunas);
            } else {
                Toast.makeText(EditarVacunaActivity.this, "Error al eliminar la vacuna", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(EditarVacunaActivity.this, "Error! El ID de la vacuna es menor a cero", Toast.LENGTH_LONG).show();
        }
    }


}