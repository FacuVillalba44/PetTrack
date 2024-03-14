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
    public static final String TABLE_VACUNA = "vacuna";

    //Constantes para claves primarias
    public static final String COLUMN_ID_REGISTRO = "id_registro";
    public static final String COLUMN_ID_USUARIO= "id_usuario";
    public static final String COLUMN_ID_MASCOTA = "id_mascota";
    public static final String COLUMN_ID_RECORDATORIO= "id_recordatorio";
    public static final String COLUMN_ID_VACUNA = "id_vacuna";



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

    public static final String COLUMN_IMAGEN_BLOB = "imagen_blob";

    //Tabla Recordatorio
    public static final String COLUMN_MOTIVO_VISITA = "motivo_visita";
    public static final String COLUMN_FECHA_RECORDATORIO = "fecha_recordatorio";
    public static final String COLUMN_ESTADO = "estado";

    //Tabla Vacuna
    public static final String COLUMN_NOMBRE_VACUNA = "nombre_vacuna";
    public static final String COLUMN_FECHA_APLICACION = "fecha_aplicacion";
    public static final String COLUMN_PROXIMA_APLICACION = "fecha_prox_aplicacion";
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
                COLUMN_ESPECIE + " TEXT, " +
                COLUMN_SEXO + " TEXT, " +
                COLUMN_IMAGEN_MASCOTA + " TEXT," +
                COLUMN_IMAGEN_BLOB + " BLOB, " +
                COLUMN_ID_USUARIO + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ID_USUARIO + ") REFERENCES " + TABLE_USUARIO + "(" + COLUMN_ID_USUARIO + ")" +
                ")";
        db.execSQL(create_table_mascota);

        //TABLA VACUNAS
        String create_table_vacuna = "CREATE TABLE IF NOT EXISTS " + TABLE_VACUNA + "(" +
                COLUMN_ID_VACUNA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE_VACUNA + " TEXT NOT NULL, " +
                COLUMN_FECHA_APLICACION + " TEXT NOT NULL, " +
                COLUMN_PROXIMA_APLICACION + " TEXT NOT NULL, " +
                COLUMN_ID_MASCOTA + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ID_MASCOTA + ") REFERENCES " + TABLE_MASCOTA + "(" + COLUMN_ID_MASCOTA + "))";

        db.execSQL(create_table_vacuna);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}



