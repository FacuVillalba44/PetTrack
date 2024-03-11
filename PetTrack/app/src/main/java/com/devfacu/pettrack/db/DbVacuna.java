package com.devfacu.pettrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.devfacu.pettrack.entidades.Vacuna;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class DbVacuna extends BaseDeDatos {
    Context context;
    Vacuna vacuna;
    public DbVacuna(@Nullable Context context) {
        super(context);
    }

    public int crearVacuna (String nombre, String fecha_aplicacion, String proxima_aplicacion, int id_mascota){
        int id_vacuna = -1;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BaseDeDatos.COLUMN_NOMBRE_VACUNA, nombre);
        values.put(BaseDeDatos.COLUMN_FECHA_APLICACION, fecha_aplicacion);
        values.put(BaseDeDatos.COLUMN_PROXIMA_APLICACION, proxima_aplicacion);
        values.put(BaseDeDatos.COLUMN_ID_MASCOTA, id_mascota);

        try{
            id_vacuna = (int) db.insert(TABLE_VACUNA, null, values);

        }catch (Exception ex){
            Log.e("Db vacuna", "Error al registrar vacuna"+ ex.getMessage());
        }finally {
            db.close();
        }
        return id_vacuna;
    }

    public ArrayList<Vacuna> listarVacunas() {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                COLUMN_ID_VACUNA,
                COLUMN_NOMBRE_VACUNA,
                COLUMN_FECHA_APLICACION,
                COLUMN_PROXIMA_APLICACION,
                COLUMN_ID_MASCOTA
        };

        ArrayList<Vacuna> vacunas = new ArrayList<>();
        Cursor cursor = db.query(TABLE_VACUNA, columns, null, null, null, null, null);


        while (cursor.moveToNext()) {
            Vacuna vacuna = new Vacuna();
            vacuna.setId_vacuna(cursor.getInt(0));
            vacuna.setNombre_vacuna(cursor.getString(1));
            vacuna.setFecha_aplicacion(cursor.getString(2));
            vacuna.setProxima_aplicacion(cursor.getString(3));
//            vacuna.setIdMascota(cursor.getInt(4));

            vacunas.add(vacuna);
        }

        cursor.close();
        db.close();

        return vacunas;
    }

    public Vacuna obtenerVacunaPorId(int idVacuna) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                COLUMN_ID_VACUNA,
                COLUMN_NOMBRE_VACUNA,
                COLUMN_FECHA_APLICACION,
                COLUMN_PROXIMA_APLICACION,
                COLUMN_ID_MASCOTA
        };

        String selection = COLUMN_ID_VACUNA + " = ?";
        String[] selectionArgs = { String.valueOf(idVacuna) };

        Cursor cursor = db.query(TABLE_VACUNA, columns, selection, selectionArgs, null, null, null);

        Vacuna vacuna = null;
        if (cursor.moveToNext()) {
            vacuna = new Vacuna();
            vacuna.setId_vacuna(cursor.getInt(0));
            vacuna.setNombre_vacuna(cursor.getString(1));
            vacuna.setFecha_aplicacion(cursor.getString(2));
            vacuna.setProxima_aplicacion(cursor.getString(3));
//            vacuna.set(cursor.getInt(4));
        }

        cursor.close();
        db.close();

        return vacuna;
    }
    public ArrayList<Vacuna> listarVacunasPorMascota(int idMascota) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Vacuna> listaVacunas = new ArrayList<>();

        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_VACUNA + " WHERE " + COLUMN_ID_MASCOTA + " = ?",
                    new String[]{String.valueOf(idMascota)});

            while (cursor.moveToNext()) {
                Vacuna vacuna = new Vacuna();
                vacuna.setId_vacuna(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_VACUNA)));
                vacuna.setNombre_vacuna(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_VACUNA)));
                vacuna.setFecha_aplicacion(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_APLICACION)));
                vacuna.setProxima_aplicacion(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROXIMA_APLICACION)));
                vacuna.setId_mascota(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_MASCOTA)));

                listaVacunas.add(vacuna);
            }
        } catch (Exception ex) {
            Log.e("DbVacuna", "Error al obtener vacunas por mascota: " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return listaVacunas;
    }
    public List<Vacuna> obtenerVacunasFuturas() {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                COLUMN_ID_VACUNA,
                COLUMN_NOMBRE_VACUNA,
                COLUMN_FECHA_APLICACION,
                COLUMN_PROXIMA_APLICACION,
                COLUMN_ID_MASCOTA
        };

        String selection = COLUMN_PROXIMA_APLICACION + " >= date('now')";

        Cursor cursor = db.query(TABLE_VACUNA, columns, selection, null, null, null, null);

        List<Vacuna> vacunas = new ArrayList<>();
        while (cursor.moveToNext()) {
            Vacuna vacuna = new Vacuna();
            vacuna.setId_vacuna(cursor.getInt(0));
            vacuna.setNombre_vacuna(cursor.getString(1));
            vacuna.setFecha_aplicacion(cursor.getString(2));
            vacuna.setProxima_aplicacion(cursor.getString(3));
//            vacuna.setIdMascota(cursor.getInt(4));

            vacunas.add(vacuna);
        }

        cursor.close();
        db.close();

        return vacunas;
    }
    public int editarVacuna(int id_vacuna, String nombre, String fecha_aplicacion, String proxima_aplicacion, int id_mascota){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BaseDeDatos.COLUMN_ID_VACUNA, id_vacuna);
        values.put(BaseDeDatos.COLUMN_NOMBRE_VACUNA, nombre);
        values.put(BaseDeDatos.COLUMN_FECHA_APLICACION, fecha_aplicacion);
        values.put(BaseDeDatos.COLUMN_PROXIMA_APLICACION, proxima_aplicacion);
        values.put(BaseDeDatos.COLUMN_ID_MASCOTA, id_mascota);

        int rowsAffected = -1;
        try{
            rowsAffected = db.update(TABLE_VACUNA, values, COLUMN_ID_VACUNA + " = ?", new String[]{String.valueOf(id_vacuna)});

        }catch (Exception ex){
            Log.e("DB vacuna", "error al modificar vacuna"+ ex.getMessage());
        }finally {
            db.close();
        }
        return rowsAffected;
    }
    public int eliminarVacuna(int idVacuna) {
        SQLiteDatabase db = getWritableDatabase();

        int rowsAffected = -1;

        try {
            rowsAffected = db.delete(TABLE_VACUNA, COLUMN_ID_VACUNA + " = ?", new String[]{String.valueOf(idVacuna)});
        } catch (Exception ex) {
            Log.e("DbVacuna", "Error al eliminar vacuna: " + ex.getMessage());
        } finally {
            db.close();
        }

        return rowsAffected;
    }
}
