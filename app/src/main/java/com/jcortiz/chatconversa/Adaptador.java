package com.jcortiz.chatconversa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcortiz.chatconversa.Activities.MapsActivity;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.DataMensaje;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder>{

    private ArrayList<DataMensaje> modelo;
    private LayoutInflater inflater;
    private String user_id;
    private String idItemUser;

    public Adaptador(Context context, ArrayList<DataMensaje> modelo, String user_id) {
        this.inflater = LayoutInflater.from(context);
        this.modelo = modelo;
        this.user_id = user_id;
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
        String foto = modelo.get(position).getUser().getUserThumbnail();
        String imagenDeChat = modelo.get(position).getThumnail();
        String latitud = modelo.get(position).getLatitude();
        String longitud = modelo.get(position).getLongitude();

        holder.horaMensaje.setText(hora);
        holder.cuerpoMensaje.setText(cuerpo);
        if(Integer.parseInt(user_id) == modelo.get(position).getUser().getUserId()){
            holder.contenedorMensaje.setBackgroundColor(Color.CYAN);
            holder.nombreMensaje.setText("Yo");
        } else {
            holder.nombreMensaje.setText(nombre);
        }

        if(!imagenDeChat.isEmpty() && imagenDeChat != null) {
            holder.imagenDeChat.setVisibility(View.VISIBLE);
            Picasso.get().load(imagenDeChat).into(holder.imagenDeChat);
        }

        if(!foto.isEmpty() && foto!=null){
            Picasso.get().load(foto).transform(new CropCircleTransformation()).into(holder.fotoMensaje);
        }

        if ( latitud != null && longitud !=null && !latitud.isEmpty() && !longitud.isEmpty() ) {
            holder.cuerpoMensaje.setVisibility(View.GONE);
            holder.btnUbicacion.setVisibility(View.VISIBLE);
        }

        holder.setOnClickListener(modelo, position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
        Button btnUbicacion;
        ImageView imagenDeChat;
        RelativeLayout contenedorMensaje;
        Context context;
        ArrayList<DataMensaje> dataMensajes;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nombreMensaje = itemView.findViewById(R.id.textNombreUsuario);
            cuerpoMensaje = itemView.findViewById(R.id.textMensaje);
            horaMensaje = itemView.findViewById(R.id.textHoraDelMensaje);
            fotoMensaje = itemView.findViewById(R.id.imgPerfilChat);
            btnUbicacion = itemView.findViewById(R.id.btnUbicacion);
            imagenDeChat = itemView.findViewById(R.id.imagenDeChat);
            contenedorMensaje = itemView.findViewById(R.id.contenedorMensaje);
        }

        void setOnClickListener(ArrayList<DataMensaje> dataMensajes, int position) {
            this.dataMensajes = dataMensajes;
            this.position = position;
            fotoMensaje.setOnClickListener(this);
            imagenDeChat.setOnClickListener(this);
            btnUbicacion.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            switch(view.getId()) {
                case R.id.imgPerfilChat:
                    ampliarImagen(dataMensajes.get(position).getUser().getUserImage());
                    break;
                case R.id.imagenDeChat:
                    ampliarImagen(dataMensajes.get(position).getImage());
                    break;
                case R.id.btnUbicacion:
                    mostrarUbicacion(dataMensajes.get(position).getLatitude(), dataMensajes.get(position).getLongitude());
                    break;
            }
        }

        private void mostrarUbicacion(String latitude, String longitude) {
            Intent i = new Intent(context, MapsActivity.class);
            i.putExtra(Constantes.ENVIAR_LONGITUD, longitude);
            i.putExtra(Constantes.ENVIAR_LATITUD, latitude);
            context.startActivity(i);
        }

        private void ampliarImagen(String image) {
            if(!image.isEmpty() && image != null) {
                ImageView fotoFlotante;
                AlertDialog alertDialog;
                AlertDialog.Builder builder;
                View imagenFlotanteView;

                builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);

                imagenFlotanteView = LayoutInflater.from(context).inflate(R.layout.flotante_mostrar_imagen, null);
                fotoFlotante = imagenFlotanteView.findViewById(R.id.fotoAmpliada);

                Picasso.get().load(image).into(fotoFlotante);

                builder.setView(imagenFlotanteView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }

}
