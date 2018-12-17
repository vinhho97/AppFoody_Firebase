package com.example.huynhvinh.appfoody_firebase.Controller;

import com.example.huynhvinh.appfoody_firebase.Model.BinhLuanModel;

import java.util.List;



public class BinhLuanController {
    BinhLuanModel binhLuanModel;

    public  BinhLuanController(){
        binhLuanModel = new BinhLuanModel();
    }

    public void ThemBinhLuan(String maQuanAn, BinhLuanModel binhLuanModel, List<String> listHinh){
        binhLuanModel.ThemBinhLuan(maQuanAn,binhLuanModel,listHinh);
    }
}
