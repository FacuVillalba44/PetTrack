package com.devfacu.pettrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

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




}
