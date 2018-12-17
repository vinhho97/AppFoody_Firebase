package com.example.huynhvinh.appfoody_firebase.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.huynhvinh.appfoody_firebase.Adapters.AdapterRecyclerOdau;
import com.example.huynhvinh.appfoody_firebase.Controller.Interfaces.OdauInterface;
import com.example.huynhvinh.appfoody_firebase.Model.ChiNhanhQuanAnModel;
import com.example.huynhvinh.appfoody_firebase.Model.QuanAnModel;
import com.example.huynhvinh.appfoody_firebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OdauController {

    Context context;
    QuanAnModel quanAnModel;
    AdapterRecyclerOdau adapterRecyclerOdau;
    int itemdaco = 3;
    int bientam = itemdaco;
    final List<QuanAnModel> quanAnModelList = new ArrayList<>();

    public OdauController(Context context){
        this.context = context;
        quanAnModel = new QuanAnModel();
    }

    public void getDanhSachQuanAn(final Context context, NestedScrollView nestedScrollView, final RecyclerView recyclerOdau, final ProgressBar progressBar, final Location vitrihientai, final int khoangcach){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerOdau.setLayoutManager(layoutManager);
        adapterRecyclerOdau = new AdapterRecyclerOdau(context,quanAnModelList, R.layout.custom_layout_recyclerview_odau,khoangcach);
        recyclerOdau.setAdapter(adapterRecyclerOdau);

        progressBar.setVisibility(View.VISIBLE);
        final OdauInterface odauInterface = new OdauInterface() {
            @Override
            public void getDanhSachQuanAnModel(final QuanAnModel quanAnModel) {
                final List<Bitmap> bitmaps = new ArrayList<>();
                for(String linkhinh : quanAnModel.getHinhanhquanans()){
                    StorageReference storageHinhAnh = FirebaseStorage.getInstance().getReference().child("hinhanh").child(linkhinh);
                    long ONE_MEGABYTE = 1024 * 1024*5;
                    storageHinhAnh.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                            bitmaps.add(bitmap);
                            quanAnModel.setBitmapList(bitmaps);
                            // kiểm tra list bitmap có bằng số lượng hình ảnh của quán hay không (nếu bằng nhau thì xuất ra 1 lần để không lặp lại 2 lần)
                            if(quanAnModel.getBitmapList().size() == quanAnModel.getHinhanhquanans().size()){
                                ChiNhanhQuanAnModel chiNhanhQuanAnModel = quanAnModel.getChiNhanhQuanAnModelList().get(0);
                                for(int i=0;i<quanAnModel.getChiNhanhQuanAnModelList().size();i++)
                                {
                                    if(chiNhanhQuanAnModel.getKhoangcach() > quanAnModel.getChiNhanhQuanAnModelList().get(i).getKhoangcach())
                                    {
                                        chiNhanhQuanAnModel = quanAnModel.getChiNhanhQuanAnModelList().get(i);
                                    }
                                }
                                if(chiNhanhQuanAnModel.getKhoangcach() < khoangcach) {
                                    quanAnModelList.add(quanAnModel);
                                    adapterRecyclerOdau.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                        }
                    });
                }
            }
        };
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getChildAt(v.getChildCount() - 1) !=null){
                    if(scrollY >= (v.getChildAt(v.getChildCount() - 1)).getMeasuredHeight() - v.getMeasuredHeight()){
                        itemdaco += 3;
                        bientam = itemdaco;
                        quanAnModel.getDanhSachQuanAn(odauInterface,vitrihientai,itemdaco,itemdaco-3);
                    }
                }
            }
        });

        quanAnModel.getDanhSachQuanAn(odauInterface,vitrihientai,itemdaco,0);

    }
}