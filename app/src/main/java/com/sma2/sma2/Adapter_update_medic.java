package com.sma2.sma2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_update_medic extends RecyclerView.Adapter<Adapter_update_medic.ViewHolderDatos> {

    private ArrayList<ejm_data_medicine> list_medic;
    private Activity activity;

    public Adapter_update_medic(ArrayList<ejm_data_medicine> list_medic, Activity activity){
        this.list_medic=list_medic;
        this.activity = activity;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_update_medicine,parent,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, final int position) {
        final ejm_data_medicine datos = list_medic.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_mod_medicine =new Intent(activity, Modif_medicine.class);
                intent_mod_medicine.putExtra("id", datos.getId());
                intent_mod_medicine.putExtra("Medicine",datos.getMedicine());
                intent_mod_medicine.putExtra("Doses",datos.getDoses());
                intent_mod_medicine.putExtra("Time",datos.getTime());
                activity.startActivity(intent_mod_medicine);
            }
        });
        //holder.id.setText(String.valueOf(list_medic.get(position).getId()));
        holder.dato.setText(list_medic.get(position).getMedicine());
        holder.doses.setText(String.valueOf(list_medic.get(position).getDoses()));
        holder.time.setText(String.valueOf(list_medic.get(position).getTime()));
    }

    @Override
    public int getItemCount() {
        return list_medic.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView dato,doses,time;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            //id=itemView.findViewById(R.id.id1);
            dato=itemView.findViewById(R.id.idmedicine1);
            doses=itemView.findViewById(R.id.iddose1);
            time=itemView.findViewById(R.id.idtime1);

        }
    }

}

