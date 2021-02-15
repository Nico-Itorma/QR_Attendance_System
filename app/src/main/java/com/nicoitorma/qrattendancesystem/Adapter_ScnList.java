package com.nicoitorma.qrattendancesystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
    RECYCLERVIEW ADAPTER ON SCAN QR ACTIVITY FOR LIST OF ATTENDANCE CREATED
 */

public class Adapter_ScnList extends RecyclerView.Adapter<Adapter_ScnList.ScanVH> {

    List<DataModels> attendanceDataList;
    private OnCheckListener checkListener;

    public interface OnCheckListener{
        void onChecked(int position);
        void isChecked(boolean b);
    }
    public void setOnClick(OnCheckListener checkListener) {
        this.checkListener = checkListener;
    }

    Adapter_ScnList(List<DataModels> attendanceData) {
        attendanceDataList = attendanceData;
    }

    @NonNull
    @Override
    public ScanVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.atten_listview_template, parent, false);
        return new ScanVH(v, checkListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanVH holder, int position) {
        DataModels data = attendanceDataList.get(position);
        holder.name.setText(data.getActivity_name());
        holder.date.setText(data.getDate_att());
        holder.time.setText(data.getLogin_att());
//        holder.dept.setText(data.getDept_attendance());
    }

    @Override
    public int getItemCount() {
        return attendanceDataList.size();
    }

    public static class ScanVH extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView date;
        public TextView time;
        public TextView dept;
        CheckBox checkBox;

        public ScanVH(@NonNull View itemView, final OnCheckListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.list_name_tv);
            date = itemView.findViewById(R.id.list_date_tv);
            time = itemView.findViewById(R.id.list_time_tv);
//            dept = itemView.findViewById(R.id.list_dept_tv);
            checkBox = itemView.findViewById(R.id.checkbox);

            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onChecked(position);
                        listener.isChecked(b);
                    }
                }
            });
        }
    }
}