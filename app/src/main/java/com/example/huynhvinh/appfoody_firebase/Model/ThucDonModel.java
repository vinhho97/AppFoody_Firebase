package com.example.huynhvinh.appfoody_firebase.Model;

import android.util.Log;

import com.example.huynhvinh.appfoody_firebase.Controller.Interfaces.ThucDonInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Binh on 7/10/17.
 */

public class ThucDonModel {
    String mathucdon;
    String tenthucdon;
    List<MonAnModel> monAnModelList;

    public List<MonAnModel> getMonAnModelList() {
        return monAnModelList;
    }

    public void setMonAnModelList(List<MonAnModel> monAnModelList) {
        this.monAnModelList = monAnModelList;
    }

    public String getMathucdon() {
        return mathucdon;
    }

    public void setMathucdon(String mathucdon) {
        this.mathucdon = mathucdon;
    }

    public String getTenthucdon() {
        return tenthucdon;
    }

    public void setTenthucdon(String tenthucdon) {
        this.tenthucdon = tenthucdon;
    }

    public void getDanhSachThucDonQuanAnTheoMa(String maquanan, final ThucDonInterface thucDonInterface){

        DatabaseReference nodeThucDonQuanAn = FirebaseDatabase.getInstance().getReference().child("thucdonquanans").child(maquanan);
        nodeThucDonQuanAn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                final List<ThucDonModel> thucDonModels = new ArrayList<>();

                for (DataSnapshot valueThucDon : dataSnapshot.getChildren()){

                    final ThucDonModel thucDonModel = new ThucDonModel();

                    DatabaseReference nodeThucDon = FirebaseDatabase.getInstance().getReference().child("thucdons").child(valueThucDon.getKey());
                    nodeThucDon.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotThucDon) {
                            String mathucdon = dataSnapshotThucDon.getKey();
                            thucDonModel.setMathucdon(mathucdon);
                            thucDonModel.setTenthucdon(dataSnapshotThucDon.getValue(String.class));
                            List<MonAnModel> monAnModels = new ArrayList<>();

                            for (DataSnapshot valueMonAn : dataSnapshot.child(mathucdon).getChildren()){
                                MonAnModel monAnModel = valueMonAn.getValue(MonAnModel.class);
                                monAnModel.setMamon(valueMonAn.getKey());

                                monAnModels.add(monAnModel);

                            }

                            thucDonModel.setMonAnModelList(monAnModels);

                            thucDonModels.add(thucDonModel);

                            thucDonInterface.getThucDonThanhCong(thucDonModels);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
