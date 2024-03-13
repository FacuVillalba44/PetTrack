package com.devfacu.pettrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.devfacu.pettrack.db.DbUsuario;

public class RegistrarseActivity extends AppCompatActivity {
    private EditText etNombreUsuario, etEmail, etPass1, etPass2;
    Button btnRegistrarse;
    DbUsuario dbUsuario;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        etNombreUsuario=findViewById(R.id.etNombreUsuario);
        etEmail=findViewById(R.id.etEmailUsuario_R);
        etPass1=findViewById(R.id.etPassword1);
        etPass2=findViewById(R.id.etPassword2);
        btnRegistrarse = findViewById(R.id.btnRegistrarUsuario);

        dbUsuario = new DbUsuario(this);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarDatosL(etEmail,etNombreUsuario,etPass1,etPass2)){
                    String email = etEmail.getText().toString();
                    String nombre_usuario = etNombreUsuario.getText().toString();
                    String password = etPass1.getText().toString();

                    int id_usuario = dbUsuario.crearUsuario(nombre_usuario, email, password);
                    Log.d("LoginActivity", "UsuarioId obtenido: " + id_usuario);
                    if (id_usuario>0){
                        Toast.makeText(getApplicationContext(), "Bienvenido, inicia sesion con tu credenciales", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(RegistrarseActivity.this,LoginActivity.class);
                        login.putExtra("id_usuario", id_usuario);
                        startActivity(login);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Error al registrar el Ususario", Toast.LENGTH_SHORT).show();

                    }


                }
            }
        });
    //_____Inicio de manejo del botón  de la barra de nativa para volver: Obtener el Dispatcher para el botón de "volver"
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
    //_____Registrar un callback para manejar el botón de "volver"
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent volverAHome = new Intent(RegistrarseActivity.this, Home_Activity.class);
                startActivity(volverAHome);
                finish();
            }
        });
    }//<llave onCreate
    public void volver(View view){
        Intent volver = new Intent(this, inicioActivity .class);
        finish();
        startActivity(volver);
    }
    private boolean validarDatosL(EditText et_email, EditText et_nombre,EditText et_pass1, EditText et_pass2) {
        String email = et_email.getText().toString().trim();
        String nombre = et_nombre.getText().toString().trim();
        String pass1 = et_pass1.getText().toString().trim();
        String pass2 = et_pass2.getText().toString().trim();

        if (email.isEmpty()||nombre.isEmpty()||pass1.isEmpty()||pass2.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, complete todos los campos", Toast.LENGTH_LONG).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Ingrese un e-mail válido", Toast.LENGTH_LONG).show();
            return false;
        } else if (!pass1.equals(pass2)){
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return false;
        } else {
            Toast.makeText(getApplicationContext(), "Te has registrado correctamente", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}