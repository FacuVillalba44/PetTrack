package com.devfacu.pettrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devfacu.pettrack.db.DbUsuario;
import com.devfacu.pettrack.entidades.Usuario;

import java.util.regex.Pattern;

public class ModificarPerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil);

        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);

        Button buttonGuardarCambios = findViewById(R.id.buttonGuardarCambios);

        Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        if(usuario != null){
            editTextName.setText(usuario.getNombre());
            editTextEmail.setText(usuario.getEmail());
            editTextPassword.setText(usuario.getPassword());
            editTextRepeatPassword.setText(usuario.getPassword());
        }

        buttonGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los nuevos valores ingresados por el usuario
                String nuevoNombre = editTextName.getText().toString();
                String nuevoEmail = editTextEmail.getText().toString();
                String nuevaPassword = editTextPassword.getText().toString();
                String repetirPassword = editTextRepeatPassword.getText().toString();

                if (nuevoNombre.isEmpty() || nuevoEmail.isEmpty() || nuevaPassword.isEmpty() || repetirPassword.isEmpty()) {
                    Toast.makeText(ModificarPerfilActivity.this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(nuevoEmail)) {
                    Toast.makeText(ModificarPerfilActivity.this, "Formato de correo electrónico no válido", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!nuevaPassword.equals(repetirPassword)) {
                    Toast.makeText(ModificarPerfilActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                DbUsuario dbUsuario = new DbUsuario(ModificarPerfilActivity.this);
                dbUsuario.getWritableDatabase();

                // Obtener el ID del usuario desde el objeto Usuario
                long idUsuario = usuario.getId();

                // Actualizar la base de datos con los nuevos valores
                int filasAfectadas = dbUsuario.editarUsuario(idUsuario, nuevoNombre, nuevoEmail, nuevaPassword);


                if (filasAfectadas > 0) {
                    Toast.makeText(ModificarPerfilActivity.this, "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent(ModificarPerfilActivity.this, PerfilActivity.class);
                    resultIntent.putExtra("edicionExitosa", true);
                    setResult(ModificarPerfilActivity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(ModificarPerfilActivity.this, "Error al intentar modificar los datos", Toast.LENGTH_SHORT).show();
                }

                dbUsuario.close();
            }
        });
    }
    private boolean isValidEmail(String nuevoEmail) {
        return PatternsCompat.EMAIL_ADDRESS.matcher(nuevoEmail).matches();
    }
}