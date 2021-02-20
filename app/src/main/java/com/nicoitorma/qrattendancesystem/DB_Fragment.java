package com.nicoitorma.qrattendancesystem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class DB_Fragment extends Fragment {

    RecyclerView recyclerView;
    Adapter_DB mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<DataModels> qrFiles;
    Gen_QR_Database dbHelper;
    OutputStream outputStream;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_database, container, false);

        dbHelper = new Gen_QR_Database(getContext());
        qrFiles = dbHelper.getData();

        TextView no_qr = view.findViewById(R.id.noqr);
        if (qrFiles.size() == 0)
        {
            no_qr.setVisibility(View.VISIBLE);
        }
        else {
            no_qr.setVisibility(View.GONE);
        }

        recyclerView = view.findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        mAdapter = new Adapter_DB(qrFiles);
        recyclerView.setLayoutManager(layoutManager);

        no_qr.setOnClickListener(view1 -> {
            Toast toast = Toast.makeText(getContext(), "Please generate new QR Code", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });

        registerForContextMenu(recyclerView);

        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = Adapter_DB.getPosition();
        DataModels qr_file = qrFiles.get(position);
        switch (item.getItemId()) {
            case R.id.menu_delete:
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Permanently delete generated QR?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            dbHelper.deleteData(qr_file);
                            MainActivity.instance.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DB_Fragment()).commit();
                            Toast.makeText(getContext(), "QR Deleted", Toast.LENGTH_SHORT).show();
                        }).setNegativeButton("No", null);

                AlertDialog builder = alert.create();
                builder.show();
            return true;

            case R.id.menu_save:
                Gen_QR_Database database = new Gen_QR_Database(getContext());

                byte[] bArray = database.getBlob(qr_file);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
                String fileName =  database.getName(qr_file) + ".png";
                File filepath = Environment.getExternalStorageDirectory();
                File dir = new File(filepath.getAbsolutePath() + "/QRSystem/Generated QR");
                if (!dir.exists() ) {
                    dir.mkdirs();
                }
                File file = new File(dir, fileName);
                try {
                    outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 99, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), "QR saved on QRSystem/Generated QR", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}