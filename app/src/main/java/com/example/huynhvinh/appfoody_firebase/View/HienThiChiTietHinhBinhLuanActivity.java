package com.example.huynhvinh.appfoody_firebase.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.huynhvinh.appfoody_firebase.Adapters.AdapterRecyclerHinhBinhLuan;
import com.example.huynhvinh.appfoody_firebase.Model.BinhLuanModel;
import com.example.huynhvinh.appfoody_firebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HienThiChiTietHinhBinhLuanActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView txtTieuDeBinhLuan,txtNoiDungBinhLuan,txtSoDiem;
    RecyclerView recyclerViewHinhBinhLuan;
    BinhLuanModel binhLuanModel;

    List<Bitmap> bitmapList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout_binhluan);
        AnhXa();

        txtTieuDeBinhLuan.setText(binhLuanModel.getTieude());
        txtNoiDungBinhLuan.setText(binhLuanModel.getNoidung());
        txtSoDiem.setText(binhLuanModel.getChamdiem() + "");
        setHinhAnhBinhLuan(circleImageView, "user.png");

        // Duyệt hình đưa vào list Bitmap
        for (String linkhinh : binhLuanModel.getHinhanhBinhLuanList()) {
            StorageReference storageHinhUser = FirebaseStorage.getInstance().getReference().child("hinhanh").child(linkhinh);
            long mege_byte = 1024 * 1024;
            storageHinhUser.getBytes(mege_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    bitmapList.add(bitmap);
                    // Kiểm tra số hình trong bitmap có bằng số hình trong hình của 1 mình bình. Việc này để tránh adapter thực hiện nhiều lần. Nếu duyệt đến khi bằng nhau thì đó là hình cuối cùng của bình luận đó và chỉ thực hiện 1 lần
                    if (bitmapList.size() == binhLuanModel.getHinhanhBinhLuanList().size()) {
                        // Load lên ảnh lên recyclerView
                        AdapterRecyclerHinhBinhLuan adapterRecyclerHinhBinhLuan = new AdapterRecyclerHinhBinhLuan(HienThiChiTietHinhBinhLuanActivity.this, R.layout.custom_layout_hinhbinhluan, bitmapList, binhLuanModel,true); // truyền bình luận model để xử lý việc load hình ảnh bên adapter
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(HienThiChiTietHinhBinhLuanActivity.this, 2);
                        recyclerViewHinhBinhLuan.setLayoutManager(layoutManager);
                        recyclerViewHinhBinhLuan.setAdapter(adapterRecyclerHinhBinhLuan);
                        adapterRecyclerHinhBinhLuan.notifyDataSetChanged();
                    }
                }
            });
        }
    }
    private  void setHinhAnhBinhLuan(final CircleImageView circleImageView, String linkhinh){
        StorageReference storageHinhUser = FirebaseStorage.getInstance().getReference().child("thanhvien").child(linkhinh);
        long mege_byte  =1024*1024;
        storageHinhUser.getBytes(mege_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                circleImageView.setImageBitmap(bitmap);
            }
        });

    }

    private void AnhXa() {
        circleImageView = (CircleImageView) findViewById(R.id.circleImageUser);
        txtTieuDeBinhLuan = (TextView) findViewById(R.id.txtTieuDeBinhLuan);
        txtNoiDungBinhLuan = (TextView) findViewById(R.id.txtNoiDungBinhLuan);
        txtSoDiem = (TextView) findViewById(R.id.txtChamdiem);
        recyclerViewHinhBinhLuan = (RecyclerView) findViewById(R.id.recyclerHinhBinhLuan);

        binhLuanModel = getIntent().getParcelableExtra("binhluanmodel");
        bitmapList = new ArrayList<>();

    }
}
