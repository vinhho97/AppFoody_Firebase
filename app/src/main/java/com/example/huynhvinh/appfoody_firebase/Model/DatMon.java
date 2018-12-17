package com.example.huynhvinh.appfoody_firebase.Model;

public class DatMon {

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

    public String getMamonan() {
        return mamonan;
    }

    public void setMamonan(String mamonan) {
        this.mamonan = mamonan;
    }

    public String getMaquanan() {
        return maquanan;
    }

    public void setMaquanan(String maquanan) {
        this.maquanan = maquanan;
    }

    public long getSotien() {
        return sotien;
    }

    public void setSotien(long sotien) {
        this.sotien = sotien;
    }

    long sotien;
    String temonan;
    int soluong;
    String hinhanh;
    String mamonan;
    String maquanan;
}
