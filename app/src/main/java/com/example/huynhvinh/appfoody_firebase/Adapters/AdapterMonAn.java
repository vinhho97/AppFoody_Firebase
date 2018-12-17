package com.example.huynhvinh.appfoody_firebase.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huynhvinh.appfoody_firebase.Model.DatMon;
import com.example.huynhvinh.appfoody_firebase.Model.MonAnModel;
import com.example.huynhvinh.appfoody_firebase.R;


import java.util.ArrayList;
import java.util.List;


public class AdapterMonAn extends RecyclerView.Adapter<AdapterMonAn.HolderMonAn> {

    Context context;
    List<MonAnModel> monAnModelList;
    public static List<DatMon> datMonList = new ArrayList<>();
    String maquanan;

    public AdapterMonAn(Context context, List<MonAnModel> monAnModelList,String maquanan){
        this.context = context;
        this.monAnModelList = monAnModelList;
        this.maquanan = maquanan;
        datMonList.clear();
    }

    @Override
    public HolderMonAn onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_monan,parent,false);
        return new HolderMonAn(view);
    }

    @Override
    public void onBindViewHolder(final HolderMonAn holder, int position) {
        final MonAnModel monAnModel = monAnModelList.get(position);
        holder.txtTenMonAn.setText(monAnModel.getTenmon());

        holder.txtSoLuong.setTag(0);
        holder.imgTangSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dem = Integer.parseInt(holder.txtSoLuong.getTag().toString());
                dem++;
                holder.txtSoLuong.setText(dem+"");
                holder.txtSoLuong.setTag(dem);

                DatMon datMonTag = (DatMon) holder.imgGiamSoLuong.getTag();
                if(datMonTag != null){
                    AdapterMonAn.datMonList.remove(datMonTag);
                }

                DatMon datMon = new DatMon();
                datMon.setSoluong(dem);
                datMon.setTemonan(monAnModel.getTenmon());
                datMon.setHinhanh(monAnModel.getHinhanh());
                datMon.setMamonan(monAnModel.getMamon());
                datMon.setMaquanan(maquanan);
                datMon.setSotien(monAnModel.getGiatien());
                holder.imgGiamSoLuong.setTag(datMon);
                AdapterMonAn.datMonList.add(datMon);
            }
        });

        holder.imgGiamSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dem = Integer.parseInt(holder.txtSoLuong.getTag().toString());
                if(dem != 0){
                    dem--;
                    if(dem == 0){
                        DatMon datMon = (DatMon) v.getTag();
                        AdapterMonAn.datMonList.remove(datMon);
                    }
                }
                holder.txtSoLuong.setText(dem+"");
                holder.txtSoLuong.setTag(dem);

            }
        });


    }

    @Override
    public int getItemCount() {
        return monAnModelList.size();
    }

    public class HolderMonAn extends RecyclerView.ViewHolder {
        TextView txtTenMonAn,txtSoLuong;
        ImageView imgGiamSoLuong,imgTangSoLuong;

        public HolderMonAn(View itemView) {
            super(itemView);
            txtTenMonAn = (TextView) itemView.findViewById(R.id.txtTenMonAn);
            txtSoLuong = (TextView) itemView.findViewById(R.id.txtSoLuong);
            imgGiamSoLuong = (ImageView) itemView.findViewById(R.id.imgGiamSoLuong);
            imgTangSoLuong = (ImageView) itemView.findViewById(R.id.imgTangSoLuong);
        }
    }
}