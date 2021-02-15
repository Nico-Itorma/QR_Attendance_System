package com.nicoitorma.qrattendancesystem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
    FRAGMENT FOR ATTENDANCE ON NAVIGATION DRAWER
 */
public class Attendance_Fragment extends Fragment {

    RecyclerView recyclerView;
    Adapter_Attendance adapter;
    RecyclerView.LayoutManager layoutManager;

    List<DataModels> dataList;
    Attendance_Created_Database database;
    String dbName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        database = new Attendance_Created_Database(getContext());
        dataList = database.getAllData();

        TextView no_att = view.findViewById(R.id.noatt_tv);
        if (dataList.size() == 0)
        {
            no_att.setVisibility(View.VISIBLE);
        }
        else {
            no_att.setVisibility(View.GONE);
        }

        recyclerView = view.findViewById(R.id.attendance_listview);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter_Attendance(dataList);
        recyclerView.setLayoutManager(layoutManager);

        no_att.setOnClickListener(view1 -> {
            Toast toast = Toast.makeText(getContext(), "Please add new attendance", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });

        adapter.setOnClick(position -> {
            DataModels data = dataList.get(position);
            int id = data.getId_attendance();

            dbName = data.getActivity_name();
            Intent intent = new Intent(getContext(), Activity_Details.class);
            intent.putExtra("ID", id);
            intent.putExtra("DBNAME", dbName);
            startActivity(intent);
        });

        registerForContextMenu(recyclerView);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = Adapter_Attendance.getPosition();
        DataModels data = dataList.get(position);

        switch (item.getItemId())
        {
            case R.id.menu_export:
                //TODO: EXPORT ATTENDANCE AS EXCEL FILE
                Toast.makeText(getContext(), "Next TODO: Export into excel file", Toast.LENGTH_SHORT).show();
                return true;
                
            case R.id.menu_del_att:
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Permanently delete attendance?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        database.deleteData(data);                              //DELETE THE RECYCLERVIEW OF THE DB IN THE ATTENDANCE FRAGMENT
                        MainActivity.instance.deleteDatabase(dbName);           //DELETE THE DATABASE INSTANCE
                    MainActivity.instance.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Attendance_Fragment()).commit();
                }).setNegativeButton("No", null);

            AlertDialog builder = alert.create();
            builder.show();
            return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}