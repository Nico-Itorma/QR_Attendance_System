package com.nicoitorma.qrattendancesystem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    DataModels data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        MainActivity.instance.getSupportActionBar().setTitle("Attendance Report");
        MainActivity.instance.hideBottomVisibility();
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
        data = dataList.get(position);

        switch (item.getItemId())
        {
            case R.id.menu_export:
                new attendance_export().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    @SuppressLint("StaticFieldLeak")
    public class attendance_export extends AsyncTask<String, Void, Boolean> {

        private final ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting Attendance...");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String dbName = data.getActivity_name();
            ScannedQR_Database database = new ScannedQR_Database(getContext(), dbName);
            String filename = dbName + ".csv";
            File filepath = Environment.getExternalStorageDirectory();
            File dir = new File(filepath.getAbsolutePath() + "/QR System/Exported Attendance");
            if (!dir.exists() ) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            try {
                CSV_Writer csvWriter = new CSV_Writer(new FileWriter(file));
                Cursor cursor = database.raw();
                csvWriter.writeNext(cursor.getColumnNames());
                while (cursor.moveToNext()) {
                    String[] arr = new String[cursor.getColumnNames().length];
                    for (int i = 0; i < cursor.getColumnNames().length; i++) {
                        arr[i] = cursor.getString(i);
                    }
                    csvWriter.writeNext(arr);
                }
                csvWriter.close();
                cursor.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (this.dialog.isShowing()) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    dialog.dismiss();
                    if (success) {
                        Toast.makeText(getContext(), "Exported to QRSystem/Exported Attendance", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Export Failed", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        }
    }
}