package com.devfacu.pettrack;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.devfacu.pettrack.db.DbMascota;

import java.util.Calendar;

public class RegistrarMascotaActivity extends AppCompatActivity {

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editTextNombreMascota;
    private EditText editTextFechaNacimiento;
    private EditText editTextEspecie;
    private EditText editTextRaza;
    private RadioGroup radioGroupSex;
    private RadioButton radioMacho;
    private RadioButton radioHembra;
    private Button botonSeleccionImagen;
    private Button botonCamara;
    private ImageView imageViewFoto;
    private Button botonGuardarCambios;
    private DbMascota dbMascota;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> imagenLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    Log.d("RegistrarMascota", "URI de la imagen seleccionada: " + selectedImage);
                    imageViewFoto.setImageURI(selectedImage);
                    selectedImageUri = selectedImage;
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_mascota);

        editTextNombreMascota = findViewById(R.id.editTextNombreMascota);
        editTextFechaNacimiento = findViewById(R.id.editTextFecNac);
        editTextEspecie = findViewById(R.id.editTextEspecie);
        editTextRaza = findViewById(R.id.editTextRaza);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        radioMacho = findViewById(R.id.radioMacho);
        radioHembra = findViewById(R.id.radioHembra);
        botonSeleccionImagen = findViewById(R.id.btnSeleccionarImagen);
        botonCamara = findViewById(R.id.btnCamara);
        imageViewFoto = findViewById(R.id.imageViewFoto);
        botonGuardarCambios = findViewById(R.id.buttonGuardarCambios);
        Intent intent = getIntent();
        editTextFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        botonSeleccionImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagenLauncher.launch(intent);
            }
        });
        botonGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreMascota = editTextNombreMascota.getText().toString();
                String fechaNacimiento = editTextFechaNacimiento.getText().toString();
                String especie = editTextEspecie.getText().toString();
                String raza = editTextRaza.getText().toString();

                int selectedId = radioGroupSex.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String sexo = selectedRadioButton.getText().toString();

                String imagenPerfil = (selectedImageUri != null) ? selectedImageUri.toString() : "";

                int idUsuario = intent.getIntExtra("id_usuario", -1);
                Log.d("MainActivity", "Valor de idUsuario: " + idUsuario);


                // Agregar bloque try-catch
                try {
                    dbMascota = new DbMascota(RegistrarMascotaActivity.this);
                    int id_mascota = dbMascota.crearMascota(nombreMascota, fechaNacimiento, especie, raza, sexo, imagenPerfil, idUsuario);

                    // Agregar log para imprimir el ID generado
                    Log.d("RegistrarMascota", "ID de la nueva mascota registrada: " + id_mascota);

                    if (id_mascota > 0) {
                        Toast.makeText(RegistrarMascotaActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegistrarMascotaActivity.this, TusMascotasActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Agregar log para imprimir la excepción
                    Log.e("RegistrarMascota", "Error al registrar mascota: " + e.getMessage());
                }
            }
        });
    }
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
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
            String fecha = dayOfMonth + "-" + (month + 1) + "-" + year;
            if (getActivity() instanceof RegistrarMascotaActivity) {
                ((RegistrarMascotaActivity) getActivity()).setFechaNacimiento(fecha);
            }
        }
    }
    private void setFechaNacimiento(String fecha_seleccionada) {
        editTextFechaNacimiento.setText(fecha_seleccionada);
    }
}