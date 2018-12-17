package com.example.huynhvinh.appfoody_firebase.Model;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.huynhvinh.appfoody_firebase.Controller.Interfaces.OdauInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuanAnModel implements Parcelable {
    boolean giaohang;
    String giodongcua,giomocua,tenquanan,maquanan;
    List<String> hinhanhquanans;
    List<BinhLuanModel> binhLuanModelList;
    List<ChiNhanhQuanAnModel> chiNhanhQuanAnModelList;
    List<Bitmap> bitmapList;
    long giatoida;
    long giatoithieu;
    long luotthich;
    DatabaseReference nodeRoot;

    public long getGiatoida() {
        return giatoida;
    }

    public void setGiatoida(long giatoida) {
        this.giatoida = giatoida;
    }

    public long getGiatoithieu() {
        return giatoithieu;
    }

    public void setGiatoithieu(long giatoithieu) {
        this.giatoithieu = giatoithieu;
    }




    protected QuanAnModel(Parcel in) {
        giaohang = in.readByte() != 0;
        giodongcua = in.readString();
        giomocua = in.readString();
        tenquanan = in.readString();
        maquanan = in.readString();
        hinhanhquanans = in.createStringArrayList();
        giatoida = in.readLong();
        giatoithieu = in.readLong();
        luotthich = in.readLong();
        chiNhanhQuanAnModelList = new ArrayList<ChiNhanhQuanAnModel>();
        in.readTypedList(chiNhanhQuanAnModelList,ChiNhanhQuanAnModel.CREATOR);
        binhLuanModelList = new ArrayList<BinhLuanModel>();
        in.readTypedList(binhLuanModelList,BinhLuanModel.CREATOR);
    }

    public static final Creator<QuanAnModel> CREATOR = new Creator<QuanAnModel>() {
        @Override
        public QuanAnModel createFromParcel(Parcel in) {
            return new QuanAnModel(in);
        }

        @Override
        public QuanAnModel[] newArray(int size) {
            return new QuanAnModel[size];
        }
    };

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    public List<ChiNhanhQuanAnModel> getChiNhanhQuanAnModelList() {
        return chiNhanhQuanAnModelList;
    }

    public void setChiNhanhQuanAnModelList(List<ChiNhanhQuanAnModel> chiNhanhQuanAnModelList) {
        this.chiNhanhQuanAnModelList = chiNhanhQuanAnModelList;
    }

    public List<BinhLuanModel> getBinhLuanModelList() {
        return binhLuanModelList;
    }

    public void setBinhLuanModelList(List<BinhLuanModel> binhLuanModelList) {
        this.binhLuanModelList = binhLuanModelList;
    }

    public List<String> getHinhanhquanans() {
        return hinhanhquanans;
    }

    public void setHinhanhquanans(List<String> hinhanhquanans) {
        this.hinhanhquanans = hinhanhquanans;
    }


    public long getLuotthich() {
        return luotthich;
    }

    public void setLuotthich(long luotthich) {
        this.luotthich = luotthich;
    }

    public QuanAnModel(){
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    public boolean isGiaohang() {
        return giaohang;
    }

    public void setGiaohang(boolean giaohang) {
        this.giaohang = giaohang;
    }

    public String getGiodongcua() {
        return giodongcua;
    }

    public void setGiodongcua(String giodongcua) {
        this.giodongcua = giodongcua;
    }

    public String getGiomocua() {
        return giomocua;
    }

    public void setGiomocua(String giomocua) {
        this.giomocua = giomocua;
    }

    public String getTenquanan() {
        return tenquanan;
    }

    public void setTenquanan(String tenquanan) {
        this.tenquanan = tenquanan;
    }


    public String getMaquanan() {
        return maquanan;
    }

    public void setMaquanan(String maquanan) {
        this.maquanan = maquanan;
    }



    private DataSnapshot dataRoot;
    public void getDanhSachQuanAn(final OdauInterface odauInterface, final Location vitrihientai, final int itemtieptheo, final int itemdaco){       // Hàm khởi tạo intergare để lấy hình

        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;
                LayDanhSachQuanAn(dataSnapshot,odauInterface,vitrihientai,itemtieptheo,itemdaco);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if(dataRoot!=null)  // kiểm tra đã thực hiện sự kiện lisstener hay chưa.. nêu có thì việc gán datanapshot ở trên đã đc thực thi và ko cần load lại cái sự kiện listener lần nữa
        {
            LayDanhSachQuanAn(dataRoot,odauInterface,vitrihientai,itemtieptheo,itemdaco);
        }
        else
        {
            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }

    }

    private void LayDanhSachQuanAn(DataSnapshot dataSnapshot,OdauInterface odauInterface,Location vitrihientai,int itemtieptheo,int itemdaco)
    {
        DataSnapshot dataSnapshotQuanAn = dataSnapshot.child("quanans");  // Trỏ tới node quanans

        int i =0;
        // Lấy danh sách quán ăn
        for(DataSnapshot valueQuanAn : dataSnapshotQuanAn.getChildren())   // Lấy các Key(node con của quán ăn)
        {

            if(i==itemtieptheo) // itemtieptheo là số lường item cần load lên. Nếu biến i = itemtiep theo có nghĩa là đã load đủ item cần load
            {
                break;
            }
            if(i<itemdaco)    // itemban đầu là số item đã đc load lên.. Code này để tránh việc lặp lại những item đã đc load
            {
                i++;
                continue;
            }
            i++;
            QuanAnModel quanAnModel = valueQuanAn.getValue(QuanAnModel.class);

            quanAnModel.setMaquanan(valueQuanAn.getKey());
            DataSnapshot dataSnapshotHinhAnhQuanAn = dataSnapshot.child("hinhanhquanans").child(valueQuanAn.getKey());    // Trỏ tới nơi hình quán ăn thông qua  key quán ăn

            List<String> hinhanhlist = new ArrayList<>();       // List để lưu trữ hình của quán ăn
            // Lấy danh sách hình ảnh quán ăn theo mã
            for(DataSnapshot valueHinhAnh : dataSnapshotHinhAnhQuanAn.getChildren())    // get key cua hinh anh quan an
            {

                hinhanhlist.add(valueHinhAnh.getValue(String.class));
            }

            quanAnModel.setHinhanhquanans(hinhanhlist);     // truyền dữ liệu hình ảnh quán ăn vào quanAnModel

            // End lấy hình ảnh quán ăn


            // Lấy danh sách bình luận của quán ăn
            DataSnapshot dataSnapshotBinhLuan = dataSnapshot.child("binhluans").child(quanAnModel.getMaquanan());
            List<BinhLuanModel> binhLuanModels = new ArrayList<>();
            // Load từng mabinhluan cua quán ăn
            for(DataSnapshot valueBinhLuan : dataSnapshotBinhLuan.getChildren())
            {
                BinhLuanModel binhLuanModel = valueBinhLuan.getValue(BinhLuanModel.class);
                binhLuanModel.setMabinhluan(valueBinhLuan.getKey());  // Lấy key(mã bình luận) của quán ăn
                ThanhVienModel thanhVienModel = dataSnapshot.child("thanhviens").child(binhLuanModel.getMauser()).getValue(ThanhVienModel.class);
                binhLuanModel.setThanhVienModel(thanhVienModel);

                List<String> hinhanhBinhLuanList = new ArrayList<>();  // List lưu hình ảnh bình luận
                DataSnapshot dataNodehHinhBinhLuan = dataSnapshot.child("hinhanhbinhluans").child(binhLuanModel.getMabinhluan());
                // Duyệt vào key của hình ảnh bình luận
                for(DataSnapshot valueHinhBinhLuan : dataNodehHinhBinhLuan.getChildren())
                {
                    hinhanhBinhLuanList.add(valueHinhBinhLuan.getValue(String.class));
                }
                binhLuanModel.setHinhanhBinhLuanList(hinhanhBinhLuanList);

                binhLuanModels.add(binhLuanModel);
            }
            quanAnModel.setBinhLuanModelList(binhLuanModels);

            // End lấy bình luận quán ăn

            // Lấy chi nhánh quán ăn
            DataSnapshot dataSnapshotChiNhanhQuanAn = dataSnapshot.child("chinhanhquanans").child(quanAnModel.getMaquanan());
            List<ChiNhanhQuanAnModel> chiNhanhQuanAnModels = new ArrayList<>();
            for(DataSnapshot valueChiNhanhQuanAn : dataSnapshotChiNhanhQuanAn.getChildren())
            {
                ChiNhanhQuanAnModel chiNhanhQuanAnModel = valueChiNhanhQuanAn.getValue(ChiNhanhQuanAnModel.class);
                Location ViTriQuanAn = new Location("");
                ViTriQuanAn.setLongitude(chiNhanhQuanAnModel.getLongtitude());
                ViTriQuanAn.setLatitude(chiNhanhQuanAnModel.getLatitude());
                double KhoangCach = vitrihientai.distanceTo(ViTriQuanAn)/1000;
                chiNhanhQuanAnModel.setKhoangcach(KhoangCach);

                chiNhanhQuanAnModels.add(chiNhanhQuanAnModel);

            }
            quanAnModel.setChiNhanhQuanAnModelList(chiNhanhQuanAnModels);
            odauInterface.getDanhSachQuanAnModel(quanAnModel);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (giaohang ? 1 : 0));
        parcel.writeString(giodongcua);
        parcel.writeString(giomocua);
        parcel.writeString(tenquanan);
        parcel.writeString(maquanan);
        parcel.writeStringList(hinhanhquanans);
        parcel.writeLong(giatoida);
        parcel.writeLong(giatoithieu);
        parcel.writeLong(luotthich);
        parcel.writeTypedList(chiNhanhQuanAnModelList);
        parcel.writeTypedList(binhLuanModelList);
    }
}