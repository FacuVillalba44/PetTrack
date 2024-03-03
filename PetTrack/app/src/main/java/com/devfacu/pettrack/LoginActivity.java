package com.devfacu.pettrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devfacu.pettrack.db.DbUsuario;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private Button cancelarButton;
    private Button registrarButton;
    private EditText email;
    private EditText password;
    private SQLiteDatabase db;
    private DbUsuario dbUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Context context = this;

        DbUsuario db= new DbUsuario(context);
        SQLiteDatabase bd = db.getWritableDatabase();

        loginButton = findViewById(R.id.buttonInicioSesion);
        cancelarButton = findViewById(R.id.buttonCancelar);
        registrarButton = findViewById(R.id.buttonRegistrar);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_usuario= email.getText().toString();
                String password_usuario = password.getText().toString();

                if (email_usuario.equals("") || password_usuario.equals("")){
                    Toast.makeText(LoginActivity.this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkDatos = db.checkEmailPassword(email_usuario, password_usuario);
                    if(checkDatos){
                        Toast.makeText(LoginActivity.this, "Inicio exitoso", Toast.LENGTH_SHORT).show();

                        int usuarioId = db.obtenerIdUsuario(email_usuario, password_usuario);
                        Log.d("LoginActivity", "UsuarioId obtenido: " + usuarioId);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("id_usuario", usuarioId);
                        Log.d("LoginActivity", "Antes de iniciar MainActivity, usuarioId: " + usuarioId);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Datos invalidos", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText("");
                password.setText("");
            }
        });

        registrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroUsuarioActivity.class);
                startActivity(intent);
            }
        });

    }
}