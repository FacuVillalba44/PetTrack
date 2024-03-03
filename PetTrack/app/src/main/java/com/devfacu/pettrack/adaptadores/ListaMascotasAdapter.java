package com.devfacu.pettrack.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.devfacu.pettrack.PerfilMascotaActivity;
import com.devfacu.pettrack.R;
import com.devfacu.pettrack.entidades.Mascota;

import java.util.ArrayList;

public class ListaMascotasAdapter extends RecyclerView.Adapter<ListaMascotasAdapter.ContactoViewHolder> {

    private ArrayList<Mascota> listaMascota;
    private Context context;

    public ListaMascotasAdapter(Context context, ArrayList<Mascota> listaMascota) {
        this.context = context;
        this.listaMascota = listaMascota;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_mascota_layout, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        // Verificar que la lista y el índice sean válidos
        if (listaMascota != null && position >= 0 && position < listaMascota.size()) {
            if (holder != null) {

                // Verificar que las vistas en ContactoViewHolder no sean nulas
                if (holder.imagenPerfil != null && holder.botonPerfil != null) {

                    holder.nombreMascota.setText(listaMascota.get(position).getNombre());

                    String imagenUri = listaMascota.get(position).getImagen();
                    cargarImagen(holder.imagenPerfil, imagenUri);

                    holder.botonPerfil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int adapterPosition = holder.getBindingAdapterPosition();

                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                Mascota mascotaSeleccionada = listaMascota.get(adapterPosition);
                                int id_mascota = mascotaSeleccionada.getId_mascota();

                                Intent intent = new Intent(context, PerfilMascotaActivity.class);
                                intent.putExtra("id_mascota", id_mascota);
                                intent.putExtra("imagen", mascotaSeleccionada.getImagen());
                                context.startActivity(intent);
                            }
                        }
                    });

                } else {
                    Log.e("Adapter", "ImageView o Button en ContactoViewHolder son nulos");
                }

            } else {
                Log.e("Adapter", "ContactoViewHolder es nulo");
            }

        } else {
            Log.e("Adapter", "La lista de mascotas es nula o el índice está fuera de límites");
        }
    }

    @Override
    public int getItemCount() {
        return listaMascota.size();
    }

    private void cargarImagen(ImageView imageView, String imagenUri) {
        Uri uri = Uri.parse(imagenUri);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error);

        Glide.with(context)
                .load(uri)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Error loading image: " + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    public class ContactoViewHolder extends RecyclerView.ViewHolder {

        ImageView imagenPerfil;
        TextView nombreMascota;
        Button botonPerfil;
        TextView fechaNacimiento;
        TextView especie;
        TextView raza;
        TextView sexo;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenPerfil = itemView.findViewById(R.id.imageViewPerfilMascota);
            nombreMascota = itemView.findViewById(R.id.textViewNombreMascota);
            botonPerfil = itemView.findViewById(R.id.buttonPerfilMascota);
            fechaNacimiento = itemView.findViewById(R.id.textViewFecNac);
            especie = itemView.findViewById(R.id.textViewEspecie);
            raza = itemView.findViewById(R.id.textViewtRaza);
            sexo = itemView.findViewById(R.id.textViewSexo);
        }
    }
}