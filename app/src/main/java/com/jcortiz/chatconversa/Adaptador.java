package com.jcortiz.chatconversa;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcortiz.chatconversa.Retrofit.respuestasWS.DataMensaje;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.MensajeWS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder>{

    private ArrayList<DataMensaje> modelo;
    private LayoutInflater inflater;

    public Adaptador(Context context, ArrayList<DataMensaje> modelo) {
        this.inflater = LayoutInflater.from(context);
        this.modelo = modelo;
    }

    @NonNull
    @Override
    public Adaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mensaje_recibido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.ViewHolder holder, int position) {
        String cuerpo = modelo.get(position).getMessage();
        String hora = modelo.get(position).getDate();
        String nombre = modelo.get(position).getUser().getUsername();
        String foto = modelo.get(position).getUser().getUserImage();
        String imagenDeChat = modelo.get(position).getImage();
        holder.nombreMensaje.setText(nombre);
        holder.horaMensaje.setText(hora);
        holder.cuerpoMensaje.setText(cuerpo);

        if(!imagenDeChat.isEmpty() && imagenDeChat != null) {
            holder.cuerpoMensaje.setVisibility(View.GONE);
            holder.imagenDeChat.setVisibility(View.VISIBLE);
            Picasso.get().load(imagenDeChat).into(holder.imagenDeChat);
        }

        if(!foto.isEmpty() && foto!=null){
            Picasso.get().load(foto).transform(new CropCircleTransformation()).into(holder.fotoMensaje);
        }

        holder.setOnClickListener();
    }


    @Override
    public int getItemCount() {
        return modelo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombreMensaje;
        TextView cuerpoMensaje;
        TextView horaMensaje;
        ImageView fotoMensaje;
        ImageView imagenDeChat;
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nombreMensaje = itemView.findViewById(R.id.textNombreUsuario);
            cuerpoMensaje = itemView.findViewById(R.id.textMensaje);
            horaMensaje = itemView.findViewById(R.id.textHoraDelMensaje);
            fotoMensaje = itemView.findViewById(R.id.imgPerfilChat);
            imagenDeChat = itemView.findViewById(R.id.imagenDeChat);
        }

        void setOnClickListener() {
            fotoMensaje.setOnClickListener(this);
            cuerpoMensaje.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.imgPerfilChat:
                    Toast.makeText(context,"Se realizó click en una imagen",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.textMensaje:
                    Toast.makeText(context,"Se a hecho click en el cuerpo del mensaje",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
