package com.devfacu.pettrack;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

public class EditarMascotaActivity extends AppCompatActivity {
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private EditText editTextNombreMascota, editTextFechaNacimiento, editTextEspecie, editTextRaza;
    private RadioButton radioMacho, radioHembra;
    private RadioGroup radioGroupSex;
    private Button botonSeleccionImagen, botonCamara, botonGuardarCambios;
    private ImageView imageViewFoto;
    private DbMascota dbMascota;
    private Uri selectedImageUri;
    private Mascota mascota;
    private boolean nuevaImagenSeleccionada = false;
    private String imageDirectoryPath;
    private final ActivityResultLauncher<Intent> imagenLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        Log.d("RegistrarMascota", "URI de la imagen seleccionada: " + selectedImageUri);
                        imageViewFoto.setImageURI(selectedImageUri);
                        EditarMascotaActivity.this.selectedImageUri = selectedImageUri;
                    }
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
        imageViewFoto = findViewById(R.id.imageViewFoto);
        botonGuardarCambios = findViewById(R.id.buttonGuardarCambios);

        File imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (imageDirectory != null) {
            if (!imageDirectory.exists()) {
                imageDirectory.mkdirs();
            }
            imageDirectoryPath = imageDirectory.getAbsolutePath();
        }
        Intent intent = getIntent();
        int id_mascota = intent.getIntExtra("id_mascota", -1);
        byte[] imgBytes = intent.getByteArrayExtra("imagen_blob");
        int id_usuario = intent.getIntExtra("id_usuario", -1);

        Log.d("Editar Mascota", "ID de mascota: " + id_mascota);
        Log.d("Editar Mascota", "id usuario" + id_usuario);

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
                    imageViewFoto.setImageResource(R.drawable.placeholder);
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
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                imagenLauncher.launch(intent);

                nuevaImagenSeleccionada = true;
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
                        bytesImagen = imgBytes;
                    }


                    int mascotaEditada = dbMascota.editarMascota(id_mascota, nombreMascota, fechaNacimiento, especie, raza, sexo, imagenPerfil, bytesImagen, id_usuario);

                    if (mascotaEditada > 0) {
                        Toast.makeText(EditarMascotaActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditarMascotaActivity.this, TusMascotasActivity.class);
                        intent.putExtra("id_mascota_editada", id_mascota);
                        setResult(RESULT_OK, intent);
                        finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("EditarMascotaActivity", "Error al registrar mascota: " + e.getMessage());
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
    public void setFechaNacimiento(String fecha_seleccionada) {
        editTextFechaNacimiento.setText(fecha_seleccionada);
    }

    private void cargarImagenDesdeBlob(byte[] imgBytes) {
        try {
            Log.d("EditarMascotaActivity", "Nueva imagen seleccionada: " + nuevaImagenSeleccionada);
            Log.d("EditarMascotaActivity", "Longitud de imgBytes: " + (imgBytes != null ? imgBytes.length : 0));
            Log.d("EditarMascotaActivity", "selectedImageUri: " + selectedImageUri);
            Glide.with(EditarMascotaActivity.this).clear(imageViewFoto);

            if (imgBytes != null && imgBytes.length > 0) {
                // Si hay datos en imgBytes, cargamos la imagen desde el blob
                Log.d("EditarMascotaActivity", "Cargando imagen desde blob");
                Glide.with(EditarMascotaActivity.this)
                        .load(imgBytes)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(imageViewFoto);
            } else if (selectedImageUri != null) {
                // Si no hay datos en imgBytes pero hay una Uri seleccionada, cargamos la imagen desde la Uri
                Log.d("EditarMascotaActivity", "Cargando nueva imagen seleccionada");
                Glide.with(EditarMascotaActivity.this)
                        .load(selectedImageUri)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(imageViewFoto);
            } else {
                // En otros casos, establecemos la imagen de placeholder
                Log.d("EditarMascotaActivity", "Placeholder imagen");
                imageViewFoto.setImageResource(R.drawable.placeholder);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EditarMascotaActivity", "Error al cargar la imagen con Glide: " + e.getMessage());
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    // Método para manejar el resultado de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, lanzar el selector de imágenes
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagenLauncher.launch(intent);
            } else {
                // Permiso denegado, informar al usuario
                Toast.makeText(EditarMascotaActivity.this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

