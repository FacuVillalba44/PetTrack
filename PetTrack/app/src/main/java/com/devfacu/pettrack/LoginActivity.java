package com.devfacu.pettrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    Button logearse;
    ImageButton volver;
    EditText emailUsuario, claveUsuario;
    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailUsuario=findViewById(R.id.etEmailUsuario_L);
        claveUsuario = findViewById(R.id.etClaveUsuario_L);
        logearse = findViewById(R.id.btnIniciaSesion);
        volver = findViewById(R.id.imgBtnVolver_L);

        //btn iniciar sesion
        logearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarUsuarioL(emailUsuario,claveUsuario)){
                    Intent home = new Intent(LoginActivity.this, Home_Activity.class);
                    finish();
                    startActivity(home);
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
    private boolean validarUsuarioL(EditText et_email, EditText et_pass) {
        String email = et_email.getText().toString().trim();
        String pass = et_pass.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            // Mostrar mensaje de error
            Toast.makeText(getApplicationContext(), "Por favor, complete todos los campos", Toast.LENGTH_LONG).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Mostrar mensaje de error
            Toast.makeText(getApplicationContext(), "Ingrese un e-mail válido", Toast.LENGTH_LONG).show();
            return false;
        } else {
            // Los campos están completos y el email es válido inicia sesion
            Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}