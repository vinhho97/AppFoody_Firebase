package com.example.huynhvinh.appfoody_firebase.View;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.huynhvinh.appfoody_firebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HienThiFullHinhAnhBinhLuanActivity extends AppCompatActivity {
    ImageView hinhbinhluan;
    List<byte[]> imageByteList = new ArrayList<byte[]>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hienthi_fullhinhanhbinhluan);
        AnhXa();

        final String hinhBinhLuan = getIntent().getStringExtra("HinhBinhLuan");


        StorageReference storageHinhUser = FirebaseStorage.getInstance().getReference().child("hinhanh").child(hinhBinhLuan);
        long mege_byte = 1024 * 1024;
        storageHinhUser.getBytes(mege_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                hinhbinhluan.setImageBitmap(bitmap);
            }
        });


    }

    private void AnhXa() {
    hinhbinhluan = (ImageView) findViewById(R.id.imgAnhBinhLuan);
    }
}
