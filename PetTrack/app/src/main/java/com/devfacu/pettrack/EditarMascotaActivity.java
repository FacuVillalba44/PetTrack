package com.devfacu.pettrack;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.entidades.Mascota;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Objects;

public class EditarMascotaActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final String TAG = "activity_editar_mascota";
    private EditText editTextNombreMascota, editTextFechaNacimiento, editTextEspecie, editTextRaza;
    private RadioButton radioMacho, radioHembra;
    private RadioGroup radioGroupSex;
    private Button botonSeleccionImagen, botonGuardarCambios, botonEliminarMascota;
    private ImageView imageViewFoto;
    private DbMascota dbMascota;
    private Uri selectedImageUri;
    private Mascota mascota;
    private boolean nuevaImagenSeleccionada = false;
    private String imageDirectoryPath;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

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

        // Inicializar vistas
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
        botonEliminarMascota = findViewById(R.id.buttonEliminarMascota);

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
        }
        editTextFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        EditarMascotaActivity.this,
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
                editTextFechaNacimiento.setText(date);//<esta variable tiene la fecha
            }
        };
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
                Log.d(TAG, "Guardando los cambios de la mascota...");
                String nombreMascota = editTextNombreMascota.getText().toString();
                String fechaNacimiento = editTextFechaNacimiento.getText().toString();
                String especie = editTextEspecie.getText().toString();
                String raza = editTextRaza.getText().toString();

                int selectedId = radioGroupSex.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String sexo = selectedRadioButton.getText().toString();

                String imagenPerfil = (selectedImageUri != null) ? selectedImageUri.toString() : "";


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

                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.myapp.PREFERENCES", Context.MODE_PRIVATE);
                    int id_usuario_guardado = sharedPreferences.getInt("id_usuario", -1);

                    if (id_usuario_guardado != -1) {
                        // El ID de usuario se recuperó correctamente
                        Log.d(TAG, "ID de usuario recuperado de las preferencias: " + id_usuario_guardado);
                    } else {
                        // No se pudo recuperar el ID de usuario de las preferencias
                        Log.d(TAG, "No se pudo recuperar el ID de usuario de las preferencias");
                    }

                    int mascotaEditada = dbMascota.editarMascota(id_mascota, nombreMascota, fechaNacimiento, especie, raza, sexo, imagenPerfil, bytesImagen, id_usuario_guardado);

                    if (mascotaEditada > 0) {
                        Log.d(TAG, "Los cambios de la mascota se guardaron correctamente");

                        Toast.makeText(EditarMascotaActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditarMascotaActivity.this, TusMascotasActivity.class);
                        intent.putExtra("id_mascota", id_mascota);
                        setResult(RESULT_OK, intent);
                        finish();
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("EditarMascotaActivity", "Error al registrar mascota: " + e.getMessage());
                    Log.e(TAG, "Error al registrar mascota: " + e.getMessage());
                }
            }
        });
        botonEliminarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoConfirmacionBorrar();
            }
        });
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
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagenLauncher.launch(intent1);
            } else {
                // Permiso denegado, informar al usuario
                Toast.makeText(EditarMascotaActivity.this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void mostrarDialogoConfirmacionBorrar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta mascota?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id_mascota = EditarMascotaActivity.this.mascota.getId_mascota();
                Log.d("PerfilMascotaActivity", "ID de mascota a eliminar: " + id_mascota);

                eliminarMascota(id_mascota);
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

    private void eliminarMascota(int id_mascota) {
        DbMascota dbMascota = new DbMascota(EditarMascotaActivity.this);
        int rowsAffected = dbMascota.eliminarMascota(id_mascota);

        if (rowsAffected > 0) {
            Toast.makeText(EditarMascotaActivity.this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
            volverATusMascotasActivity();
        } else {
            Toast.makeText(EditarMascotaActivity.this, "Error al eliminar la mascota", Toast.LENGTH_SHORT).show();
        }
    }

    private void volverATusMascotasActivity() {
        Intent intent = new Intent(EditarMascotaActivity.this, TusMascotasActivity.class);
        startActivity(intent);
        finish();
    }
}