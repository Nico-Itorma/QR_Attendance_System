package com.nicoitorma.qrattendancesystem;

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

/*
    FRAGMENT WHEN YOU CLICK THE NEW ATTENDANCE ON HOME SCREEN
 */

public class New_Attendance extends Fragment implements View.OnClickListener {

    EditText attendance_name, attendance_dept, attendance_login, attendance_date;
    Button create;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_attendance, container, false);
        MainActivity.instance.toolbar.setTitle("New Attendance");
        MainActivity.instance.navView.setCheckedItem(R.id.menu_home);
        attendance_name = view.findViewById(R.id.newAct_et);
        attendance_dept = view.findViewById(R.id.et_dept_new);
        attendance_date = view.findViewById(R.id.new_date_et);
        attendance_login = view.findViewById(R.id.login_time_et);
        create = view.findViewById(R.id.btn_create_activity);
        create.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Attendance_Created_Database database = new Attendance_Created_Database(getContext());

        try {
            if ((attendance_name.length() == 0) || (attendance_dept.length() == 0) || (attendance_login.length() == 0))
            {
                attendance_name.setError("Required Field");
                attendance_dept.setError("Required Field");
                attendance_login.setError("Required Field");
            }
            else {

                //Add the info of new attendance to DB "AttendanceFragmentDB"
                DataModels attendanceData = new DataModels(1, attendance_name.getText().toString().toUpperCase().trim(),
                        attendance_date.getText().toString().toUpperCase().trim(),
                        attendance_login.getText().toString().trim(),
                        attendance_dept.getText().toString().toUpperCase().trim());
                database.addAttendance(attendanceData);

                String toast_message = attendance_name.getText().toString().toUpperCase().trim();
                Toast.makeText(getContext(), "Attendance: \"" + toast_message + "\" successfully created", Toast.LENGTH_SHORT).show();

                //RESETS THE TEXT FIELDS
                attendance_name.setText("");
                attendance_date.setText("");
                attendance_dept.setText("");
                attendance_login.setText("");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
