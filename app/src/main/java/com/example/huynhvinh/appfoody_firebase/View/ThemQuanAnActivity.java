package com.example.huynhvinh.appfoody_firebase.View;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.huynhvinh.appfoody_firebase.Model.ChiNhanhQuanAnModel;
import com.example.huynhvinh.appfoody_firebase.Model.KhuVucModel;
import com.example.huynhvinh.appfoody_firebase.Model.MonAnModel;
import com.example.huynhvinh.appfoody_firebase.Model.QuanAnModel;
import com.example.huynhvinh.appfoody_firebase.Model.ThemNodeQuanAnModel;
import com.example.huynhvinh.appfoody_firebase.Model.ThemThucDonModel;
import com.example.huynhvinh.appfoody_firebase.Model.ThucDonModel;
import com.example.huynhvinh.appfoody_firebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThemQuanAnActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    final int Request_Code_img1 = 111;
    final int Request_Code_img2 = 112;
    final int Request_Code_img3 = 113;
    final int Request_Code_img4 = 114;
    final int Request_ImgThucDon = 222;

    Toolbar toolbar;
    Spinner spinerKhuVuc;
    Button btnGioMoCua,btnGioDongCua,btnThemQuanAn;
    ImageButton imageButton;
    String GioDongCua,GioMoCua;
    String KhuVuc;
    ImageView imageTam; // video phút 50
    ImageView imgHinhQuan1,imgHinhQuan2,imgHinhQuan3,imgHinhQuan4,imgHinhQuan5,imgHinhQuan6;
    LinearLayout khungChiNhanh,khungChuaChiNhanh,khungChuaThucDon;
    RadioGroup rdgTrangThaiGiaoHang;
    EditText edTenQuan,edGiaToiDa,edGiaToiThieu;
    String maQuanAn;

    ArrayAdapter<String> adapterKhuVuc;

    List<ThucDonModel> thucDonModelList;
    List<String> khuVucList,thucdonList;
    List<String> chiNhanhList;
    List<ThemThucDonModel> themThucDonModelList;
    List<Bitmap> listHinhDaChup;  // lưu tất cả các ảnh của món ăn để đưa lên storage
    List<Bitmap> hinhQuanAnList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themquanan);
        AnhXa();

        // set khu vuc len spiner

        adapterKhuVuc = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,khuVucList);
        spinerKhuVuc.setAdapter(adapterKhuVuc);
        adapterKhuVuc.notifyDataSetChanged();

        // Bắt sự kiện ImageButton thêm quán ăn . Mới đầu vào nó sẽ tự động load view cho việc thêm chi nhánh
        CloneChiNhanh();
        CloneThucDon();

        LayDanhSachKhuVuc();

        btnGioMoCua.setOnClickListener(this);
        btnGioDongCua.setOnClickListener(this);
        spinerKhuVuc.setOnItemSelectedListener(this);
        imgHinhQuan1.setOnClickListener(this);
        imgHinhQuan2.setOnClickListener(this);
        imgHinhQuan3.setOnClickListener(this);
        imgHinhQuan4.setOnClickListener(this);
        btnThemQuanAn.setOnClickListener(this);

    }

    private void AnhXa() {
        btnGioDongCua  = (Button) findViewById(R.id.btnGioDongCua);
        btnGioMoCua = (Button) findViewById(R.id.btnGioMoCua);
        btnThemQuanAn = (Button) findViewById(R.id.btnLuuQuanAn);
        spinerKhuVuc= (Spinner) findViewById(R.id.spinerKhuVuc);
        khungChiNhanh = (LinearLayout) findViewById(R.id.khungChiNhanh);
        khungChuaChiNhanh = (LinearLayout) findViewById(R.id.khungChuaChiNhanh);
        khungChuaThucDon = (LinearLayout) findViewById(R.id.khungChuaThucDon);
        imageButton = (ImageButton) khungChuaChiNhanh.findViewById(R.id.btnThemChiNhanh);
        imgHinhQuan1 = (ImageView) findViewById(R.id.imgHinhQuan1);
        imgHinhQuan2 = (ImageView) findViewById(R.id.imgHinhQuan2);
        imgHinhQuan3 = (ImageView) findViewById(R.id.imgHinhQuan3);
        imgHinhQuan4 = (ImageView) findViewById(R.id.imgHinhQuan4);
        rdgTrangThaiGiaoHang = (RadioGroup) findViewById(R.id.rdgTrangThaiGiaoHang);
        edTenQuan = (EditText) findViewById(R.id.edTenQuanAn);
        edGiaToiDa = (EditText) findViewById(R.id.edGiaToiDa);
        edGiaToiThieu = (EditText) findViewById(R.id.edGiaToiThieu);

        thucDonModelList = new ArrayList<>();
        khuVucList = new ArrayList<>();
        thucdonList = new ArrayList<>();
        chiNhanhList = new ArrayList<>();
        themThucDonModelList = new ArrayList<>();
        listHinhDaChup = new ArrayList<>();
        hinhQuanAnList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbarThemQuanAn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private  void LayDanhSachThucDon(final ArrayAdapter<String> adapterThucDon) {
        FirebaseDatabase.getInstance().getReference().child("thucdons").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ThucDonModel   thucDonModel = new ThucDonModel();
                    String key = snapshot.getKey();
                    String value = snapshot.getValue(String.class);

                    thucDonModel.setTenthucdon(value);
                    thucDonModel.setMathucdon(key);
                    thucDonModelList.add(thucDonModel);
                    thucdonList.add(value);
                }
                adapterThucDon.notifyDataSetChanged();
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private  void LayDanhSachKhuVuc(){
        FirebaseDatabase.getInstance().getReference().child("khuvucs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String tenkhuvuc= snapshot.getKey();
                    khuVucList.add(tenkhuvuc);
                }
                adapterKhuVuc.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(final View view) {

        int id = view.getId();
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        switch(id)
        {
            case R.id.btnGioMoCua:
                TimePickerDialog timePickerDialog = new TimePickerDialog(ThemQuanAnActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        GioDongCua = hourOfDay+ ":" + minute;
                        ((Button)view).setText(GioDongCua);
                    }
                },gio,phut,true);
                timePickerDialog.show();
                break;
            case R.id.btnGioDongCua:
                TimePickerDialog moCuatimePickerDialog = new TimePickerDialog(ThemQuanAnActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        GioMoCua = hourOfDay+ ":" + minute;
                        ((Button)view).setText(GioMoCua);
                    }
                },gio,phut,true);
                moCuatimePickerDialog.show();
                break;
            case R.id.imgHinhQuan1:
                ChonHinhTuGallary(Request_Code_img1);
                break;
            case R.id.imgHinhQuan2:
                ChonHinhTuGallary(Request_Code_img2);
                break;
            case R.id.imgHinhQuan3:
                ChonHinhTuGallary(Request_Code_img3);
                break;
            case R.id.imgHinhQuan4:
                ChonHinhTuGallary(Request_Code_img4);
                break;
            case R.id.btnLuuQuanAn:
                ThemQuanAn();
                break;
        }

    }


    private  void ThemQuanAn(){
        String TenQuanAn = edTenQuan.getText().toString();
        String giatoida = edGiaToiDa.getText().toString();
        String giatoithieu = edGiaToiThieu.getText().toString();
        String GioDongCua = btnGioDongCua.getText().toString();
        String GioMoCua = btnGioMoCua.getText().toString();
        // Lấy id radioButton đc chon
        int idRadioSelected = rdgTrangThaiGiaoHang.getCheckedRadioButtonId();
        boolean giaoHang = false;
        if(idRadioSelected == R.id.rdGiaoHang)
        {
            giaoHang=true;
        }
        else {
            giaoHang=false;
        }
        DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
        DatabaseReference nodeQuanAn = nodeRoot.child("quanans");
        maQuanAn = nodeQuanAn.push().getKey();           // lấy key được phát sính động cho quán ăn

        nodeRoot.child("khuvucs").child(KhuVuc).push().setValue(maQuanAn);      // Thêm khu vực cho quán ăn

        for(String chinhanh:chiNhanhList)           // Thêm chi nhánh cho quán ăn
        {
            String urlGeoCodeing = "https://maps.googleapis.com/maps/api/geocode/json?address=" + chinhanh.replace(" ","%20") + "&key=AIzaSyAkIMUw-B81djXbAJk7cpaeVC16-z8ZPS0"; // để lấy latitude và longtitude dựa vào địa chỉ sử dụng Google map geo
            DownLoadToaDo downLoadToaDo = new DownLoadToaDo();
            downLoadToaDo.execute(urlGeoCodeing);
        }

        if(TenQuanAn.equals("") || giatoida.equals("") || giatoithieu.equals("") || GioDongCua.equals("") || GioMoCua.equals("") || hinhQuanAnList.size()==0 || listHinhDaChup.size()==0)
        {
            Toast.makeText(this, getResources().getString(R.string.KiemTraThemQuanAn), Toast.LENGTH_SHORT).show();
        }
        else {

            long GiaToiDa = Long.parseLong(giatoida);
            long GiaToiThieu = Long.parseLong(giatoithieu);

            ThemNodeQuanAnModel themNodeQuanAnModel = new ThemNodeQuanAnModel();
            // QuanAnModel quanAnModel = new QuanAnModel();
            themNodeQuanAnModel.setTenquanan(TenQuanAn);
            themNodeQuanAnModel.setGiatoida(GiaToiDa);
            themNodeQuanAnModel.setGiatoithieu(GiaToiThieu);
            themNodeQuanAnModel.setGiaohang(giaoHang);
            themNodeQuanAnModel.setLuotthich(0);
            themNodeQuanAnModel.setGiodongcua(GioDongCua);
            themNodeQuanAnModel.setGiomocua(GioMoCua);
            nodeQuanAn.child(maQuanAn).setValue(themNodeQuanAnModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ThemQuanAnActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                }
            });

            for(Bitmap hinhquan:hinhQuanAnList) {
                Calendar calendar = Calendar.getInstance();
                String TenHinh = calendar.getTimeInMillis()+".jpg";
                nodeRoot.child("hinhanhquanans").child(maQuanAn).push().setValue(TenHinh);
                // set len Storage
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                hinhquan.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("hinhanh/"+ TenHinh).putBytes(data);
            }
            for(int i=0; i<themThucDonModelList.size();i++)
            {
                nodeRoot.child("thucdonquanans").child(maQuanAn).child(themThucDonModelList.get(i).getMathucdon()).push().setValue(themThucDonModelList.get(i).getMonAnModel());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = listHinhDaChup.get(i);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] data = baos.toByteArray();

                FirebaseStorage.getInstance().getReference().child("hinhanh/"+themThucDonModelList.get(i).getMonAnModel().getHinhanh()).putBytes(data);
            }

            // đâyr các hình đã chụp trong phần thêm thực đơn lên ( lưu dạng bitmap )
            for(Bitmap bitmap:listHinhDaChup)
            {

            }
        }
    }

    // Lấy tọa độ dựa vào google map geo
    class DownLoadToaDo extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line+"\n");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray results = jsonObject.getJSONArray("results");
                for(int i=0; i<results.length();i++)
                {
                    JSONObject object = results.getJSONObject(i);
                    String address = object.getString("formatted_address");
                    JSONObject  geometry = (JSONObject) object.get("geometry");
                    JSONObject location = (JSONObject) geometry.get("location");
                    double latitude = (double) location.get("lat");
                    double longtitude = (double) location.get("lng");

                    ChiNhanhQuanAnModel chiNhanhQuanAnModel = new ChiNhanhQuanAnModel();
                    chiNhanhQuanAnModel.setDiachi(address);
                    chiNhanhQuanAnModel.setLatitude(latitude);
                    chiNhanhQuanAnModel.setLongtitude(longtitude);

                    FirebaseDatabase.getInstance().getReference().child("chinhanhquanans").child(maQuanAn).push().setValue(chiNhanhQuanAnModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private  void ChonHinhTuGallary(int request_code)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Chọn ảnh"),request_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case Request_Code_img1:
                if(resultCode == RESULT_OK)
                {
                    // Khi lấy hình trên thư viện ta nó sẽ lưu dưới dạng đường dẫn (URi)
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        imgHinhQuan1.setImageBitmap(bitmap);
                        hinhQuanAnList.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case Request_Code_img2:
                if(resultCode == RESULT_OK)
                {
                    // Khi lấy hình trên thư viện ta nó sẽ lưu dưới dạng đường dẫn (URi)
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        imgHinhQuan2.setImageBitmap(bitmap);
                        hinhQuanAnList.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Request_Code_img3:
                if(resultCode == RESULT_OK)
                {
                    // Khi lấy hình trên thư viện ta nó sẽ lưu dưới dạng đường dẫn (URi)
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        imgHinhQuan3.setImageBitmap(bitmap);
                        hinhQuanAnList.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Request_Code_img4:
                if(resultCode == RESULT_OK)
                {
                    // Khi lấy hình trên thư viện ta nó sẽ lưu dưới dạng đường dẫn (URi)
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        imgHinhQuan4.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Request_ImgThucDon:

                if(resultCode == RESULT_OK)
                {
                    // Khi lấy hình trên thư viện ta nó sẽ lưu dưới dạng đường dẫn (URi)
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        imageTam.setImageBitmap(bitmap);
                        listHinhDaChup.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                /*
                // hình chụp từ camera nó sẽ mặc định ra dạng bitmap sẵn
                Bitmap bitmap = (Bitmap) data.getExtras().get("data"); // ép kiểu data về dạng bitmap
                imageTam.setImageBitmap(bitmap);
                listHinhDaChup.add(bitmap);*/
                break;

        }


    }

    private  void CloneThucDon(){
        final View viewThucDon = LayoutInflater.from(ThemQuanAnActivity.this).inflate(R.layout.layout_clone_thucdon,null);
        final Spinner spinnerThucDon= (Spinner) viewThucDon.findViewById(R.id.spinerThucDon);
        // Bắt sự kiện Button Thêm thực đơn
        Button btnThemThucDon =(Button) viewThucDon.findViewById(R.id.btnThemThucDon);
        final Button btnXoaThucDon = (Button) viewThucDon.findViewById(R.id.btnXoaThucDon);
        final EditText edTenMon = (EditText) viewThucDon.findViewById(R.id.edTenMon);
        final EditText edGiaTien = (EditText) viewThucDon.findViewById(R.id.edGiaTien);
        ImageView imgChupHinh = (ImageView) viewThucDon.findViewById(R.id.imgChupHinh);
        imageTam = imgChupHinh;
        imgChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonHinhTuGallary(Request_ImgThucDon);
            }
        });


        //set thuc don len spiner
        ArrayAdapter<String> adapterThucDon = new ArrayAdapter<String>(ThemQuanAnActivity.this,android.R.layout.simple_list_item_1,thucdonList);
        spinnerThucDon.setAdapter(adapterThucDon);
        adapterThucDon.notifyDataSetChanged();
        // Kiểm tra nếu list có giá trị thì ta không thức hiện lấy danh sách thực đơn lần nữa để tránh lặp lại dữ liệu
        if(thucdonList.size()==0)
        {
            LayDanhSachThucDon(adapterThucDon);
        }

        khungChuaThucDon.addView(viewThucDon);

        btnThemThucDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);

                if(edTenMon.getText().toString().equals("") || edGiaTien.getText().toString().equals("")) {
                    Toast.makeText(ThemQuanAnActivity.this,getResources().getString(R.string.KiemTraThucDon), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // Lấy vị trí ta đã chọn trong spiner
                    int position = spinnerThucDon.getSelectedItemPosition();
                    String maThucDon = thucDonModelList.get(position).getMathucdon();

                    // Tạo tên cho bức ảnh bằng cách lấy ngày giờ hiện tại để khỏi trùng
                    long ThoiGian =  Calendar.getInstance().getTimeInMillis();
                    String TenHinh = String.valueOf(ThoiGian + ".jpg");

                    // Lưu dữ liệu vào MonAnModel
                    MonAnModel monAnModel = new MonAnModel();
                    monAnModel.setTenmon(edTenMon.getText().toString());
                    monAnModel.setGiatien(Long.parseLong(edGiaTien.getText().toString()));
                    monAnModel.setHinhanh(TenHinh);

                    // Add dữ liệu vào ThemThucDonModel để  đẩy lên firebase

                    ThemThucDonModel themThucDonModel = new ThemThucDonModel();
                    themThucDonModel.setMathucdon(maThucDon);
                    themThucDonModel.setMonAnModel(monAnModel);
                    themThucDonModelList.add(themThucDonModel);
                    btnXoaThucDon.setVisibility(View.VISIBLE);
                    btnXoaThucDon.setTag(themThucDonModel);
                }
                CloneThucDon();

            }
        });
        btnXoaThucDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themThucDonModelList.remove(view.getTag());
                khungChuaThucDon.removeView(viewThucDon);
                btnXoaThucDon.setVisibility(View.GONE);

            }
        });

    }

    private  void CloneChiNhanh(){
        final View viewChiNhanh = LayoutInflater.from(ThemQuanAnActivity.this).inflate(R.layout.layout_clone_chinhanh,null);
        // bắt sự kiện click vào imageButton cho những viewChiNhanh được add sau
        final ImageButton btnThemChiNhanh =(ImageButton) viewChiNhanh.findViewById(R.id.btnThemChiNhanh);
        final ImageButton btnXoaChiNhanh = (ImageButton) viewChiNhanh.findViewById(R.id.btnXoaChiNhanh);


        btnThemChiNhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText edTenChiNhanh =(EditText) viewChiNhanh.findViewById(R.id.edTenChiNhanh);
                String tenChiNhanh = edTenChiNhanh.getText().toString();
                if(tenChiNhanh.equals("") || tenChiNhanh == null)
                {
                    Toast.makeText(ThemQuanAnActivity.this,getResources().getString(R.string.KiemTraThemChiNhanh), Toast.LENGTH_SHORT).show();
                }
                else {
                    btnThemChiNhanh.setVisibility(View.GONE);
                    btnXoaChiNhanh.setVisibility(View.VISIBLE);
                    btnXoaChiNhanh.setTag(tenChiNhanh);

                    chiNhanhList.add(tenChiNhanh);
                    CloneChiNhanh();
                }
            }
        });
        btnXoaChiNhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenChiNhanh = view.getTag().toString();
                chiNhanhList.remove(tenChiNhanh);
                khungChuaChiNhanh.removeView(viewChiNhanh);
            }
        });
        khungChuaChiNhanh.addView(viewChiNhanh);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch(adapterView.getId()){
            case R.id.spinerKhuVuc:
                KhuVuc = khuVucList.get(position);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}