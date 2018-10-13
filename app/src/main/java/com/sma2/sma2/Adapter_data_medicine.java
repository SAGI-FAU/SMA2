package com.sma2.sma2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_data_medicine extends RecyclerView.Adapter<Adapter_data_medicine.ViewHolderDatos> {

    ArrayList<ejm_data_medicine> list_medicine;

    public Adapter_data_medicine(ArrayList<ejm_data_medicine> list_medicine) {
        this.list_medicine = list_medicine;
    }


    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine,parent,false);
        return new ViewHolderDatos(view);
        }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        holder.dato.setText(list_medicine.get(position).getMedicine());
        holder.doses.setText(String.valueOf(list_medicine.get(position).getDoses()));
        }

    @Override
    public int getItemCount() {
        return (list_medicine.size());
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView dato,doses;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            dato=itemView.findViewById(R.id.idmedicine);
            doses=itemView.findViewById(R.id.iddose);
        }
    }
}
