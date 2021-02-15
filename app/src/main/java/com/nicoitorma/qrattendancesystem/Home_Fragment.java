package com.nicoitorma.qrattendancesystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/*
    FRAGMENT FOR HOME MENU
 */
public class Home_Fragment extends Fragment implements View.OnClickListener {

    View btn_scan, btn_newAct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen, container, false);
        MainActivity.instance.toolbar.setTitle("Home");
        btn_scan = view.findViewById(R.id.btn_scan);
        btn_newAct = view.findViewById(R.id.btn_new_activity);
        btn_scan.setOnClickListener(this);
        btn_newAct.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                MainActivity.instance.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new QRScanner())
                        .commit();
                break;
            case R.id.btn_new_activity:
                MainActivity.instance.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new New_Attendance())
                        .commit();
                break;
        }
    }
}