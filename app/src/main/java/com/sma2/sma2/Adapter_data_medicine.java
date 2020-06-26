package com.sma2.sma2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_data_medicine extends RecyclerView.Adapter<Adapter_data_medicine.ViewHolderDatos> {

    private ArrayList<ejm_data_medicine> list_medicine;

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
        int time=list_medicine.get(position).getTime();
        String hour;
        if (time>=10){
            hour=String.valueOf(time)+":00";
        }
        else{
            hour="0"+String.valueOf(time)+":00";
        }

        holder.doses.setText(hour);
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
