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
import com.devfacu.pettrack.vacunas_mascota_Activity;
import com.devfacu.pettrack.db.DbMascota;
import com.devfacu.pettrack.db.DbVacuna;
import com.devfacu.pettrack.entidades.Mascota;
import com.devfacu.pettrack.entidades.Vacuna;

import java.util.ArrayList;

public class ListaVacunasAdapter extends RecyclerView.Adapter<ListaVacunasAdapter.ContactoViewHolder> {

    private ArrayList<Vacuna> listaVacuna;
    private Context context;
    private int id_mascota;

    public ListaVacunasAdapter(Context context, ArrayList<Vacuna> listaVacuna, int id_mascota) {
        this.context = context;
        this.listaVacuna = listaVacuna;
        this.id_mascota = id_mascota;
    }

    @NonNull
    @Override
    public ListaVacunasAdapter.ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_vacunas_layout, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaVacunasAdapter.ContactoViewHolder holder, int position) {
        Vacuna vacuna = listaVacuna.get(position);



        String nombre_mascota = obtenerNombreMascota(vacuna.getId_mascota());
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

                // Aquí se agrega el id_mascota al intent
                intent.putExtra("id_mascota", id_mascota);
                intent.putExtra("nombre_mascota", obtenerNombreMascota(vacuna.getId_mascota()) != null ? obtenerNombreMascota(vacuna.getId_mascota()) : "Desconocida");

                context.startActivity(intent);
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

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreMascota = itemView.findViewById(R.id.nombreMascota);
            textViewNombreVacuna = itemView.findViewById(R.id.textViewNombreVacuna);
            textViewFechaAplicacion = itemView.findViewById(R.id.textViewFechaAplicacion);
            textViewProximaAplicacion = itemView.findViewById(R.id.textViewProximaAplicacion);
            btnEditarVacuna = itemView.findViewById(R.id.btnEditarVacuna);

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




}