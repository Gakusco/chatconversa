package com.jcortiz.chatconversa;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcortiz.chatconversa.Retrofit.respuestasWS.DataMensaje;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.MensajeWS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> implements View.OnClickListener {

    private ArrayList<DataMensaje> modelo;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    public Adaptador(Context context, ArrayList<DataMensaje> modelo) {
        this.inflater = LayoutInflater.from(context);
        this.modelo = modelo;
    }

    @NonNull
    @Override
    public Adaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mensaje_recibido, parent, false);
        view.setOnClickListener(this);
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
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return modelo.size();
    }

    @Override
    public void onClick(View view) {
        if (listener!=null) {
           listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreMensaje;
        TextView cuerpoMensaje;
        TextView horaMensaje;
        ImageView fotoMensaje;
        ImageView imagenDeChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreMensaje = itemView.findViewById(R.id.textNombreUsuario);
            cuerpoMensaje = itemView.findViewById(R.id.textMensaje);
            horaMensaje = itemView.findViewById(R.id.textHoraDelMensaje);
            fotoMensaje = itemView.findViewById(R.id.imgPerfilChat);
            imagenDeChat = itemView.findViewById(R.id.imagenDeChat);
        }
    }
}
