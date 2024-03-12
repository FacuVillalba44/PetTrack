package com.devfacu.pettrack;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.devfacu.pettrack.db.DbMascota;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Objects;

public class RegistrarMascotaActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final String TAG = "activity_registrar_macota";
    private TextView fechaNacimiento;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RadioButton rMacho,rHembra;
    private EditText nombreMascota, razaMascota;
    private Spinner spinnerEspecie;
    private Button registrarMascota;
    private RadioGroup rGroupSex;
    private Uri selectedImageUri;
    private ImageView imageViewFoto;
    private ImageButton botonAgregarImg;

    DbMascota dbMascota;
    private int id_usuario;

    private final ActivityResultLauncher<Intent> imagenLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        Log.d("RegistrarMascota", "URI de la imagen seleccionada: " + selectedImageUri);
                        imageViewFoto.setImageURI(selectedImageUri);

                        RegistrarMascotaActivity.this.selectedImageUri = selectedImageUri;
                    }
                }
            });

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

        botonAgregarImg= findViewById(R.id.imgBtnAgregarFoto);
        imageViewFoto = findViewById(R.id.ivMascota);

        registrarMascota=findViewById(R.id.btnRegistrarMascota_F);
//__________________________Boton Seleccionar Imagen__________________________________

        botonAgregarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                imagenLauncher.launch(intent);
            }
        });


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
                    String raza = razaMascota.getText().toString();

                    int selectedId = rGroupSex.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String sexo = selectedRadioButton.getText().toString();

                    Intent intent = getIntent();
                    id_usuario = intent.getIntExtra("id_usuario", -1);
                    Log.d(TAG, "Valor de id_usuario: " + id_usuario);

                    String imagenMascota = (selectedImageUri !=null) ? selectedImageUri.toString() : "";

                    try{
                        dbMascota = new DbMascota(RegistrarMascotaActivity.this);
                        byte[] bytesImagen = null;
                        if(selectedImageUri != null){
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            bytesImagen = stream.toByteArray();
                        }
                        int id_mascota= dbMascota.crearMascota(nombreM, fechaNac, especieM,raza, sexo, imagenMascota, bytesImagen, id_usuario);

                        if (id_mascota > 0) {
                            Toast.makeText(getApplicationContext(), "Datos guardados", Toast.LENGTH_LONG).show();
                            Intent retornar = new Intent(RegistrarMascotaActivity.this, Home_Activity.class);
                            startActivity(retornar);
                        }else{
                            Toast.makeText(getApplicationContext(), "Fallo al guardar datos", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
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

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
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
                Toast.makeText(RegistrarMascotaActivity.this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




