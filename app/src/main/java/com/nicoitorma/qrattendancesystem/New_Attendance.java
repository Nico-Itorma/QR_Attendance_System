package com.nicoitorma.qrattendancesystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

/*
    FRAGMENT WHEN YOU CLICK THE NEW ATTENDANCE ON HOME SCREEN
 */

public class New_Attendance extends Fragment implements View.OnClickListener {

    EditText attendance_name, attendance_dept;
    Button create;
    Button attendance_date, attendance_login;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_attendance, container, false);
        MainActivity.instance.getSupportActionBar().setTitle("New Attendance");
        MainActivity.instance.hideBottomVisibility();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        attendance_name = view.findViewById(R.id.newAct_et);
        attendance_dept = view.findViewById(R.id.et_dept_new);
        attendance_date = view.findViewById(R.id.new_date_et);
        attendance_login = view.findViewById(R.id.login_time_et);
        create = view.findViewById(R.id.btn_create_activity);
        create.setOnClickListener(this);

        attendance_date.setOnClickListener(view12 -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year12, month12, dayOfMonth) -> {
                month12 += 1;
                String date = month12 + "/" + dayOfMonth + "/" + year12;
                attendance_date.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });

        attendance_login.setOnClickListener(view1 -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hourOfDay, minutesOfDay) -> {
                String time = hourOfDay + ":" + minutesOfDay;
                attendance_login.setText(time);
            }, 12, 0, true);
            timePickerDialog.show();
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        Attendance_Created_Database database = new Attendance_Created_Database(getContext());

        if (attendance_name.length() == 0) {
            attendance_name.setError("Required Field");
        } else {
            try {
                //Add the info of new attendance to DB "AttendanceFragmentDB"
                String Att_name = attendance_name.getText().toString().toUpperCase().trim();
                String Att_date = attendance_date.getText().toString();
                String Att_dept = attendance_dept.getText().toString().toUpperCase().trim();
                String Att_time = attendance_login.getText().toString();

                if ((Att_date.length() == 0) || (Att_dept.length() == 0) || (Att_time.length() == 0)) {
                    if (Att_date.length() == 0) {
                        Att_date = "N/A";
                    }
                    if (Att_dept.length() == 0) {
                        Att_dept = "N/A";
                    }
                    if (Att_time.length() == 0) {
                        Att_time = "N/A";
                    }
                }
                DataModels attendanceData = new DataModels(1, Att_name, Att_date, Att_time, Att_dept);
                database.addAttendance(attendanceData);
                String toast_message = attendance_name.getText().toString().toUpperCase().trim();
                Toast.makeText(getContext(), "Attendance: \"" + toast_message + "\" successfully created", Toast.LENGTH_SHORT).show();

                //RESETS THE TEXT FIELDS
                attendance_name.setText("");
                attendance_date.setText("");
                attendance_dept.setText("");
                attendance_login.setText("");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
