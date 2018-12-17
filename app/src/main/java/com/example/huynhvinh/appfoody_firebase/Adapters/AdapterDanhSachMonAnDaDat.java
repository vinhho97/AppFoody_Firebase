package com.example.huynhvinh.appfoody_firebase.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huynhvinh.appfoody_firebase.Model.DatMon;
import com.example.huynhvinh.appfoody_firebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdapterDanhSachMonAnDaDat extends RecyclerView.Adapter<AdapterDanhSachMonAnDaDat.ViewHolderMonAnDaDat>{

    Context context;
    List<DatMon> datMonList;
    int resources;

    public AdapterDanhSachMonAnDaDat(Context context, List<DatMon> datMonList, int resources){
        this.context = context;
        this.datMonList = datMonList;
        this.resources = resources;
    }


    public class ViewHolderMonAnDaDat extends RecyclerView.ViewHolder{

        ImageView imgMonAnDaDat;
        TextView txtTenMonAnDaDat,txtSoLuongMonAnDaDat,txtSoTienMonDaDat;

        public ViewHolderMonAnDaDat(View itemView) {
            super(itemView);
            imgMonAnDaDat = (ImageView) itemView.findViewById(R.id.imageMonAnDaDat);
            txtTenMonAnDaDat = (TextView) itemView.findViewById(R.id.txtTenMonAnDaDat);
            txtSoLuongMonAnDaDat = (TextView) itemView.findViewById(R.id.txtSoLuongMonAnDaDat);
            txtSoTienMonDaDat = (TextView) itemView.findViewById(R.id.txtSoTienMonDaDat);
        }
    }

    @Override
    public AdapterDanhSachMonAnDaDat.ViewHolderMonAnDaDat onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resources,parent,false);
        ViewHolderMonAnDaDat viewHolderMonAnDaDat = new ViewHolderMonAnDaDat(view);
        return viewHolderMonAnDaDat;
    }

    @Override
    public void onBindViewHolder(final AdapterDanhSachMonAnDaDat.ViewHolderMonAnDaDat holder, int position) {
        holder.txtTenMonAnDaDat.setText("Tên món: " + datMonList.get(position).getTemonan());
        holder.txtSoLuongMonAnDaDat.setText("Số lượng: " + datMonList.get(position).getSoluong());
        holder.txtSoTienMonDaDat.setText("Số tiền: " + datMonList.get(position).getSoluong()*datMonList.get(position).getSotien());
        StorageReference storageHinhUser = FirebaseStorage.getInstance().getReference().child("hinhanh").child(datMonList.get(position).getHinhanh());
        long mege_byte  =1024*1024*5;
        storageHinhUser.getBytes(mege_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                holder.imgMonAnDaDat.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datMonList.size();
    }
}
