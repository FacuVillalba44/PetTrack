package com.devfacu.pettrack.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.devfacu.pettrack.EditarVacunaActivity;
import com.devfacu.pettrack.R;
import com.devfacu.pettrack.VacunasMascotaActivity;
import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.db.DbVacuna;
import com.devfacu.pettrack.entidades.Mascota;
import com.devfacu.pettrack.entidades.Vacuna;

import java.util.ArrayList;

public class ListaVacunasAdapter extends RecyclerView.Adapter<ListaVacunasAdapter.ContactoViewHolder> {

    private ArrayList<Vacuna> listaVacuna;
    private Context context;
    private int id_mascota;
    private OnVacunaEliminadaListener onVacunaEliminadaListener;

    public interface OnVacunaEliminadaListener {
        void onVacunaEliminada();
    }

    public ListaVacunasAdapter(Context context, ArrayList<Vacuna> listaVacuna, int id_mascota) {
        this.context = context;
        this.listaVacuna = listaVacuna;
        this.id_mascota = id_mascota;
    }

    public void setOnVacunaEliminadaListener(OnVacunaEliminadaListener listener) {
        this.onVacunaEliminadaListener = listener;
    }

    @NonNull
    @Override
    public ListaVacunasAdapter.ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_vacunas_lauout, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaVacunasAdapter.ContactoViewHolder holder, int position) {
        Vacuna vacuna = listaVacuna.get(position);

        int id_vacuna = vacuna.getId_vacuna();

        String nombre_mascota = obtenerNombreMascota(id_mascota);
        holder.textViewNombreVacuna.setText(vacuna.getNombre_vacuna());
        holder.textViewFechaAplicacion.setText("Aplicación: " + vacuna.getFecha_aplicacion());
        holder.textViewProximaAplicacion.setText("Próx Aplicación: " + vacuna.getProxima_aplicacion());
        holder.nombreMascota.setText(nombre_mascota);

        holder.btnEditarVacuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditarVacunaActivity.class);
                intent.putExtra("id_vacuna", vacuna.getId_vacuna());
                intent.putExtra("nombre_vacuna", vacuna.getNombre_vacuna());
                intent.putExtra("fecha_aplicacion", vacuna.getFecha_aplicacion());
                intent.putExtra("proxima_aplicacion", vacuna.getProxima_aplicacion());

                // Pasa la información de la mascota a la actividad de edición
                intent.putExtra("id_mascota", vacuna.getId_mascota());
                intent.putExtra("nombre_mascota", obtenerNombreMascota(id_mascota) != null ? obtenerNombreMascota(id_mascota) : "Desconocida");

                context.startActivity(intent);
            }
        });

        holder.btnEliminarVacuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarDialogoEliminarVacuna(vacuna);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaVacuna.size();
    }

    public class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreVacuna;
        TextView textViewFechaAplicacion;
        TextView textViewProximaAplicacion;
        TextView nombreMascota;
        Button btnEditarVacuna;
        Button btnEliminarVacuna;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreMascota = itemView.findViewById(R.id.nombreMascota);
            textViewNombreVacuna = itemView.findViewById(R.id.textViewNombreVacuna);
            textViewFechaAplicacion = itemView.findViewById(R.id.textViewFechaAplicacion);
            textViewProximaAplicacion = itemView.findViewById(R.id.textViewProximaAplicacion);
            btnEditarVacuna = itemView.findViewById(R.id.btnEditarVacuna);
            btnEliminarVacuna = itemView.findViewById(R.id.btnEliminarVacuna);
        }
    }

    private String obtenerNombreMascota(int idMascota) {
        DbMascota dbMascota = new DbMascota(context);
        Mascota mascota = dbMascota.verDetallesMascota(idMascota);
        if (mascota != null) {
            return mascota.getNombre();
        } else {
            return "Desconocido";
        }
    }

    private void mostrarDialogoEliminarVacuna(Vacuna vacuna) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Eliminar Vacuna")
                .setMessage("¿Estás seguro que deseas eliminar esta vacuna?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarVacuna(vacuna.getId_vacuna());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void eliminarVacuna(int id_vacuna) {
        DbVacuna dbVacuna = new DbVacuna(context);

        try {
            // Elimina la vacuna
            dbVacuna.eliminarVacuna(id_vacuna);
            Toast.makeText(context, "Vacuna eliminada", Toast.LENGTH_SHORT).show();

            // Informa a la actividad anterior que la eliminación fue exitosa
            if (onVacunaEliminadaListener != null) {
                onVacunaEliminadaListener.onVacunaEliminada();
            }

        } catch (Exception ex) {
            Log.e("ListaVacunasAdapter", "Error al eliminar vacuna: " + ex.getMessage(), ex);
            Toast.makeText(context, "Error al eliminar la vacuna", Toast.LENGTH_SHORT).show();
        }
    }
}