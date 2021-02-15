package com.nicoitorma.qrattendancesystem;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
    ADAPTER FOR RECYCLERVIEW ON ATTENDANCE DATABASE ON THE NAVIGATION MENU
 */

public class Adapter_Attendance extends RecyclerView.Adapter<Adapter_Attendance.AttViewHolder> {

    List<DataModels> attendanceDataList;
    private OnClickListener mClickListener;
    private OnLongCLick mLongClick;
    private static int position;

    Adapter_Attendance(List<DataModels> attendanceData) {
        attendanceDataList = attendanceData;
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    public void setOnClick(Adapter_Attendance.OnClickListener OnClickListener) {
        mClickListener = OnClickListener;
    }

    public interface OnLongCLick {
        void onLongClick(int position);
    }

    public void setOnLongClick(Adapter_Attendance.OnLongCLick mLongClick) {
        this.mLongClick = mLongClick;
    }

    @NonNull
    @Override
    public AttViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.atten_list_template, parent, false);
        return new AttViewHolder(v, mClickListener, mLongClick);
    }

    @Override
    public void onBindViewHolder(@NonNull AttViewHolder holder, int position) {
        DataModels data = attendanceDataList.get(position);
        String name = "Attendance name: " + data.getActivity_name();
        String date = "Date: " + data.getDate_att();
        String dept = "Department: " + data.getDept_attendance();
        String time = "Login Time: " + data.getLogin_att();

        holder.att_name.setText(name);
        holder.att_date.setText(date);
        holder.att_dept.setText(dept);
        holder.att_login.setText(time);
        holder.itemView.setOnLongClickListener(view -> {
            setPosition(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return attendanceDataList.size();
    }

    @Override
    public void onViewRecycled(@NonNull AttViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public static class AttViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView att_name, att_date, att_dept, att_login;

        public AttViewHolder(@NonNull View itemView, final OnClickListener click, final OnLongCLick longCLick) {
            super(itemView);

            att_name = itemView.findViewById(R.id.attendance_name_tv);
            att_date = itemView.findViewById(R.id.attendance_date_tv);
            att_dept = itemView.findViewById(R.id.attendance_dept_tv);
            att_login = itemView.findViewById(R.id.attendance_time_tv);
            itemView.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener((view -> {
                if (click != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        click.onClick(position);
                    }
                }
            }));

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(Menu.NONE, R.id.menu_export, Menu.NONE, R.string.export_attendance);
            contextMenu.add(Menu.NONE, R.id.menu_del_att, Menu.NONE, R.string.delete_attendance);
        }
    }

    public static int getPosition() {
        return position;
    }

    public static void setPosition(int pos) {
        position = pos;
    }
}