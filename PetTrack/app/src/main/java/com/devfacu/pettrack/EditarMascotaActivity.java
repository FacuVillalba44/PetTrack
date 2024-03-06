package com.devfacu.pettrack;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.bumptech.glide.Glide;
import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.entidades.Mascota;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class EditarMascotaActivity extends AppCompatActivity {
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
    private ImageView imageView;
    private Button botonGuardarCambios;
    private DbMascota dbMascota;
    private Uri selectedImageUri;
    private Mascota mascota;
    private boolean nuevaImagenSeleccionada = false;


    private final ActivityResultLauncher<Intent> imagenLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    Log.d("EditarMascotaActivity", "URI de la imagen seleccionada: " + selectedImage);
                    imageView.setImageURI(selectedImage);
                    selectedImageUri = selectedImage;
                    nuevaImagenSeleccionada = true;

                    Log.d("EditarMascotaActivity", "Nueva imagen seleccionada: " + nuevaImagenSeleccionada);

                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_mascota);

        editTextNombreMascota = findViewById(R.id.editTextNombreMascota);
        editTextFechaNacimiento = findViewById(R.id.editTextFecNac);
        editTextEspecie = findViewById(R.id.editTextEspecie);
        editTextRaza = findViewById(R.id.editTextRaza);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        radioMacho = findViewById(R.id.radioMacho);
        radioHembra = findViewById(R.id.radioHembra);
        botonSeleccionImagen = findViewById(R.id.btnSeleccionarImagen);
        botonCamara = findViewById(R.id.btnCamara);
        imageView = findViewById(R.id.imageViewFoto);
        botonGuardarCambios = findViewById(R.id.buttonGuardarCambios);

        Intent intent = getIntent();
        int id_mascota = intent.getIntExtra("id_mascota", -1);
        byte[] imgBytes = intent.getByteArrayExtra("imagen_blob");
        int id_usuario = intent.getIntExtra("id_usuario", -1);

        Log.d("PerfilMascotaActivity", "ID de mascota: " + id_mascota);

        if (id_mascota != -1) {
            DbMascota dbMascota = new DbMascota(this);
            mascota = dbMascota.verDetallesMascota(id_mascota);

            if (mascota != null) {
                editTextNombreMascota.setText(mascota.getNombre());
                editTextFechaNacimiento.setText(mascota.getFecha_nacimiento());
                editTextEspecie.setText(mascota.getEspecie());
                editTextRaza.setText(mascota.getRaza());

                if ("Macho".equals(mascota.getSexo())) {
                    radioMacho.setChecked(true);
                    radioHembra.setChecked(false);
                } else if ("Hembra".equals(mascota.getSexo())) {
                    radioMacho.setChecked(false);
                    radioHembra.setChecked(true);
                } else {
                    radioMacho.setChecked(false);
                    radioHembra.setChecked(false);
                }

                if (imgBytes != null && imgBytes.length > 0) {
                    cargarImagenDesdeBlob(imgBytes);
                    nuevaImagenSeleccionada = false;
                } else {
                    imageView.setImageResource(R.drawable.placeholder);
                    nuevaImagenSeleccionada = true;
                }
            }
;        }
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

                try {
                    dbMascota = new DbMascota(EditarMascotaActivity.this);
                    byte[] bytesImagen = null;
                    if (nuevaImagenSeleccionada) {
                        // Seleccionó una nueva imagen, entonces cargamos la imagen seleccionada
                        if (selectedImageUri != null) {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            bytesImagen = stream.toByteArray();
                        }
                    } else {
                        // No seleccionó una nueva imagen, cargamos la imagen de la base de datos
                        bytesImagen = imgBytes;
                    }

//                    if (selectedImageUri != null) {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        bytesImagen = stream.toByteArray();
//                    }
                    int mascotaEditada;
                    mascotaEditada = dbMascota.editarMascota(id_mascota, nombreMascota, fechaNacimiento, especie, raza, sexo, imagenPerfil, bytesImagen, id_usuario);

                    if (mascotaEditada > 0) {
                        Toast.makeText(EditarMascotaActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditarMascotaActivity.this, TusMascotasActivity.class);
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
        RegistrarMascotaActivity.DatePickerFragment newFragment = new RegistrarMascotaActivity.DatePickerFragment();
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

    private void cargarImagenDesdeBlob(byte[] imgBytes) {
        try {
            Log.d("EditarMascotaActivity", "Nueva imagen seleccionada: " + nuevaImagenSeleccionada);
            Log.d("EditarMascotaActivity", "Longitud de imgBytes: " + (imgBytes != null ? imgBytes.length : 0));
            Log.d("EditarMascotaActivity", "selectedImageUri: " + selectedImageUri);

            if (imgBytes != null && imgBytes.length > 0) {
                // Si hay datos en imgBytes, cargamos la imagen desde el blob
                Log.d("EditarMascotaActivity", "Cargando imagen desde blob");
                Glide.with(EditarMascotaActivity.this)
                        .load(imgBytes)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(imageView);
            } else if (selectedImageUri != null) {
                // Si no hay datos en imgBytes pero hay una Uri seleccionada, cargamos la imagen desde la Uri
                Log.d("EditarMascotaActivity", "Cargando nueva imagen seleccionada");
                Glide.with(EditarMascotaActivity.this)
                        .load(selectedImageUri)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(imageView);
            } else {
                // En otros casos, establecemos la imagen de placeholder
                Log.d("EditarMascotaActivity", "Placeholder imagen");
                imageView.setImageResource(R.drawable.placeholder);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EditarMascotaActivity", "Error al cargar la imagen con Glide: " + e.getMessage());
        }
    }
}

