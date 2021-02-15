package com.nicoitorma.qrattendancesystem;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

public class GenQr extends Fragment implements View.OnClickListener {

    EditText name_et;
    EditText idNum_et;
    EditText dept_et;
    ImageView iv;

    Button generate;
    Button btn_save_qr;
    BitMatrix bitMatrix;
    Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_qr_generator, container, false);

        name_et = view.findViewById(R.id.Name_et);
        idNum_et = view.findViewById(R.id.idNum_et);
        dept_et = view.findViewById(R.id.dept_et);
        generate = view.findViewById(R.id.generate);
        iv = view.findViewById(R.id.iv_gen_qr);
        btn_save_qr = view.findViewById(R.id.btn_save_qr);
        generate.setOnClickListener(this);
        btn_save_qr.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        //data for generating qr
        String myDATA = "Name: " + name_et.getText().toString().toUpperCase().trim() + "\n" +
                "ID Number: " + idNum_et.getText().toString() + "\n" +
                "Department: " + dept_et.getText().toString().toUpperCase().trim();
        String ERROR_MESSAGE = "Required field";

        switch (view.getId()) {
            case R.id.generate: //this will generate new QR_code
                QRCodeWriter qrCodeWriter = new QRCodeWriter();

                //check if there's is a missing field
                if ((name_et.length() == 0 || idNum_et.length() == 0 || dept_et.length() == 0)) {
                    if (name_et.length() == 0) {
                        name_et.setError(ERROR_MESSAGE);
                    }
                    if (idNum_et.length() == 0) {
                        idNum_et.setError(ERROR_MESSAGE);
                    }
                    if (dept_et.length() == 0) {
                        dept_et.setError(ERROR_MESSAGE);
                    }
                } else {
                    //generate the qr code
                    try {
                        bitMatrix = qrCodeWriter.encode(myDATA, BarcodeFormat.QR_CODE, 250, 250);
                        bitmap = Bitmap.createBitmap(250, 250, Bitmap.Config.RGB_565);

                        for (int x = 0; x < 250; x++) {
                            for (int y = 0; y < 250; y++) {
                                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                            }
                        }
                        iv.setImageBitmap(bitmap); //set the generated qr to the image view

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_save_qr:
                if (iv.getDrawable() == null) {
                    Toast.makeText(getContext(), "NO QR CODE GENERATED YET", Toast.LENGTH_SHORT).show();
                } else if ((name_et.length() == 0 || idNum_et.length() == 0 || dept_et.length() == 0)) {
                    Toast.makeText(getContext(), "EMPTY FIELD/S", Toast.LENGTH_SHORT).show();
                } else {
                    //save qr code to database
                    Gen_QR_Database dbHelper = new Gen_QR_Database(getContext());

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 99, bos);
                    byte[] bArray = bos.toByteArray();
                    try {
                        DataModels newQrFiles = new DataModels(1, name_et.getText().toString().toUpperCase().trim(),
                                idNum_et.getText().toString().trim(),
                                dept_et.getText().toString().toUpperCase().trim(),
                                bArray);

                        dbHelper.insertData(newQrFiles);
                        Toast.makeText(getContext(), "ADDED SUCCESSFULLY!", Toast.LENGTH_SHORT).show();
                        name_et.setText("");
                        idNum_et.setText("");
                        dept_et.setText("");
                        iv.setImageBitmap(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
