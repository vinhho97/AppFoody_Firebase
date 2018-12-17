package com.example.huynhvinh.appfoody_firebase.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.huynhvinh.appfoody_firebase.Model.BinhLuanModel;
import com.example.huynhvinh.appfoody_firebase.Model.QuanAnModel;
import com.example.huynhvinh.appfoody_firebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterBinhLuan extends RecyclerView.Adapter<AdapterBinhLuan.ViewHolder> {

    Context context;
    int layout;
    List<BinhLuanModel> binhLuanModelList;
    List<Bitmap> bitmapList;        // List ảnh của bình luận

    public AdapterBinhLuan(Context context, int layout, List<BinhLuanModel> binhLuanModelList){

        this.context = context;
        this.layout = layout;
        this.binhLuanModelList = binhLuanModelList;

    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txtTieuDeBinhLuan,txtNoiDungBinhLuan,txtSoDiem;
        RecyclerView  recyclerViewHinhBinhLuan;

        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageUser);
            txtTieuDeBinhLuan = (TextView) itemView.findViewById(R.id.txtTieuDeBinhLuan);
            txtNoiDungBinhLuan = (TextView) itemView.findViewById(R.id.txtNoiDungBinhLuan);
            txtSoDiem = (TextView) itemView.findViewById(R.id.txtChamdiem);
            recyclerViewHinhBinhLuan = (RecyclerView) itemView.findViewById(R.id.recyclerHinhBinhLuan);

        }
    }

    @Override
    public AdapterBinhLuan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterBinhLuan.ViewHolder holder, int position) {
        final List<Bitmap> bitmapList = new ArrayList<>();        // List ảnh của bình luận
        final BinhLuanModel binhLuanModel = binhLuanModelList.get(position);
        holder.txtTieuDeBinhLuan.setText(binhLuanModel.getTieude());
        holder.txtNoiDungBinhLuan.setText(binhLuanModel.getNoidung());
        holder.txtSoDiem.setText(binhLuanModel.getChamdiem()+ "");
        setHinhAnhBinhLuan(holder.circleImageView,"user.png");

        // Duyệt hình đưa vào list Bitmap
        for(String linkhinh : binhLuanModel.getHinhanhBinhLuanList()) {
            StorageReference storageHinhUser = FirebaseStorage.getInstance().getReference().child("hinhanh").child(linkhinh);
            long mege_byte = 1024 * 1024*5;
            storageHinhUser.getBytes(mege_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    bitmapList.add(bitmap);
                    // Kiểm tra số hình trong bitmap có bằng số hình trong hình của 1 mình bình. Việc này để tránh adapter thực hiện nhiều lần. Nếu duyệt đến khi bằng nhau thì đó là hình cuối cùng của bình luận đó và chỉ thực hiện 1 lần
                    if(bitmapList.size() == binhLuanModel.getHinhanhBinhLuanList().size())
                    {
                        // Load lên ảnh lên recyclerView
                        AdapterRecyclerHinhBinhLuan adapterRecyclerHinhBinhLuan = new AdapterRecyclerHinhBinhLuan(context,R.layout.custom_layout_hinhbinhluan,bitmapList,binhLuanModel,false); // truyền bình luận model để xử lý việc load hình ảnh bên adapter
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,2);
                        holder.recyclerViewHinhBinhLuan.setLayoutManager(layoutManager);
                        holder.recyclerViewHinhBinhLuan.setAdapter(adapterRecyclerHinhBinhLuan);
                        adapterRecyclerHinhBinhLuan.notifyDataSetChanged();
                        //end Load lên ảnh lên recyclerView
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

    @Override
    public int getItemCount() {
        int SoBinhLuan = binhLuanModelList.size();
        if(SoBinhLuan>5)
        {
            return 5;
        }
        else
        {
            return binhLuanModelList.size();
        }
    }
}
