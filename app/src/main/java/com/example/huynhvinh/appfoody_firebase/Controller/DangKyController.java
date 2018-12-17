package com.example.huynhvinh.appfoody_firebase.Controller;

import com.example.huynhvinh.appfoody_firebase.Model.ThanhVienModel;

public class DangKyController {
    ThanhVienModel thanhVienModel;
    public DangKyController(){
        thanhVienModel = new ThanhVienModel();
    }

    public void ThemThanhVienController(ThanhVienModel thanhVienModel, String uid){
        thanhVienModel.ThemThongTinThanhVien(thanhVienModel,uid);
    }

}
