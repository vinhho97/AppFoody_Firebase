package com.example.huynhvinh.appfoody_firebase.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huynhvinh.appfoody_firebase.Controller.OdauController;
import com.example.huynhvinh.appfoody_firebase.R;

public class TrangChuActivity extends AppCompatActivity{

    OdauController odauController;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    NestedScrollView nestedScrollView;
    Toolbar toolbar;
    EditText edtTimKiem;
    TextView txtTimKiem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trangchu);
        AnhXa();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu_themmonan,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.itThemQuanAn:
                Intent iThemQuanAn = new Intent(TrangChuActivity.this,ThemQuanAnActivity.class);
                startActivity(iThemQuanAn);
                break;
        }
        return true;
    }


    private void AnhXa() {
        edtTimKiem = (EditText) findViewById(R.id.edtTimKiem);
        txtTimKiem = (TextView) findViewById(R.id.txtTimKiem);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerODau);
        progressBar = (ProgressBar) findViewById(R.id.progressBarOdau);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestScrollViewOdau);
        toolbar = (Toolbar) findViewById(R.id.toolbarTrangChu);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = this.getSharedPreferences("toado", Context.MODE_PRIVATE); // Lấy dữ liệu từ shareConference
        final Location vitrihientai = new Location("");                                               // Tạo location để tính khoảng ca
        vitrihientai.setLatitude(Double.parseDouble(sharedPreferences.getString("latitude","0")));
        vitrihientai.setLongitude(Double.parseDouble(sharedPreferences.getString("longtitude","0")));

        odauController = new OdauController(TrangChuActivity.this);
        odauController.getDanhSachQuanAn(TrangChuActivity.this,nestedScrollView,recyclerView,progressBar,vitrihientai,1000);

        txtTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String KhoangCach = edtTimKiem.getText().toString();
                int khoangcach = Integer.parseInt(KhoangCach);
                if(KhoangCach == null || KhoangCach.equals(""))
                {
                    Toast.makeText(TrangChuActivity.this,getResources().getString(R.string.KiemTraTimKiem), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    odauController = new OdauController(TrangChuActivity.this);
                    odauController.getDanhSachQuanAn(TrangChuActivity.this,nestedScrollView,recyclerView,progressBar,vitrihientai,khoangcach);
                }
            }
        });
    }

}