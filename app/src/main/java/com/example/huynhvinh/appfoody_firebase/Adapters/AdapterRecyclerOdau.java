package com.example.huynhvinh.appfoody_firebase.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huynhvinh.appfoody_firebase.Model.BinhLuanModel;
import com.example.huynhvinh.appfoody_firebase.Model.ChiNhanhQuanAnModel;
import com.example.huynhvinh.appfoody_firebase.Model.QuanAnModel;
import com.example.huynhvinh.appfoody_firebase.R;
import com.example.huynhvinh.appfoody_firebase.View.ChiTietQuanAnActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRecyclerOdau extends RecyclerView.Adapter<AdapterRecyclerOdau.ViewHolder> {

    List<QuanAnModel> quanAnModelList;
    int resources,khoangcach;
    Context context;

    public AdapterRecyclerOdau(Context context,List<QuanAnModel> quanAnModelList, int resources,int khoangcach) {
        this.quanAnModelList = quanAnModelList;
        this.resources = resources;
        this.context = context;
        this.khoangcach = khoangcach;
    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView txtTenQuanAnOdau,txtNoiDungBinhLuan2,txtNoiDungBinhLuan1,txtTieuDeBinhLuan2,txtTieuDeBinhLuan1,txtChamdiem1,txtChamdiem2,txtTongBinhLuan,txtTongHinhBinhLuan,txtDiemTrungBinhQuanAn,txtDiaChiQuanAnOdau,txtKhoangCachQuanAnOdau;
        Button btnDatMon;
        ImageView imageHinhQuanAnOdau;
        CircleImageView circleImageUser2,circleImageUser1;
        LinearLayout containerBinhLuan1,containerBinhLuan2;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTenQuanAnOdau = (TextView) itemView.findViewById(R.id.txtTenQuanAnOdau);
            btnDatMon = (Button) itemView.findViewById(R.id.btnDatMonOdau);
            imageHinhQuanAnOdau = (ImageView) itemView.findViewById(R.id.imageHinhQuanAnOdau);
            txtTieuDeBinhLuan1 = (TextView) itemView.findViewById(R.id.txtTieuDeBinhLuan1);
            txtTieuDeBinhLuan2 = (TextView) itemView.findViewById(R.id.txtTieuDeBinhLuan2);
            txtNoiDungBinhLuan1 = (TextView) itemView.findViewById(R.id.txtNoiDungBinhLuan1);
            txtNoiDungBinhLuan2 = (TextView) itemView.findViewById(R.id.txtNoiDungBinhLuan2);
            circleImageUser1 = (CircleImageView) itemView.findViewById(R.id.circleImageUser1);
            circleImageUser2 = (CircleImageView) itemView.findViewById(R.id.circleImageUser2);
            containerBinhLuan1 = (LinearLayout) itemView.findViewById(R.id.containerBinhLuan1);
            containerBinhLuan2 = (LinearLayout) itemView.findViewById(R.id.containerBinhLuan2);
            txtChamdiem1 = (TextView) itemView.findViewById(R.id.txtChamdiem1);
            txtChamdiem2 = (TextView) itemView.findViewById(R.id.txtChamdiem2);
            txtTongBinhLuan = (TextView) itemView.findViewById(R.id.txtTongBinhLuan);
            txtTongHinhBinhLuan = (TextView) itemView.findViewById(R.id.txtTongHinhBinhLuan);
            txtDiemTrungBinhQuanAn = (TextView) itemView.findViewById(R.id.txtDiemTrungBinhQuanAn);
            txtDiaChiQuanAnOdau = (TextView) itemView.findViewById(R.id.txtDiaChiQuanAnOdau);
            txtKhoangCachQuanAnOdau = (TextView) itemView.findViewById(R.id.txtKhoangCachQuanAnOdau);
            cardView = (CardView) itemView.findViewById(R.id.cardViewOdau);
        }
    }

    @Override
    public AdapterRecyclerOdau.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resources,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterRecyclerOdau.ViewHolder holder, int position) {

        final QuanAnModel quanAnModel = quanAnModelList.get(position);
        // Lấy chi nhanh quán ăn (Địa chỉ quán và khoảng cách)
        if(quanAnModel.getChiNhanhQuanAnModelList().size()> 0)
        {
            ChiNhanhQuanAnModel chiNhanhQuanAnModelTam = quanAnModel.getChiNhanhQuanAnModelList().get(0); // Lấy vị trí khoảng cách đầu tiên
            for(ChiNhanhQuanAnModel chiNhanhQuanAnModel : quanAnModel.getChiNhanhQuanAnModelList())
            {
                if(chiNhanhQuanAnModelTam.getKhoangcach() > chiNhanhQuanAnModel.getKhoangcach())
                {
                    chiNhanhQuanAnModelTam = chiNhanhQuanAnModel;           // Sau khi so sánh xong thằng nào nhỏ hơn thì đc gán
                }
            }
            if(chiNhanhQuanAnModelTam.getKhoangcach()<= khoangcach)
            {
                    LoadThongTinQuanAn(quanAnModel,holder,chiNhanhQuanAnModelTam);
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iChiTietQuanAn = new Intent(context, ChiTietQuanAnActivity.class);
                iChiTietQuanAn.putExtra("quanan",quanAnModel);
                context.startActivity(iChiTietQuanAn);
            }
        });

    }

    private  void LoadThongTinQuanAn(QuanAnModel quanAnModel, final AdapterRecyclerOdau.ViewHolder holder, ChiNhanhQuanAnModel chiNhanhQuanAnModelTam)
    {
        holder.txtTenQuanAnOdau.setText(quanAnModel.getTenquanan());
        if(quanAnModel.isGiaohang())                        // kiểm tra cửa hàng có cho giao hàng hay không
        {
            holder.btnDatMon.setVisibility(View.VISIBLE);
        }

        if(quanAnModel.getBitmapList().size() > 0)      // Lấy hình ảnh cho quán ăn
        {
            holder.imageHinhQuanAnOdau.setImageBitmap(quanAnModel.getBitmapList().get(0));
        }

        if(quanAnModel.getBinhLuanModelList().size()>0)
        {
            BinhLuanModel binhLuanModel = quanAnModel.getBinhLuanModelList().get(0);
            holder.txtTieuDeBinhLuan1.setText(binhLuanModel.getTieude());
            holder.txtNoiDungBinhLuan1.setText(binhLuanModel.getNoidung());
            holder.txtChamdiem1.setText(binhLuanModel.getChamdiem() + "");
            setHinhAnhBinhLuan(holder.circleImageUser1,"user.png");
            if(quanAnModel.getBinhLuanModelList().size()>1)
            {
                BinhLuanModel binhLuanModel2 = quanAnModel.getBinhLuanModelList().get(1);
                holder.txtTieuDeBinhLuan2.setText(binhLuanModel2.getTieude());
                holder.txtNoiDungBinhLuan2.setText(binhLuanModel2.getNoidung());
                holder.txtChamdiem2.setText(binhLuanModel2.getChamdiem() + "");
                setHinhAnhBinhLuan(holder.circleImageUser2,"user.png");
            }
            holder.txtTongBinhLuan.setText(quanAnModel.getBinhLuanModelList().size() + "");

            int TongSoHinhBinhLuan = 0;
            double TongDiemCuaQuan = 0;

            // Duyệt từng bình luận để đếm số ảnh
            for(BinhLuanModel binhLuanModel1:quanAnModel.getBinhLuanModelList())
            {
                TongSoHinhBinhLuan +=binhLuanModel1.getHinhanhBinhLuanList().size();
                TongDiemCuaQuan += binhLuanModel1.getChamdiem();
            }

            double DiemTrungBinhCuaQuan = TongDiemCuaQuan/quanAnModel.getBinhLuanModelList().size();
            holder.txtDiemTrungBinhQuanAn.setText(String.format("%.2f",DiemTrungBinhCuaQuan));

            if(TongSoHinhBinhLuan>0) {      // kiểm tra tổng số hình bình luận
                holder.txtTongHinhBinhLuan.setText(TongSoHinhBinhLuan + "");

            }
        }else{
            holder.containerBinhLuan1.setVisibility(View.GONE);
            holder.containerBinhLuan2.setVisibility(View.GONE);
            holder.txtTongBinhLuan.setText("0");
        }
        // Load khoảng cách với tên quán ăn
        holder.txtDiaChiQuanAnOdau.setText(chiNhanhQuanAnModelTam.getDiachi());
        holder.txtKhoangCachQuanAnOdau.setText(String.format("%.1f",chiNhanhQuanAnModelTam.getKhoangcach())+ "km");
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
        return quanAnModelList.size();      // số lượng view trả ra
    }

}
