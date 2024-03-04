package com.devfacu.pettrack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.entidades.Mascota;

public class PerfilMascotaActivity extends AppCompatActivity {

    private ImageView imageView;

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

        Intent intent = getIntent();
        int id_mascota = intent.getIntExtra("id_mascota", -1);
        byte[] imgBytes = intent.getByteArrayExtra("imagen_blob");

        Log.d("PerfilMascotaActivity", "ID de mascota: " + id_mascota);

        if (id_mascota != -1) {
            DbMascota dbMascota = new DbMascota(this);
            Mascota mascota = dbMascota.verDetallesMascota(id_mascota);

            if (mascota != null) {
                nombreMascota.setText(mascota.getNombre());
                fechaNacimiento.setText(mascota.getFecha_nacimiento());
                especie.setText(mascota.getEspecie());
                raza.setText(mascota.getRaza());

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
























