package com.nicoitorma.qrattendancesystem;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
   ADAPTER FOR THE RECYCLERVIEW OF CLICKED ATTENDANCE
 */

public class Adapter_Details extends RecyclerView.Adapter<Adapter_Details.MVH> {

    List<DataModels> scannedQRList;

    Adapter_Details(List<DataModels> list) {
        scannedQRList = list;
    }

    @NonNull
    @Override
    public MVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.atten_details_template, parent, false);
        return new MVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MVH holder, int position) {
        DataModels qr = scannedQRList.get(position);
        String name = qr.getData_qr();
        String time = "Time: " + qr.getTime_qr();
        String att_name = "Attendance Name: " + qr.getAtt_name_qr();
        holder.name_details.setText(name);
        holder.time_details.setText(time);
    }

    @Override
    public int getItemCount() {
        return scannedQRList.size();
    }

    public static class MVH extends RecyclerView.ViewHolder {

        TextView name_details;
        TextView time_details;

        public MVH(@NonNull View itemView) {
            super(itemView);

            name_details = itemView.findViewById(R.id.name_details_tv);
            time_details = itemView.findViewById(R.id.time_details_tv);
        }
    }
}