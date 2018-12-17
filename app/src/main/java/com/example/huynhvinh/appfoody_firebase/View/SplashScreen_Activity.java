package com.example.huynhvinh.appfoody_firebase.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.huynhvinh.appfoody_firebase.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class SplashScreen_Activity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback{

    TextView txtPhienban;
    GoogleApiClient mGoogleApiClient;
    private  static final int REQUEST_PERMISSION_LOCATION = 1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_flashscreen);
        AnhXa();
        sharedPreferences = getSharedPreferences("toado",MODE_PRIVATE);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        int checkPermissionCoarseLocaltion = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkPermissionFineLocation = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if(checkPermissionCoarseLocaltion != PackageManager.PERMISSION_GRANTED && checkPermissionCoarseLocaltion != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION_LOCATION);
        }else{
            mGoogleApiClient.connect();
        }

    }

    private void AnhXa() {
        txtPhienban = (TextView) findViewById(R.id.txtVersion);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_PERMISSION_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        @SuppressLint("MissingPermission")
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient); // lấy vị trí hiện tại của người dùng

        if(location != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("latitude", String.valueOf(location.getLatitude()));           // truyền dữ liệu lên SharedPreferences
            editor.putString("longtitude", String.valueOf(location.getLongitude()));
            editor.commit();
        }

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            txtPhienban.setText(getString(R.string.phienban) + " " + packageInfo.versionName);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent iDangNhap = new Intent(SplashScreen_Activity.this,DangNhapActivity.class);
                    startActivity(iDangNhap);
                    finish();
                }
            },2000);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}