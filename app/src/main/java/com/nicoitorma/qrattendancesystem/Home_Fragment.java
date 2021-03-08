package com.nicoitorma.qrattendancesystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.content.Context.MODE_PRIVATE;

/*
    FRAGMENT FOR HOME MENU
 */
public class Home_Fragment extends Fragment implements View.OnClickListener {

    View btn_newAct, btn_genqr, btn_qrdb, btn_attrep;
    TextView tv_dept;
    Fragment fragment;

    public Home_Fragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen, container, false);
        MainActivity.instance.getSupportActionBar().setTitle("QR Attendance System");
        SharedPreferences preferences = MainActivity.instance.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String text = preferences.getString("text", "School Department");
        tv_dept = view.findViewById(R.id.tv_college_dept);
        tv_dept.setText(text);
        btn_newAct = view.findViewById(R.id.btn_new_activity);
        btn_genqr = view.findViewById(R.id.btn_generate);
        btn_qrdb = view.findViewById(R.id.btn_qrdb);
        btn_attrep = view.findViewById(R.id.btn_attrep);
        btn_newAct.setOnClickListener(this);
        btn_genqr.setOnClickListener(this);
        btn_qrdb.setOnClickListener(this);
        btn_attrep.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_new_activity:
                fragment = new New_Attendance();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment
                                , fragment.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.btn_generate:
                fragment = new GenQr();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment
                                , fragment.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.btn_qrdb:
                fragment = new DB_Fragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment
                                , fragment.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.btn_attrep:
                fragment = new Attendance_Fragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment
                                , fragment.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}