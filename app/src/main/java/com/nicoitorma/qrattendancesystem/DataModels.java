package com.nicoitorma.qrattendancesystem;

public class DataModels {
    int id_qrgen;
    String name_qrgen;
    String idNum_qrgen;
    String dept_qrgen;
    byte[] img_qrgen;

    public DataModels(int id_qrgen, String name_qrgen, String idNum_qrgen, String dept_qrgen, byte[] img_qrgen) {
        this.id_qrgen = id_qrgen;
        this.name_qrgen = name_qrgen;
        this.idNum_qrgen = idNum_qrgen;
        this.dept_qrgen = dept_qrgen;
        this.img_qrgen = img_qrgen;
    }

    public DataModels() {
    }

    public int getId_qrgen() { return id_qrgen; }

    public String getName_qrgen() { return name_qrgen; }

    public String getIdNum_qrgen() { return idNum_qrgen; }

    public String getDept_qrgen() { return dept_qrgen; }

    public byte[] getImg_qrgen() { return img_qrgen; }


    /*
        FOR ATTENDANCE
     */
    int id_attendance;
    String activity_name;
    String date_att;
    String login_att;
    String dept_attendance;

    public DataModels(int id_attendance, String activity_name, String date_att, String login_att, String dept_attendance) {
        this.id_attendance = id_attendance;
        this.activity_name = activity_name;
        this.date_att = date_att;
        this.login_att = login_att;
        this.dept_attendance = dept_attendance;
    }

    public int getId_attendance() { return id_attendance; }

    public String getActivity_name() { return activity_name; }

    public String getDate_att() { return date_att; }

    public String getLogin_att() { return login_att; }

    public String getDept_attendance() { return dept_attendance; }


    /*
        FOR SCANNED QR
     */
    int id_qr;
    String data_qr;
    String time_qr;
    String att_name_qr;

    public DataModels(int id_qr, String data_qr, String time_qr) {
        this.id_qr = id_qr;
        this.data_qr = data_qr;
        this.time_qr = time_qr;
    }

    public String getData_qr() { return data_qr; }

    public String getTime_qr() {return time_qr; }

    public String getAtt_name_qr() { return att_name_qr; }
}
