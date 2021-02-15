package com.nicoitorma.qrattendancesystem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
    ADAPTER FOR RECYCLERVIEW OF DATABASE TAB ON NAVIGATION MENU
 */

public class Adapter_DB extends RecyclerView.Adapter<Adapter_DB.mViewHolder> {

    List<DataModels> qrFilesList;
    private static int position;

    Adapter_DB(List<DataModels> qrFiles)
    {
        qrFilesList = qrFiles;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.db_list_template, parent, false);
        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        DataModels qrFiles = qrFilesList.get(position);
        String name = "Name: " + qrFiles.getName_qrgen();
        String id_num = "ID Number: " + qrFiles.getIdNum_qrgen();
        String department = "Dept: " + qrFiles.getDept_qrgen();
        //CONVERT BYTE ARRAY BACK TO IMAGE
        byte[] qrImage = qrFiles.getImg_qrgen();
        Bitmap bitmap = BitmapFactory.decodeByteArray(qrImage, 0, qrImage.length);

        holder.name.setText(name);
        holder.idNum.setText(id_num);
        holder.dept.setText(department);
        holder.qrCode.setImageBitmap(bitmap);
        holder.itemView.setOnLongClickListener(view -> {
            setPosition(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return qrFilesList.size();
    }

    @Override
    public void onViewRecycled(@NonNull mViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public static class mViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView name;
        TextView idNum;
        TextView dept;
        ImageView qrCode;

        public mViewHolder (@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_tv);
            idNum = itemView.findViewById(R.id.idNum_tv);
            dept = itemView.findViewById(R.id.deparment_tv);
            qrCode = itemView.findViewById(R.id.qr_code_tv);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(Menu.NONE, R.id.menu_save, Menu.NONE, R.string.save_qr);
            contextMenu.add(Menu.NONE, R.id.menu_delete, Menu.NONE, R.string.delete_qr);
        }
    }

    public static int getPosition() {
        return position;
    }

    public static void setPosition(int pos) {
        position = pos;
    }
}