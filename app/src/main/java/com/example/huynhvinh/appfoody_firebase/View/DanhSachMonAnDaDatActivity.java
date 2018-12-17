package com.example.huynhvinh.appfoody_firebase.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.huynhvinh.appfoody_firebase.Adapters.AdapterDanhSachMonAnDaDat;
import com.example.huynhvinh.appfoody_firebase.Adapters.AdapterMonAn;
import com.example.huynhvinh.appfoody_firebase.Model.DatMon;
import com.example.huynhvinh.appfoody_firebase.Model.ThemDatMonAnModel;
import com.example.huynhvinh.appfoody_firebase.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DanhSachMonAnDaDatActivity extends AppCompatActivity {
    Toolbar toolbarMonAnDaDat;
    List<DatMon> datMonList;
    List<ThemDatMonAnModel> themDatMonAnModelList;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    Button btnXacNhanDatMon;
    TextView txtTongTienMonAnDaDat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_monandadat);
        AnhXa();

        // get Danh sách món ăn đã đặt

        datMonList = AdapterMonAn.datMonList;

        final AdapterDanhSachMonAnDaDat adapterDanhSachMonAnDaDat = new AdapterDanhSachMonAnDaDat(DanhSachMonAnDaDatActivity.this,datMonList,R.layout.custom_layout_monandadat);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterDanhSachMonAnDaDat);
        adapterDanhSachMonAnDaDat.notifyDataSetChanged();


        // Tính tổng số tiền món ăn khách đã đặt
        long TongTien =0;

        for(DatMon datMon : datMonList)
        {
            TongTien+= (datMon.getSoluong()*datMon.getSotien());
        }

        txtTongTienMonAnDaDat.setText( getResources().getString(R.string.TongTien) + "  " +TongTien);

        // End tính tông tiền
        btnXacNhanDatMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemDatMonAn();
                datMonList.clear();
                adapterDanhSachMonAnDaDat.notifyDataSetChanged();
                Toast.makeText(DanhSachMonAnDaDatActivity.this,getResources().getString(R.string.DatMonThanhCong), Toast.LENGTH_SHORT).show();
                Intent iTrangChu = new Intent(DanhSachMonAnDaDatActivity.this,TrangChuActivity.class);
                startActivity(iTrangChu);
            }
        });
    }

    private void ThemDatMonAn() {
        for(int i=0;i<datMonList.size();i++)
        {
            ThemDatMonAnModel themDatMonAnModel = new ThemDatMonAnModel();
            themDatMonAnModel.setTemonan(datMonList.get(i).getTemonan());
            themDatMonAnModel.setHinhanh(datMonList.get(i).getHinhanh());
            themDatMonAnModel.setSoluong(datMonList.get(i).getSoluong());
            themDatMonAnModel.setTongtien(datMonList.get(i).getSoluong()*datMonList.get(i).getSotien());
            themDatMonAnModelList.add(themDatMonAnModel);
        }
        // Lấy id của người đăng nhập
        sharedPreferences = DanhSachMonAnDaDatActivity.this.getSharedPreferences("luudangnhap", Context.MODE_PRIVATE);
        String mauser = sharedPreferences.getString("mauser","error");
        String maQuanAn = datMonList.get(0).getMaquanan();
        for(int i=0; i<datMonList.size();i++) {
            FirebaseDatabase.getInstance().getReference().child("datmons").child(mauser).child(maQuanAn).push().child(datMonList.get(0).getMamonan()).setValue(themDatMonAnModelList.get(i));
        }
    }

    private void AnhXa() {
        txtTongTienMonAnDaDat = (TextView) findViewById(R.id.txtTongTienMonAnDaDat);
        btnXacNhanDatMon = (Button) findViewById(R.id.btnXacNhanDatMon);
        toolbarMonAnDaDat = (Toolbar) findViewById(R.id.toolbarMonAnDaDat);
        setSupportActionBar(toolbarMonAnDaDat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarMonAnDaDat.setTitle("");

        datMonList = new ArrayList<>();
        themDatMonAnModelList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMonAnDaDat);
    }

    // Hàm hỗ trợ bắt sự kiện click vào nút mũi tên trên toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // end
}
