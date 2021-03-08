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
    DATABASE FOR SCANNED QR
 */

public class ScannedQR_Database extends SQLiteOpenHelper {

  public static final String DB_NAME = "ScannedQR";
  public static final String COLUMN_NAME = "DATA";
  public static final String COLUMN_DATE = "DATE";
  public static final String COLUMN_ID = "ID";

  public ScannedQR_Database(@Nullable Context context, String name) {
    super(context, name, null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    String createTable = "CREATE TABLE  IF NOT EXISTS " + DB_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_DATE + " TEXT)";
    db.execSQL(createTable);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

  }

  public void addAttendance(DataModels scannedQR) {
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues cv = new ContentValues();

    cv.put(COLUMN_NAME, scannedQR.getData_qr());
    cv.put(COLUMN_DATE, scannedQR.getTime_qr());

    database.insert(DB_NAME, null, cv);
  }

  public List<DataModels> getAll() {
    List<DataModels> returnList = new ArrayList<>();

    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM " + DB_NAME;

    Cursor cursor = db.rawQuery(query, null);

    if (cursor.moveToFirst())
    {
      do
      {
        int id = cursor.getInt(0);
        String data = cursor.getString(1);
        String time = cursor.getString(2);

        DataModels newData = new DataModels(id, data, time);
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

  public Cursor raw() {

    SQLiteDatabase db = this.getReadableDatabase();
    return db.rawQuery("SELECT * FROM " + DB_NAME , new String[]{});
  }
}