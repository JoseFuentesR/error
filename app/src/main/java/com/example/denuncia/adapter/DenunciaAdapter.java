package com.example.denuncia.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.denuncia.DetalleActivity;
import com.example.denuncia.R;
import com.example.denuncia.model.Denuncia;
import com.example.denuncia.model.Usuarios;

import java.util.List;
//adapter para la class de ver denuncias encargado y usuarios.
public class DenunciaAdapter extends RecyclerView.Adapter<DenunciaAdapter.DenunciaHolder> {
    List<Denuncia> list;
    int layout;
    Activity activity;

    public DenunciaAdapter(List<Denuncia> list, int layaut, Activity activity) {
        this.list = list;
        this.layout = layaut;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DenunciaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new DenunciaHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DenunciaHolder holder, int position) {
        Denuncia denuncia = list.get(position);
        holder.txttipo.setText(denuncia.getTipo_denuncia());
        holder.txtdireccion.setText(denuncia.getDireccion());
        holder.txtnombre.setText(denuncia.getTitulo_denuncia());
        holder.txtestado.setText(denuncia.getEstado_denuncia());
        holder.txtfecha.setText(denuncia.getFecha());
        holder.txtdetalle.setText(denuncia.getDenuncia_detalles());
        Glide.with(activity).load(denuncia.getUrl()).into(holder.imagen);
    }

    @Override
    public int getItemCount() {return list.size(); }

    public class DenunciaHolder extends RecyclerView.ViewHolder{
        TextView txtnombre,txtdireccion,txtestado,txttipo,txtdetalle,txtfecha;
        ImageView imagen;
        RelativeLayout container;
        public DenunciaHolder(@NonNull View itemView) {
            super(itemView);
            txttipo = itemView.findViewById(R.id.tipo);
            txtdetalle = itemView.findViewById(R.id.descripcion);
            txtdireccion = itemView.findViewById(R.id.direccionDenuncia);
            txtestado = itemView.findViewById(R.id.estadoDenuncia);
            txtnombre = itemView.findViewById(R.id.nombreDenuncia);
            txtfecha= itemView.findViewById(R.id.fecha);
            imagen = itemView.findViewById(R.id.img_item);
            container = itemView.findViewById(R.id.item_container);


            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, DetalleActivity.class);
                    intent.putExtra("NOMBRE",txtnombre.getText().toString());
                    intent.putExtra("DIRECCION",txtdireccion.getText().toString());
                    intent.putExtra("DETALLE",txtdetalle.getText().toString());
                    intent.putExtra("ESTADO",txtestado.getText().toString());
                    intent.putExtra("TIPO",txttipo.getText().toString());


                    activity.startActivity(intent);
                }
            });
        }

    }

}
