package com.devfacu.pettrack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devfacu.pettrack.db.DbUsuario;
import com.devfacu.pettrack.entidades.Usuario;

public class PerfilActivity extends AppCompatActivity {

    private static final int TU_CODIGO_DE_SOLICITUD = 1; // Puedes elegir un valor específico

    private Usuario usuario;
    private DbUsuario dbUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewEmail = findViewById(R.id.textViewEmail);
        TextView textViewPassword = findViewById(R.id.textViewPassword);
        Button btnEditarUsuario = findViewById(R.id.btnEditarUsuario);
        Button btnEliminarUsuario = findViewById(R.id.btnEliminarUsuario);

        dbUsuario = new DbUsuario(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id_usuario")) {
            long idUsuario = intent.getLongExtra("id_usuario", -1);

            Log.d("PerfilActivity", "idUsuario recibido: " + idUsuario);

            usuario = dbUsuario.getUsuarioById(idUsuario);

            if (usuario != null) {
                textViewName.setText(usuario.getNombre());
                textViewEmail.setText(usuario.getEmail());
                textViewPassword.setText(usuario.getPassword());
            } else {
                textViewName.setText("El usuario no existe");
                textViewEmail.setText("");
                textViewPassword.setText("");
            }
        }

        btnEditarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this, ModificarPerfilActivity.class);
                intent.putExtra("usuario", usuario);
                startActivityForResult(intent, TU_CODIGO_DE_SOLICITUD);
            }
        });

        btnEliminarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);
                builder.setMessage("¿Desea eliminar su cuenta?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Eliminar al usuario de la base de datos
                                if (usuario != null) {
                                    dbUsuario.eliminarUsuario(usuario.getId());

                                    // Mostrar un mensaje de éxito o realizar alguna acción
                                    Toast.makeText(PerfilActivity.this, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();

                                    // Redirigir a la página de login
                                    Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                    // Cerrar la actividad actual
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // No hacer nada si el usuario elige no eliminar
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TU_CODIGO_DE_SOLICITUD && resultCode == RESULT_OK) {
            boolean edicionExitosa = data.getBooleanExtra("edicionExitosa", false);

            if (edicionExitosa) {
                // Recargamos la informacion del usuario luego de que se hayan editado los datos
                if (usuario != null) {
                    usuario = dbUsuario.getUsuarioById(usuario.getId());

                    if (usuario != null) {
                        TextView textViewName = findViewById(R.id.textViewName);
                        TextView textViewEmail = findViewById(R.id.textViewEmail);
                        TextView textViewPassword = findViewById(R.id.textViewPassword);

                        textViewName.setText(usuario.getNombre());
                        textViewEmail.setText(usuario.getEmail());
                        textViewPassword.setText(usuario.getPassword());
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbUsuario.close();
    }
}