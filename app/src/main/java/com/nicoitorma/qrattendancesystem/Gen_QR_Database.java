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
   DATABASE OF GENERATED QR CODES
 */
public class Gen_QR_Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "QR_DATA";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_ID_NUMBER = "ID_NUM";
    public static final String COLUMN_DEPT = "DEPT";
    public static final String COLUMN_QR_CODE = "QR_CODE";
    public static final String COLUMN_ID = "ID";

    public Gen_QR_Database(@Nullable Context context) {
        super(context, "GENERATED_QR.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE  IF NOT EXISTS " + DB_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_ID_NUMBER + " TEXT, " + COLUMN_DEPT + " TEXT, " + COLUMN_QR_CODE + " BLOB)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertData(DataModels qrFiles) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, qrFiles.getName_qrgen());
        cv.put(COLUMN_ID_NUMBER, qrFiles.getIdNum_qrgen());
        cv.put(COLUMN_DEPT, qrFiles.getDept_qrgen());
        cv.put(COLUMN_QR_CODE, qrFiles.getImg_qrgen());

        database.insert(DB_NAME, null, cv);

        database.close();
    }

    public byte[] getBlob (DataModels data) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_QR_CODE + " FROM " + DB_NAME + " WHERE " + COLUMN_ID + " = " + data.getId_qrgen();
        byte[] img = null;
        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst())
        {
            img = c.getBlob(0);
        }
        db.close();
        return img;
    }

    public String getName(DataModels data) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_NAME + " FROM " + DB_NAME + " WHERE " + COLUMN_ID + " = " + data.getId_qrgen();
        String name = null;

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst())
        {
            name = c.getString(0);
        }
        if (name != null) {
            db.close();
            return name.toLowerCase();
        }
        else {
            db.close();
            return null;
        }
    }

    public List<DataModels> getData()
    {
        List<DataModels> returnList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + DB_NAME;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String id_num = cursor.getString(2);
                String dept = cursor.getString(3);
                byte[] image = cursor.getBlob(4);

                DataModels newQRFiles = new DataModels(id, name, id_num, dept, image);
                returnList.add(newQRFiles);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return returnList;
    }

    public void deleteData(DataModels qrFiles) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + DB_NAME + " WHERE " + COLUMN_ID + " = " + qrFiles.getId_qrgen();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        db.close();
    }
}