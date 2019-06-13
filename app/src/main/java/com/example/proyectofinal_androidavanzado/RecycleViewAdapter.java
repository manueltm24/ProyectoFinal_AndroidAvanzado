package com.example.proyectofinal_androidavanzado;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectofinal_androidavanzado.Clases.Proyecto;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.Holder> {
    private Context context;

    public void setListadoProyectos(List<Proyecto> listadoProyectos) {
        this.listadoProyectos = listadoProyectos;
    }

    private List<Proyecto> listadoProyectos;
    private Listener listener;

    public RecycleViewAdapter(Context context, List<Proyecto> listadoProyectos, Listener listener) {
        this.listadoProyectos = listadoProyectos;
        this.context = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_proyecto, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        Log.d("CONTADOR",String.valueOf(i));

        TextView textView_name = holder.itemView.findViewById(R.id.textView_nombre);
        TextView textView_remoto = holder.itemView.findViewById(R.id.textView_remoto);
        TextView textView_local = holder.itemView.findViewById(R.id.textView_oficina);
        TextView textView_description = holder.itemView.findViewById(R.id.textView_descripcion);
        TextView textView_fechaPublicacion = holder.itemView.findViewById(R.id.textView_fechaPublicacion);
        TextView textView_pagoHr = holder.itemView.findViewById(R.id.textView_pagoHr);
        textView_name.setText(listadoProyectos.get(i).getNombre());
        if(listadoProyectos.get(i).getDescripcion().length() > 70){
            textView_description.setText(listadoProyectos.get(i).getDescripcion().substring(0,70));

        }
        else{
            textView_description.setText(listadoProyectos.get(i).getDescripcion());

        }
        textView_fechaPublicacion.setText(listadoProyectos.get(i).getFechaPublicacion());

        if(listadoProyectos.get(i).isRemoto()){
            textView_remoto.setVisibility(View.VISIBLE);//set visibility to false on create
            textView_local.setVisibility(View.INVISIBLE);//set visibility to false on create
        }
        else {
            textView_remoto.setVisibility(View.INVISIBLE);//set visibility to false on create
            textView_local.setVisibility(View.VISIBLE);//set visibility to false on create
        }
        textView_pagoHr.setText("RD$"+String.format("%.2f", listadoProyectos.get(i).getPagoHora())+"/hr");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(listadoProyectos.get(i));
            }
        });


    }

    @Override
    public int getItemCount() {
        return listadoProyectos.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        public Holder(@NonNull View itemView) {
            super(itemView);
        }


    }

    public interface Listener{
        void onClick(Proyecto proyecto);
    }
}
