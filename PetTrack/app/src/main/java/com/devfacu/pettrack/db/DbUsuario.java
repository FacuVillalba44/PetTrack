package com.devfacu.pettrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.devfacu.pettrack.entidades.Usuario;

public class DbUsuario extends BaseDeDatos {
    Context context;

    public DbUsuario(@Nullable Context context) {
        super(context);
    }

    public long crearUsuario (String nombre, String email, String password){

        long id = 0;

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_NOMBRE, nombre);
            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_PASSWORD, password);

            id = db.insert(TABLE_USUARIO, null, values);

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return id;
    }

    public boolean checkEmail (String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ TABLE_USUARIO +" where "+ COLUMN_EMAIL + " =?", new String []{email});

        if (cursor.getCount()>0){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkEmailPassword(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_USUARIO + " where " + COLUMN_EMAIL + " =? and " + COLUMN_PASSWORD + " =?", new String[]{email, password});

        if(cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }

    public int obtenerIdUsuario(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        int idUsuario = -1; // Valor predeterminado en caso de que no se encuentre el usuario

        String[] columns = {COLUMN_ID_USUARIO};
        String selection = COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USUARIO, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(COLUMN_ID_USUARIO);
            idUsuario = cursor.getInt(columnIndex);
            Log.d("DbUsuario", "Usuario encontrado. idUsuario: " + idUsuario);
        } else {
            Log.d("DbUsuario", "Usuario no encontrado.");
        }

        cursor.close();
        db.close();
        return idUsuario;
    }

    public Usuario getUsuarioById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USUARIO,
                new String[]{COLUMN_ID_USUARIO, COLUMN_NOMBRE, COLUMN_EMAIL, COLUMN_PASSWORD},
                COLUMN_ID_USUARIO + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null, null
        );

        Usuario usuario = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                usuario = new Usuario();


                int idUsuario = cursor.getColumnIndex(COLUMN_ID_USUARIO);
                if (idUsuario != -1) {
                    usuario.setId(cursor.getLong(idUsuario));
                }else {
                    throw new IllegalStateException("La columna " + COLUMN_ID_USUARIO + " no fue encontrada en el cursor.");
                }

                int nombre = cursor.getColumnIndex(COLUMN_NOMBRE);
                if (nombre != -1) {
                    usuario.setNombre(cursor.getString(nombre));
                }else {
                    throw new IllegalStateException("La columna " + COLUMN_NOMBRE + " no fue encontrada en el cursor.");
                }

                int email = cursor.getColumnIndex(COLUMN_EMAIL);
                if (email != -1) {
                    usuario.setEmail(cursor.getString(email));
                }else {
                    throw new IllegalStateException("La columna " + COLUMN_EMAIL + " no fue encontrada en el cursor.");
                }

                int password = cursor.getColumnIndex(COLUMN_PASSWORD);
                if (password != -1) {
                    usuario.setPassword(cursor.getString(password));
                }else {
                    throw new IllegalStateException("La columna " + COLUMN_PASSWORD + " no fue encontrada en el cursor.");
                }
            }

            cursor.close();
        }

        return usuario;
    }

    public int editarUsuario(long id, String nuevoNombre, String nuevoEmail, String nuevaPassword) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nuevoNombre);
        values.put(COLUMN_EMAIL, nuevoEmail);
        values.put(COLUMN_PASSWORD, nuevaPassword);

        // Updating row
        return db.update(TABLE_USUARIO, values, COLUMN_ID_USUARIO + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void eliminarUsuario(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_USUARIO, COLUMN_ID_USUARIO + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    

}
