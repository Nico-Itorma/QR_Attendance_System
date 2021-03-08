package com.nicoitorma.qrattendancesystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import static android.content.Context.MODE_PRIVATE;

public class Settings extends Fragment {

    TextView tv_dept_name, contact;
    ImageButton edit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_settings, container, false);
        tv_dept_name = v.findViewById(R.id.tv_dept_name);
        contact = v.findViewById(R.id.contactNum_tv);
        edit = v.findViewById(R.id.imageButton);

        SharedPreferences preferences = MainActivity.instance.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String text = preferences.getString("text", "School Department");
        String contactNum = preferences.getString("contactNum", "");
        tv_dept_name.setText(text);
        contact.setText(contactNum);

        edit.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), update_settings.class);
            intent.putExtra("deptName", text);
            intent.putExtra("contact", contactNum);
            startActivity(intent);
        });
        return v;
    }
}