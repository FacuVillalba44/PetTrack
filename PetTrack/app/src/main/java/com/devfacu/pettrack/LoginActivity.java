package com.devfacu.pettrack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.devfacu.pettrack.db.DbUsuario;


public class LoginActivity extends AppCompatActivity {

    Button logearse;
    ImageButton volver;
    EditText emailUsuario, claveUsuario;
    private SQLiteDatabase db;
    private DbUsuario dbUsuario;

    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailUsuario = findViewById(R.id.etEmailUsuario_L);
        claveUsuario = findViewById(R.id.etClaveUsuario_L);
        logearse = findViewById(R.id.btnIniciaSesion);
        volver = findViewById(R.id.imgBtnVolver_L);


        Context context = this;
        dbUsuario = new DbUsuario(context);
        SQLiteDatabase bd = dbUsuario.getWritableDatabase();
        //btn iniciar sesion
        logearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailUsuario.getText().toString();
                String clave = claveUsuario.getText().toString();

                if (email.equals("") || clave.equals("")){
                    Toast.makeText(LoginActivity.this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkDatos = dbUsuario.checkEmailPassword(email, clave);
                    if (checkDatos){
                        Toast.makeText(LoginActivity.this, "Inicio exitoso", Toast.LENGTH_SHORT).show();
                        int id_usuario = dbUsuario.obtenerIdUsuario(email, clave);
                        SharedPreferences sharedPreferences = getSharedPreferences("com.example.myapp.PREFERENCES", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("id_usuario", id_usuario);
                        Log.d("Home activity", "UsuarioId guardado en preferencias: " + id_usuario);
                        editor.apply();
//                        Log.d("LoginActivity", "UsuarioId obtenido en login: " + id_usuario);

                        Intent home = new Intent(LoginActivity.this, Home_Activity.class);
                        home.putExtra("id_usuario", id_usuario);
                        startActivity(home);
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Datos invalidos", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        //btn para volver a inicio
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inicio = new Intent(LoginActivity.this, inicioActivity.class);
                finish();
                startActivity(inicio);
            }
        });


        //_____Inicio de manejo del botón  de la barra de nativa para volver: Obtener el Dispatcher para el botón de "volver"
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        //_____Registrar un callback para manejar el botón de "volver"
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent volverAHome = new Intent(LoginActivity.this, inicioActivity.class);
                startActivity(volverAHome);
                finish();
            }
        });

    }//<llave de onCreate
}