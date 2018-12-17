package com.example.huynhvinh.appfoody_firebase.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huynhvinh.appfoody_firebase.Adapters.AdapterBinhLuan;
import com.example.huynhvinh.appfoody_firebase.Controller.ThucDonController;
import com.example.huynhvinh.appfoody_firebase.Model.QuanAnModel;
import com.example.huynhvinh.appfoody_firebase.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChiTietQuanAnActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {
    TextView txtTenQuanAn,txtDiaChi,txtThoiGianHoatDong,txtTrangThaiHoatDong,txtTongSoHinhAnh,txtTongSoBinhLuan,txtTieuDeToolbar,txtGiaQuanAn,txtXemMonDaDat;
    ImageView imgHinhQuanAn;
    QuanAnModel quanAnModel;
    Toolbar toolbar;
    LinearLayout linearLayoutChiNhanhQuanAn;
    RecyclerView recyclerView;
    NestedScrollView nestedScrollViewChiTiet;
    GoogleMap googleMap;
    MapFragment mapFragment;

    RecyclerView recyclerThucDon;
    ThucDonController thucDonController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_chitietquanan);
        AnhXa();
        quanAnModel = getIntent().getParcelableExtra("quanan");


        // Xem danh sách món ăn đã đặt
        txtXemMonDaDat.setOnClickListener(this);
        HienThiChiTietQuanAn();
    }

    private void AnhXa() {
        txtXemMonDaDat = (TextView) findViewById(R.id.txtXemMonDaDat);
        txtGiaQuanAn = (TextView) findViewById(R.id.txtGiaQuanAn);
        txtTenQuanAn = (TextView) findViewById(R.id.txtTenQuanAn);
        txtThoiGianHoatDong = (TextView) findViewById(R.id.txtThoiGianHoatDong);
        txtTrangThaiHoatDong = (TextView) findViewById(R.id.txtTrangThaiHoatDong);
        txtTongSoHinhAnh = (TextView) findViewById(R.id.txtTongSoHinhAnh);
        txtTongSoBinhLuan = (TextView) findViewById(R.id.txtTongSoBinhLuan);

        imgHinhQuanAn = (ImageView) findViewById(R.id.imageHinhQuanAn);
        txtTieuDeToolbar = (TextView) findViewById(R.id.txtTieuDeToolbar);

        linearLayoutChiNhanhQuanAn = (LinearLayout) findViewById(R.id.linearDiaChi);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerBinhLuanChiTietQuanAn);
        // Lấy google map
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // end google map
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("");
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // lấy nút mũi tên
       getSupportActionBar().setDisplayShowHomeEnabled(true);

       recyclerThucDon = (RecyclerView) findViewById(R.id.recyclerThucDon);
       thucDonController = new ThucDonController();

    }

    // Hàm hỗ trợ bắt sự kiện click vào nút mũi tên trên toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    // end


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu_binhluan,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.itBinhLuan:
                Intent iBinhLuan = new Intent(ChiTietQuanAnActivity.this,BinhLuanActivity.class);
                iBinhLuan.putExtra("maquanan",quanAnModel.getMaquanan());
                iBinhLuan.putExtra("tenquan",quanAnModel.getTenquanan());
                iBinhLuan.putExtra("diachi",quanAnModel.getChiNhanhQuanAnModelList().get(0).getDiachi());
                startActivity(iBinhLuan);
                break;
        }
        return true;
    }

    @SuppressLint("ResourceType")
    private  void HienThiChiTietQuanAn(){
        // Lấy ngày giờ hiện tại để kiểm tra quán đang đóng của hay mở cửa
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:MM");
        String giohientai = dateFormat.format(calendar.getTime());
        String giomocua = quanAnModel.getGiomocua();
        String giodongcua = quanAnModel.getGiodongcua();

        try {
            Date dateHienTai = dateFormat.parse(giohientai);
            Date dateMoCua =  dateFormat.parse(giomocua);
            Date dateDongCua  = dateFormat.parse(giodongcua);
            // Kiểm tra thời gian hiện tại cửa hàng có đang mở cửa hay không
            if(dateHienTai.after(dateMoCua) && dateHienTai.before(dateDongCua))
            {
                txtTrangThaiHoatDong.setText(getResources().getString(R.string.DangMoCua));
            }
            else {
                txtTrangThaiHoatDong.setText(getResources().getString(R.string.DongCua));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtTieuDeToolbar.setText(quanAnModel.getTenquanan());
        txtTenQuanAn.setText(quanAnModel.getTenquanan());
        txtThoiGianHoatDong.setText(quanAnModel.getGiomocua() +  " - " + quanAnModel.getGiodongcua() );
        txtTongSoHinhAnh.setText(quanAnModel.getHinhanhquanans().size()  + "");
        txtTongSoBinhLuan.setText(quanAnModel.getBinhLuanModelList().size() + "");
        txtGiaQuanAn.setText(getResources().getString(R.string.GiaQuanAn) + " : " + quanAnModel.getGiatoithieu() + " - " + quanAnModel.getGiatoida());

        // Show ra chi nhánh quán ăn
        for(int i=0; i< quanAnModel.getChiNhanhQuanAnModelList().size(); i++)
        {
            txtDiaChi = new TextView(ChiTietQuanAnActivity.this);
            txtDiaChi.setText( "Chi nhánh " +  (i+1)  +" : " + quanAnModel.getChiNhanhQuanAnModelList().get(i).getDiachi());
            txtDiaChi.setTextColor(getResources().getInteger(R.color.colorPrimaryDark));
            linearLayoutChiNhanhQuanAn.addView(txtDiaChi);
        }

        // Lấy ảnh của quán ăn
        StorageReference storageHinhAnhQuanAn = FirebaseStorage.getInstance().getReference().child("hinhanh").child(quanAnModel.getHinhanhquanans().get(0));
        long mege_byte  =1024*1024*5;
        storageHinhAnhQuanAn.getBytes(mege_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imgHinhQuanAn.setImageBitmap(bitmap);
            }
        });

        // Load danh sách bình luận quán ăn
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        AdapterBinhLuan adapterBinhLuan = new AdapterBinhLuan(this,R.layout.custom_layout_binhluan,quanAnModel.getBinhLuanModelList());
        recyclerView.setAdapter(adapterBinhLuan);
        adapterBinhLuan.notifyDataSetChanged();

        // Code để nestscrollView load lên vị trí đầu
        nestedScrollViewChiTiet = (NestedScrollView) findViewById(R.id.nestScrollViewChiTiet);
        nestedScrollViewChiTiet.smoothScrollTo(0,0);

        // Lấy mã quán để biết ai quán nào được đặt món ăn
        String maquanan = quanAnModel.getMaquanan();
        thucDonController.getDanhSachThucDonQuanAnTheoMa(ChiTietQuanAnActivity.this,quanAnModel.getMaquanan(),recyclerThucDon,maquanan);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        double latitude = quanAnModel.getChiNhanhQuanAnModelList().get(0).getLatitude();
        double longtitude = quanAnModel.getChiNhanhQuanAnModelList().get(0).getLongtitude();

        LatLng latLng = new LatLng(latitude,longtitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(quanAnModel.getTenquanan());
        googleMap.addMarker(markerOptions);

        // Zoom tới vị trí của chúng ta
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,14);
        googleMap.moveCamera(cameraUpdate);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.txtXemMonDaDat:
                if(quanAnModel.isGiaohang()) {
                    Intent iDanhSachMonAn = new Intent(ChiTietQuanAnActivity.this, DanhSachMonAnDaDatActivity.class);
                    startActivity(iDanhSachMonAn);
                }
                else
                {
                    Toast.makeText(this,getResources().getString(R.string.KiemTraGiaoHang), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}