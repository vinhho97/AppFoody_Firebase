package com.example.huynhvinh.appfoody_firebase.Model;

public class ThemDatMonAnModel {
    String temonan;
    int soluong;
    String hinhanh;
    double tongtien;

    public double getTongtien() {
        return tongtien;
    }

    public void setTongtien(double tongtien) {
        this.tongtien = tongtien;
    }

    public String getTemonan() {
        return temonan;
    }

    public void setTemonan(String temonan) {
        this.temonan = temonan;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
