package com.example.huynhvinh.appfoody_firebase.Controller;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.huynhvinh.appfoody_firebase.Adapters.AdapterThucDon;
import com.example.huynhvinh.appfoody_firebase.Controller.Interfaces.ThucDonInterface;
import com.example.huynhvinh.appfoody_firebase.Model.ThucDonModel;


import java.util.List;


public class ThucDonController {
    ThucDonModel thucDonModel;

    public ThucDonController(){
        thucDonModel = new ThucDonModel();
    }

    public void getDanhSachThucDonQuanAnTheoMa(final Context context, String manquanan, final RecyclerView recyclerView, final String maquanan){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        ThucDonInterface thucDonInterface = new ThucDonInterface() {
            @Override
            public void getThucDonThanhCong(List<ThucDonModel> thucDonModelList) {

                AdapterThucDon adapterThucDon = new AdapterThucDon(context,thucDonModelList,maquanan);
                recyclerView.setAdapter(adapterThucDon);
                adapterThucDon.notifyDataSetChanged();
            }
        };
        thucDonModel.getDanhSachThucDonQuanAnTheoMa(manquanan,thucDonInterface);
    }
}
