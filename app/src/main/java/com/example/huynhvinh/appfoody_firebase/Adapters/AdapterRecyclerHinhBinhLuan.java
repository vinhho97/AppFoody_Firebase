package com.example.huynhvinh.appfoody_firebase.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huynhvinh.appfoody_firebase.Model.BinhLuanModel;
import com.example.huynhvinh.appfoody_firebase.R;
import com.example.huynhvinh.appfoody_firebase.View.HienThiChiTietHinhBinhLuanActivity;
import com.example.huynhvinh.appfoody_firebase.View.HienThiFullHinhAnhBinhLuanActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerHinhBinhLuan extends RecyclerView.Adapter<AdapterRecyclerHinhBinhLuan.ViewHolderHinhBinhLuan> {

    Context context;
    int resource;
    List<Bitmap> listHinh;
    BinhLuanModel binhLuanModel;
    boolean iChiTietBinhLuan;  // kiểm tra là đang load ảnh dưới dàng bình luận hay chi tiết hình bình luận để show ra ảnh
    ArrayList<byte[]> imageByteList = new ArrayList<byte[]>();

    public AdapterRecyclerHinhBinhLuan(Context context, int resource, List<Bitmap> listHinh,BinhLuanModel binhLuanModel,boolean iChiTietBinhLuan){
        this.context = context;
        this.resource = resource;
        this.listHinh = listHinh;
        this.binhLuanModel = binhLuanModel;
        this.iChiTietBinhLuan  = iChiTietBinhLuan;
    }

    public class ViewHolderHinhBinhLuan extends RecyclerView.ViewHolder {
        ImageView imageHinhBinhLuan;
        TextView txtSoHinhBinhLuan;
        FrameLayout khungSoHinhBinhLuan;

        public ViewHolderHinhBinhLuan(View itemView) {
            super(itemView);
            imageHinhBinhLuan = (ImageView) itemView.findViewById(R.id.imageBinhLuan);
            txtSoHinhBinhLuan = (TextView) itemView.findViewById(R.id.txtSoHinhBinhLuan);
            khungSoHinhBinhLuan = (FrameLayout) itemView.findViewById(R.id.KhungSoHinhBinhLuan);
        }
    }

    @Override
    public AdapterRecyclerHinhBinhLuan.ViewHolderHinhBinhLuan onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        ViewHolderHinhBinhLuan  viewHolderHinhBinhLuan = new ViewHolderHinhBinhLuan(view);
        return viewHolderHinhBinhLuan;
    }

    @Override
    public void onBindViewHolder(final AdapterRecyclerHinhBinhLuan.ViewHolderHinhBinhLuan holder, final int position) {
               // holder.imageHinhBinhLuan.setImageBitmap(listHinh.get(position));
                StorageReference storageHinhUser = FirebaseStorage.getInstance().getReference().child("hinhanh").child(binhLuanModel.getHinhanhBinhLuanList().get(position));
                long mege_byte = 1024 * 1024;
                storageHinhUser.getBytes(mege_byte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imageHinhBinhLuan.setImageBitmap(bitmap);
                    }
                });
                if(!iChiTietBinhLuan)  // nếu không phải đang load ra chi tiết bình luận (nghĩa là load ra tất cả các ảnh của bình luận ở bên AdapterBinhLuan)
                {
                    if (position == 3) {
                        int soHinhConLai = listHinh.size() - 4;
                        if (soHinhConLai > 0) {
                            holder.khungSoHinhBinhLuan.setVisibility(View.VISIBLE);
                            holder.txtSoHinhBinhLuan.setText("+" + soHinhConLai);
                            holder.imageHinhBinhLuan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent iChitietbinhluan = new Intent(context, HienThiChiTietHinhBinhLuanActivity.class);
                                    iChitietbinhluan.putExtra("binhluanmodel", binhLuanModel);
                                    context.startActivity(iChitietbinhluan);
                                }
                            });
                        }
                        else {
                            holder.imageHinhBinhLuan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, HienThiFullHinhAnhBinhLuanActivity.class);
                                    // đổi list hình bitmap vê byte Array

                                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                                    listHinh.get(position).compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                                    Bundle bundle = new Bundle();
                                    bundle.putByteArray("HinhBinhLuan", byteArray.toByteArray());
                                    intent.putExtras(bundle);



                                    context.startActivity(intent);
                                }
                            });
                        }
                    }
                    else if(position<3)
                    {
                        holder.imageHinhBinhLuan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, HienThiFullHinhAnhBinhLuanActivity.class);
                                // đổi list hình bitmap vê byte Array

                                  /*  ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                                    listHinh.get(position).compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                                    Bundle bundle = new Bundle();
                                    bundle.putByteArray("HinhBinhLuan", byteArray.toByteArray());
                                    intent.putExtras(bundle);*/


                                intent.putExtra("HinhBinhLuan",binhLuanModel.getHinhanhBinhLuanList().get(position));
                                context.startActivity(intent);
                            }
                        });
                    }
                }
                /*else {
                    holder.imageHinhBinhLuan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, HienThiFullHinhAnhBinhLuanActivity.class);
                            intent.putExtra("VitriHinhClick",position);

                            //   intent.putExtra("HinhBinhLuanModel", binhLuanModel);
                            // Code chuyển đổi list ảnh từ bitmap sang byte
                            for(int i=0; i< listHinh.size(); i++) {
                                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                                listHinh.get(i).compress(Bitmap.CompressFormat.PNG,100,byteArray);
                                imageByteList.add( byteArray.toByteArray());
                            }
                            intent.putExtra("ListByteArray_Size",imageByteList.size());
                            // Đứa list image byte lên intent
                            for(int j=0;j<imageByteList.size();j++)
                            {
                                intent.putExtra("imageByte"+j,imageByteList.get(j));
                            }
                            context.startActivity(intent);
                        }
                    });
                }*/

    }

    @Override
    public int getItemCount() {
        if (!iChiTietBinhLuan) {
            if(listHinh.size()<4)
            {
                return listHinh.size();
            }
            else
            {
                return 4;
            }

        }
        else {
            return listHinh.size();
        }
    }

}
