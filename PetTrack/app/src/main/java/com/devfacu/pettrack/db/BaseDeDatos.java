package com.devfacu.pettrack.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PetTrackDB";

    //Constantes para nombres de tabla

    public static final String TABLE_USUARIO = "usuario";
    public static final String TABLE_MASCOTA = "mascota";

    public static final String TABLE_RECORDATORIO = "recordatorio";

    //Constantes para claves primarias
    public static final String COLUMN_ID_REGISTRO = "id_registro";
    public static final String COLUMN_ID_USUARIO= "id_usuario";
    public static final String COLUMN_ID_MASCOTA = "id_mascota";
    public static final String COLUMN_ID_RECORDATORIO= "id_recordatorio";



    //Tabla Usuario
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    //Tabla Mascota
    public static final String COLUMN_NOMBRE_MASCOTA = "nombre_mascota";
    public static final String COLUMN_FECHA_NACIMIENTO = "fecha_nacimiento";
    public static final String COLUMN_ESPECIE = "especie";
    public static final String COLUMN_SEXO = "sexo";
    public static final String COLUMN_RAZA = "raza";
    public static final String COLUMN_IMAGEN_MASCOTA = "imagen_mascota";


    //Tabla Recordatorio
    public static final String COLUMN_MOTIVO_VISITA = "motivo_visita";
    public static final String COLUMN_FECHA_RECORDATORIO = "fecha_recordatorio";
    public static final String COLUMN_ESTADO = "estado";


    Context context;






    public BaseDeDatos(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //TABLA USUARIO
        String create_table_usuario = "CREATE TABLE IF NOT EXISTS  " + TABLE_USUARIO + "(" +
                COLUMN_ID_USUARIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                COLUMN_PASSWORD + " TEXT NOT NULL" + ")";
        db.execSQL(create_table_usuario);

        //TABLA MASCOTA
        String create_table_mascota = "CREATE TABLE IF NOT EXISTS  " + TABLE_MASCOTA + "(" +
                COLUMN_ID_MASCOTA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE_MASCOTA + " TEXT NOT NULL, " +
                COLUMN_FECHA_NACIMIENTO + " TEXT NOT NULL, " +
                COLUMN_RAZA + " TEXT NOT NULL, " +
                COLUMN_SEXO + " TEXT, " +
                COLUMN_IMAGEN_MASCOTA + " TEXT," +
                COLUMN_ID_USUARIO + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ID_USUARIO + ") REFERENCES " + TABLE_USUARIO + "(" + COLUMN_ID_USUARIO + ")" +
                ")";
        db.execSQL(create_table_mascota);

//        //TABLA RECORDATORIO
//        String create_table_recordatorio = "CREATE TABLE IF NOT EXISTS " + TABLE_RECORDATORIO + "(" +
//                COLUMN_ID_RECORDATORIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COLUMN_MOTIVO_VISITA + " TEXT NOT NULL, " +
//                COLUMN_FECHA_RECORDATORIO + " TEXT NOT NULL, " +
//                COLUMN_ESTADO + " TEXT NOT NULL, " +
//                COLUMN_ID_USUARIO + " INTEGER, " +
//                COLUMN_ID_REGISTRO + " INTEGER, " +
//                "FOREIGN KEY(" + COLUMN_ID_USUARIO + ") REFERENCES " + TABLE_USUARIO + "(" + COLUMN_ID_USUARIO + "), " +
//                "FOREIGN KEY(" + COLUMN_ID_REGISTRO + ") REFERENCES " + TABLE_REGISTRO + "(" + COLUMN_ID_REGISTRO + ")" +
//                ")";
//        db.execSQL(create_table_recordatorio);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public int obtenerIdUsuario(String email, String password) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        int idUsuario = -1;
//
//        String[] columns = {COLUMN_ID_USUARIO};
//        String selection = COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
//        String[] selectionArgs = {email, password};
//
//        Cursor cursor = db.query(TABLE_USUARIO, columns, selection, selectionArgs, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            int columnIndex = cursor.getColumnIndexOrThrow(COLUMN_ID_USUARIO);
//            idUsuario = cursor.getInt(columnIndex);
//        }
//
//        cursor.close();
//        db.close();
//        System.out.print(idUsuario);
//        return idUsuario;
//    }
}



