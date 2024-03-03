package com.devfacu.pettrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.devfacu.pettrack.entidades.Mascota;

import java.util.ArrayList;

public class DbMascota extends BaseDeDatos {
    Context context;

    public DbMascota(@Nullable Context context) {
        super(context);
    }

    public int crearMascota(String nombre, String fecha_nacimiento, String especie, String raza, String sexo, String imagen, int idUsuario) {
        int id_mascota = -1;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BaseDeDatos.COLUMN_NOMBRE_MASCOTA, nombre);
        values.put(BaseDeDatos.COLUMN_FECHA_NACIMIENTO, fecha_nacimiento);
        values.put(BaseDeDatos.COLUMN_ESPECIE, especie);
        values.put(BaseDeDatos.COLUMN_RAZA, raza);
        values.put(BaseDeDatos.COLUMN_SEXO, sexo);
        values.put(BaseDeDatos.COLUMN_IMAGEN_MASCOTA, imagen);
        values.put(BaseDeDatos.COLUMN_ID_USUARIO, idUsuario);

        try {
            id_mascota = (int) db.insert(TABLE_MASCOTA, null, values);
            Log.d("DbMascota", "Nuevo registro de mascota creado. ID: " + id_mascota);
        } catch (Exception ex) {
            Log.e("DbMascota", "Error al crear mascota: " + ex.getMessage());
            Log.e("RegistrarMascota", "Error al registrar mascota: " + ex.getMessage());
        } finally {
            db.close();
        }

        return id_mascota;
    }


    public ArrayList<Mascota> listarMascotas() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Mascota> listaMascotas = new ArrayList<>();

        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_MASCOTA, null);

            while (cursor.moveToNext()) {
                Mascota mascota = new Mascota();
                mascota.setId_mascota(cursor.getInt(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_ID_MASCOTA)));
                mascota.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_NOMBRE_MASCOTA)));
                mascota.setFecha_nacimiento(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_FECHA_NACIMIENTO)));
                mascota.setEspecie(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_ESPECIE)));
                mascota.setRaza(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_RAZA)));
                mascota.setSexo(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_SEXO)));
                mascota.setImagen(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_IMAGEN_MASCOTA)));
                //mascota.setIdUsuario(cursor.getInt(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_ID_USUARIO)));

                listaMascotas.add(mascota);
            }
        } catch (Exception ex) {
            Log.e("DbMascota", "Error al listar mascotas: " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return listaMascotas;
    }

    public ArrayList<Mascota> listarMascotasSimplificado() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Mascota> listaMascotas = new ArrayList<>();

        Cursor cursor = null;

        try {
            // Selecciona solo las columnas necesarias
            String[] columnas = {
                    BaseDeDatos.COLUMN_ID_MASCOTA,
                    BaseDeDatos.COLUMN_NOMBRE_MASCOTA,
                    BaseDeDatos.COLUMN_IMAGEN_MASCOTA
            };

            cursor = db.query(TABLE_MASCOTA, columnas, null, null, null, null, null);

            while (cursor.moveToNext()) {
                Mascota mascota = new Mascota();
                mascota.setId_mascota(cursor.getInt(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_ID_MASCOTA)));
                mascota.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_NOMBRE_MASCOTA)));
                mascota.setImagen(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_IMAGEN_MASCOTA)));

                listaMascotas.add(mascota);
            }
        } catch (Exception ex) {
            Log.e("DbMascota", "Error al listar mascotas: " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return listaMascotas;
    }

    public Mascota verDetallesMascota(int idMascota) {
        SQLiteDatabase db = getReadableDatabase();
        Mascota mascota = null;

        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_MASCOTA + " WHERE " + BaseDeDatos.COLUMN_ID_MASCOTA + " = ?", new String[]{String.valueOf(idMascota)});

            if (cursor.moveToFirst()) {
                mascota = new Mascota();
                mascota.setId_mascota(cursor.getInt(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_ID_MASCOTA)));
                mascota.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_NOMBRE_MASCOTA)));
                mascota.setFecha_nacimiento(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_FECHA_NACIMIENTO)));
                mascota.setEspecie(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_ESPECIE)));
                mascota.setRaza(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_RAZA)));
                mascota.setSexo(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_SEXO)));
                mascota.setImagen(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_IMAGEN_MASCOTA))); // Agregado el campo imagen
//                mascota.setIdUsuario(cursor.getInt(cursor.getColumnIndexOrThrow(BaseDeDatos.COLUMN_ID_USUARIO)));
            }
        } catch (Exception ex) {
            Log.e("DbMascota", "Error al obtener detalles de mascota: " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return mascota;
    }


    public int editarMascota(int idMascota, String nombre, String fechaNacimiento, String especie, String raza, String sexo, String imagen) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BaseDeDatos.COLUMN_NOMBRE_MASCOTA, nombre);
        values.put(BaseDeDatos.COLUMN_FECHA_NACIMIENTO, fechaNacimiento);
        values.put(BaseDeDatos.COLUMN_ESPECIE, especie);
        values.put(BaseDeDatos.COLUMN_RAZA, raza);
        values.put(BaseDeDatos.COLUMN_SEXO, sexo);
        values.put(BaseDeDatos.COLUMN_IMAGEN_MASCOTA, imagen);

        int rowsAffected = -1;

        try {
            rowsAffected = db.update(TABLE_MASCOTA, values, COLUMN_ID_MASCOTA + " = ?", new String[]{String.valueOf(idMascota)});
        } catch (Exception ex) {
            Log.e("DbMascota", "Error al editar mascota: " + ex.getMessage());
        } finally {
            db.close();
        }

        return rowsAffected;
    }

    public int eliminarMascota(int id_mascota) {
        SQLiteDatabase db = getWritableDatabase();

        int rowsAffected = -1;

        try {
            rowsAffected = db.delete(TABLE_MASCOTA, COLUMN_ID_MASCOTA + " = ?", new String[]{String.valueOf(id_mascota)});
        } catch (Exception ex) {
            Log.e("DbMascota", "Error al eliminar mascota: " + ex.getMessage());
        } finally {
            db.close();
        }

        return rowsAffected;
    }
}