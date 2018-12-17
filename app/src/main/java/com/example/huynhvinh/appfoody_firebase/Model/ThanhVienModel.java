package com.example.huynhvinh.appfoody_firebase.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class ThanhVienModel {
    String role;
    String hoten;
    String hinhanh;
    String mathanhvien;
    public String getMathanhvien() {
        return mathanhvien;
    }

    public void setMathanhvien(String mathanhvien) {
        this.mathanhvien = mathanhvien;
    }


    private  DatabaseReference dataNodeThanhVien;   // priviate để việc setValue không set biến này vì mặc định nó chỉ set những biến mang giá trị public
    public ThanhVienModel(){
        dataNodeThanhVien = FirebaseDatabase.getInstance().getReference().child("thanhviens");
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public  void ThemThongTinThanhVien(ThanhVienModel thanhVienModel,String uid)
    {
            dataNodeThanhVien.child(uid).setValue(thanhVienModel);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}