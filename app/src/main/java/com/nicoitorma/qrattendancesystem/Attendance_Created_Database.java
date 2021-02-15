package com.nicoitorma.qrattendancesystem;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/*
    DATABASE OF CREATED NEW ATTENDANCE
 */
public class Attendance_Created_Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "ATTENDANCE";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_LOGIN = "LOGIN_TIME";
    public static final String COLUMN_DEPT = "DEPT";
    public static final String COLUMN_ID = "ID";


    public Attendance_Created_Database(@Nullable Context context) {
        super(context, "ATTENDANCE.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE  IF NOT EXISTS " + DB_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_DATE + " TEXT, " + COLUMN_LOGIN + " TEXT, " + COLUMN_DEPT + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addAttendance(DataModels attendanceData) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, attendanceData.getActivity_name());
        cv.put(COLUMN_DATE, attendanceData.getDate_att());
        cv.put(COLUMN_DEPT, attendanceData.getDept_attendance());
        cv.put(COLUMN_LOGIN, attendanceData.getLogin_att());

        database.insert(DB_NAME, null, cv);

        database.close();
    }

    public List<DataModels> getAllData() {
        List<DataModels> returnList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DB_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                int id = cursor.getInt(0);
                String att_name = cursor.getString(1);
                String att_date = cursor.getString(2);
                String att_login = cursor.getString(3);
                String att_dept = cursor.getString(4);

                DataModels newData = new DataModels(id, att_name, att_date, att_login, att_dept);
                returnList.add(newData);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public void deleteData(DataModels data) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + DB_NAME + " WHERE " + COLUMN_ID + " = " + data.getId_attendance();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        db.close();
    }
}