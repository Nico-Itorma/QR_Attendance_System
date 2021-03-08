package com.nicoitorma.qrattendancesystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Settings extends Fragment {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    ImageView change_logo;
    EditText et_change_name;
    Button save_changes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);
        MainActivity.instance.getSupportActionBar().setTitle("Settings");

        change_logo = view.findViewById(R.id.change_logo);
        et_change_name = view.findViewById(R.id.changename_et);
        save_changes = view.findViewById(R.id.save_changes);

        change_logo.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "TODO: SELECT FROM STORAGE", Toast.LENGTH_SHORT).show();
        });

        save_changes.setOnClickListener(view1 -> {
            saveData();
            et_change_name.setText("");
            MainActivity.instance.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home_Fragment()).addToBackStack(null).commit();
        });

        return view;
    }

    private void saveData()
    {
        SharedPreferences preferences = MainActivity.instance.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(TEXT, et_change_name.getText().toString());
        editor.apply();
    }
}