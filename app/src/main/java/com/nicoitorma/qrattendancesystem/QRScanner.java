package com.nicoitorma.qrattendancesystem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class QRScanner extends Fragment {

    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    Button add_attendance;
    TextView content;
    SurfaceView surfaceView;
    String QR_CONTENT = "";

    RecyclerView recyclerView;
    Adapter_ScnList adapter;
    List<DataModels> data;
    Attendance_Created_Database database;
    RecyclerView.LayoutManager layoutManager;
    DataModels att;
    private static final int REQUEST_CAMERA_PERMISSION = 100;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_qr_scanner, container, false);
        add_attendance = view.findViewById(R.id.add_attendance);
        surfaceView = view.findViewById(R.id.cameraView);
        content = view.findViewById(R.id.tv_qr_info);
        recyclerView = view.findViewById(R.id.list_in_scanner);

        initUI();

        return view;
    }

    private void initUI()
    {
        database = new Attendance_Created_Database(getContext());
        data = database.getAllData();

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter_ScnList(data);
        recyclerView.setLayoutManager(layoutManager);
        final String DEFAULT_ATTENDANCE_BUTTON = "ADD TO ATTENDANCE";

        add_attendance.setOnClickListener(view -> {
            if (adapter.getItemCount() == 0)
            {
                Toast.makeText(getContext(), "No attendance selected", Toast.LENGTH_SHORT).show();
                content.setText("");
            }
        });

        adapter.setOnClick(new Adapter_ScnList.OnCheckListener() {
            @Override
            public void onChecked(int position) {
                att = data.get(position);
            }

            @Override
            public void isChecked(boolean isChecked) {
                if (isChecked)        //CHECK IF THERE IS CLICKED ATTENDANCE
                {
                    final String EDITED_ATTENDANCE_BUTTON = "ADD TO " + att.getActivity_name();
                    add_attendance.setText(EDITED_ATTENDANCE_BUTTON);

                    add_attendance.setOnClickListener(view -> {
                        if (content.length() != 0)
                        {
                            String mData = content.getText().toString();                            //GETS THE DATA OF SCANNED QR CODE
                            Date currentTime = Calendar.getInstance().getTime();                    //GET THE CURRENT DATE AND TIME THE SCAN WAS CREATED
                            String time = currentTime.toString();                                   //STORE THE VALUE OF CURRENT TIME TO A STRING
                            String dbName = att.getActivity_name();                                 // GETS THE NAME OF THE ATTENDANCE
                            ScannedQR_Database database1 = new ScannedQR_Database(getContext(), dbName);
                            DataModels data = new DataModels(1, mData, time);                 //CREATE A TABLE OF DATABASE CREATED
                            database1.addAttendance(data);                                          //ADDS THE DATA TO THE TABLE CREATED BASED ON THE NAME OF ATTENDANCE

                            content.setText("");
                            Toast.makeText(getContext(), "Added: \n" + mData + '\n' + "on " + dbName, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(), "No scanned QR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    add_attendance.setText(DEFAULT_ATTENDANCE_BUTTON);
                    add_attendance.setOnClickListener(view -> {
                        Toast.makeText(getContext(), "No attendance selected", Toast.LENGTH_SHORT).show();
                        content.setText("");
                    });
                }
            }
        });

        recyclerView.setAdapter(adapter);

        initDetectorsAndSources();
        startScanning();
    }

    private void initDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //camera auto focus immediately
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    } else {
                        cameraSource.start(surfaceView.getHolder());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        startScanning();
    }

    private void startScanning() {
        content.setText(QR_CONTENT); // clear the text on text view
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            public void release() {
            }

            //FOR WHEN SCANNING A QR CODE
            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> codes = detections.getDetectedItems();
                if (codes.size() != 0) {

                    content.post(() -> {
                        codes.valueAt(0);
                        content.removeCallbacks(null);
                        QR_CONTENT = codes.valueAt(0).rawValue;
                        add_attendance.setVisibility(View.VISIBLE);
                        content.setText(QR_CONTENT);
                    });
                }
            }
        });
    }
}
