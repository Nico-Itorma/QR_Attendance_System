package com.nicoitorma.qrattendancesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

/*
    ACTIVITY FOR VIEWING THE SCANNED QR DETAILS WHEN YOU CLICK AN ATTENDANCE
 */

public class Activity_Details extends AppCompatActivity {

    ScannedQR_Database scannedQRDatabase;
    List<DataModels> scannedQR;

    RecyclerView recyclerView;
    Adapter_Details adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_of_attendance);

        Intent i = getIntent();
        String dbName = i.getStringExtra("DBNAME");

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Scanned QR For " + dbName);
        setSupportActionBar(toolbar);

        scannedQRDatabase = new ScannedQR_Database(getApplicationContext(), dbName);
        //GET ALL THE DATA ON ATTENDANCE
        scannedQR = scannedQRDatabase.getAll();

        initUI(); //SETS THE RECYCLERVIEW OF SCANNED QR
    }

    public void initUI() {

        recyclerView = findViewById(R.id.details_list);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter_Details(scannedQR);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}