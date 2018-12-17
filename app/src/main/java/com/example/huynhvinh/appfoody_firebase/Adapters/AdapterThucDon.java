package com.example.huynhvinh.appfoody_firebase.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.huynhvinh.appfoody_firebase.Model.ThucDonModel;
import com.example.huynhvinh.appfoody_firebase.R;

import java.util.List;



public class AdapterThucDon extends RecyclerView.Adapter<AdapterThucDon.HolderThucDon> {

    Context context;
    List<ThucDonModel> thucDonModels;
    String maquanan;

    public AdapterThucDon(Context context, List<ThucDonModel> thucDonModels,String maquanan){
        this.context = context;
        this.thucDonModels = thucDonModels;
        this.maquanan = maquanan;
    }

    @Override
    public HolderThucDon onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_thucdon,parent,false);
        return new HolderThucDon(view);
    }

    @Override
    public void onBindViewHolder(HolderThucDon holder, int position) {
        ThucDonModel thucDonModel = thucDonModels.get(position);
        holder.txtThucDon.setText(thucDonModel.getTenthucdon());
        holder.recyclerViewMonAn.setLayoutManager(new LinearLayoutManager(context));
        AdapterMonAn adapterMonAn = new AdapterMonAn(context,thucDonModel.getMonAnModelList(),maquanan);
        holder.recyclerViewMonAn.setAdapter(adapterMonAn);
        adapterMonAn.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return thucDonModels.size();
    }

    public class HolderThucDon extends RecyclerView.ViewHolder {
        TextView txtThucDon;
        RecyclerView recyclerViewMonAn;
        public HolderThucDon(View itemView) {
            super(itemView);

            txtThucDon = (TextView) itemView.findViewById(R.id.txtTenThucDon);
            recyclerViewMonAn = (RecyclerView) itemView.findViewById(R.id.recyclerMonAn);
        }
    }
}
