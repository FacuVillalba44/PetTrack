package com.devfacu.pettrack;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.entidades.Mascota;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class PerfilMascotaActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Uri uri; // Declaración como un campo de clase

    private ImageView imageView;
    private RadioButton radioMacho;
    private RadioButton radioHembra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);

        TextView nombreMascota = findViewById(R.id.textViewNombreMascota);
        TextView fechaNacimiento = findViewById(R.id.textViewFecNac);
        TextView especie = findViewById(R.id.textViewEspecie);
        TextView raza = findViewById(R.id.textViewtRaza);
        imageView = findViewById(R.id.imageViewFoto);
        radioMacho = findViewById(R.id.radioMacho);
        radioHembra = findViewById(R.id.radioHembra);

        Intent intent = getIntent();
        int id_mascota = intent.getIntExtra("id_mascota", -1);
        Log.d("PerfilMascotaActivity", "ID de mascota: " + id_mascota);

        if (id_mascota != -1) {
            DbMascota dbMascota = new DbMascota(this);
            Mascota mascota = dbMascota.verDetallesMascota(id_mascota);

            if (mascota != null) {
                nombreMascota.setText(mascota.getNombre());
                fechaNacimiento.setText(mascota.getFecha_nacimiento());
                especie.setText(mascota.getEspecie());
                raza.setText(mascota.getRaza());

                // Configuración de los RadioButton
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

                // Asigna el valor a la variable de clase 'uri'
                uri = obtenerUriDesdeString(mascota.getImagen());

                if (uri != null) {
                    Log.d("PerfilMascotaActivity", "URI de la imagen: " + uri.toString());

                    // Solicitar permiso de lectura externa si no se tiene
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        // Si ya se tiene el permiso, carga la imagen
                        cargarImagenDesdeProveedor(uri);
                    }
                } else {
                    Log.e("PerfilMascotaActivity", "La URI de la imagen no es válida");
                }
            }
        }
    }

    // Método para manejar el resultado de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido, carga la imagen
                    cargarImagenDesdeProveedor(uri);
                    Log.d("PerfilMascotaActivity", "Permiso concedido. Cargando la imagen.");
                } else {
                    // Permiso denegado, puedes mostrar un mensaje o tomar otra acción
                    Log.e("PerfilMascotaActivity", "Permiso denegado. No se puede cargar la imagen.");
                }
            } else {
                Log.e("PerfilMascotaActivity", "Error: grantResults tiene longitud cero.");
            }
        }
    }

    // Método para cargar la imagen desde el proveedor de contenido
    private void cargarImagenDesdeProveedor(Uri uri) {
        Log.d("PerfilMascotaActivity", "Intentando cargar imagen desde: " + uri.toString());

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);

            if (inputStream != null) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);

                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    Log.d("PerfilMascotaActivity", "Imagen cargada correctamente");
                } else {
                    Log.e("PerfilMascotaActivity", "Error al decodificar la imagen");
                }

                bufferedInputStream.close();
                inputStream.close();
            } else {
                Log.e("PerfilMascotaActivity", "Error: InputStream es nulo");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PerfilMascotaActivity", "Error al cargar la imagen desde el proveedor de contenido.");
        }
    }

    // Método para obtener Uri desde una cadena de texto
    private Uri obtenerUriDesdeString(String uriString) {
        try {
            return Uri.parse(uriString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}