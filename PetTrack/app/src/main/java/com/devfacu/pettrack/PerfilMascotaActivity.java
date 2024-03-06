package com.devfacu.pettrack;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.entidades.Mascota;

public class PerfilMascotaActivity extends AppCompatActivity {

    private ImageView imageView;
    private Mascota mascota;

    private int id_mascota;

    int id_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);

        TextView nombreMascota = findViewById(R.id.textViewNombreMascota);
        TextView fechaNacimiento = findViewById(R.id.textViewFecNac);
        TextView especie = findViewById(R.id.textViewEspecie);
        TextView raza = findViewById(R.id.textViewtRaza);
        imageView = findViewById(R.id.imageViewFoto);
        RadioButton radioMacho = findViewById(R.id.radioMacho);
        RadioButton radioHembra = findViewById(R.id.radioHembra);

        Button botonEditarMascota = findViewById(R.id.btnEditarMascota);
        Button botonEliminarMascota = findViewById(R.id.btnEliminarMascota);

        Intent intent = getIntent();
        id_mascota = intent.getIntExtra("id_mascota", -1);
        byte[] imgBytes = intent.getByteArrayExtra("imagen_blob");

        Log.d("PerfilMascotaActivity", "ID de mascota: " + id_mascota);

        if (id_mascota != -1) {
            DbMascota dbMascota = new DbMascota(this);
            mascota = dbMascota.verDetallesMascota(id_mascota);

            if (mascota != null) {
                nombreMascota.setText(mascota.getNombre());
                fechaNacimiento.setText(mascota.getFecha_nacimiento());
                especie.setText(mascota.getEspecie());
                raza.setText(mascota.getRaza());

                int id_usuario = mascota.getId_usuario();

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
                } else {
                    imageView.setImageResource(R.drawable.placeholder);
                }
            }
        }

        botonEditarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilMascotaActivity.this, EditarMascotaActivity.class);
                intent.putExtra("id_mascota", id_mascota);
                intent.putExtra("imagen_blob", imgBytes);
                intent.putExtra("id_usuario", id_usuario);
                startActivity(intent);
                finish();
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
            Glide.with(this)
                    .load(imgBytes)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PerfilMascotaActivity", "Error al cargar la imagen desde BLOB con Glide: " + e.getMessage());
        }
    }

    private void mostrarDialogoConfirmacionBorrar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta mascota?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Utiliza la variable de instancia this.mascota
                int id_mascota = PerfilMascotaActivity.this.mascota.getId_mascota();
                Log.d("PerfilMascotaActivity", "ID de mascota a eliminar: " + id_mascota);

                // Lógica para eliminar la mascota
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
        DbMascota dbMascota = new DbMascota(PerfilMascotaActivity.this);
        int rowsAffected = dbMascota.eliminarMascota(id_mascota);

        if (rowsAffected > 0) {
            Toast.makeText(PerfilMascotaActivity.this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
            volverATusMascotasActivity();
        } else {
            Toast.makeText(PerfilMascotaActivity.this, "Error al eliminar la mascota", Toast.LENGTH_SHORT).show();
        }
    }

    private void volverATusMascotasActivity() {
        Intent intent = new Intent(PerfilMascotaActivity.this, TusMascotasActivity.class);
        startActivity(intent);
        finish();
    }
}








































//package com.devfacu.pettrack;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;
//import com.devfacu.pettrack.db.DbMascota;
//import com.devfacu.pettrack.entidades.Mascota;
//
//import java.util.Arrays;
//
//public class PerfilMascotaActivity extends AppCompatActivity {
//
//    private ImageView imageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_perfil_mascota);
//
//        TextView nombreMascota = findViewById(R.id.textViewNombreMascota);
//        TextView fechaNacimiento = findViewById(R.id.textViewFecNac);
//        TextView especie = findViewById(R.id.textViewEspecie);
//        TextView raza = findViewById(R.id.textViewtRaza);
//        imageView = findViewById(R.id.imageViewFoto);
//        RadioButton radioMacho = findViewById(R.id.radioMacho);
//        RadioButton radioHembra = findViewById(R.id.radioHembra);
//
//        int id_mascota = getIntent().getIntExtra("id_mascota", -1);
//        Log.d("PerfilMascotaActivity", "ID de mascota: " + id_mascota);
//
//        if (id_mascota != -1) {
//            DbMascota dbMascota = new DbMascota(this);
//            Mascota mascota = dbMascota.verDetallesMascota(id_mascota);
//
//            if (mascota != null) {
//                nombreMascota.setText(mascota.getNombre());
//                fechaNacimiento.setText(mascota.getFecha_nacimiento());
//                especie.setText(mascota.getEspecie());
//                raza.setText(mascota.getRaza());
//
//                if ("Macho".equals(mascota.getSexo())) {
//                    radioMacho.setChecked(true);
//                    radioHembra.setChecked(false);
//                } else if ("Hembra".equals(mascota.getSexo())) {
//                    radioMacho.setChecked(false);
//                    radioHembra.setChecked(true);
//                } else {
//                    radioMacho.setChecked(false);
//                    radioHembra.setChecked(false);
//                }
//
//                byte[] imgBytes = mascota.getImagen_blob();
//                Log.d("PerfilMascotaActivity", "imagen blob: " + Arrays.toString(imgBytes));
//
//                if (imgBytes != null && imgBytes.length > 0) {
//                    cargarImagenDesdeBlob(imgBytes);
//                } else {
//                    imageView.setImageResource(R.drawable.placeholder);
//                }
//            }
//        }
//    }
//
//    private void cargarImagenDesdeBlob(byte[] imgBytes) {
//        try {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
//            if (bitmap != null) {
//                imageView.setImageBitmap(bitmap);
//            } else {
//                Log.e("PerfilMascotaActivity", "Error al decodificar la imagen desde los bytes");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("PerfilMascotaActivity", "Error al cargar la imagen desde BLOB: " + e.getMessage());
//        }
//    }
//}
























