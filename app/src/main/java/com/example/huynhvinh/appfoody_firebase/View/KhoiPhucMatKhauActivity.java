package com.example.huynhvinh.appfoody_firebase.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huynhvinh.appfoody_firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class KhoiPhucMatKhauActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edEmailKP;
    TextView txtDangKyMoiKP;
    Button btnGuiMailKP;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_quenmatkhau);
        AnhXa();

        btnGuiMailKP.setOnClickListener(this);
        txtDangKyMoiKP.setOnClickListener(this);
    }

    private void AnhXa() {
        firebaseAuth = FirebaseAuth.getInstance();
        edEmailKP = (EditText) findViewById(R.id.edEmailKP);
        txtDangKyMoiKP = (TextView) findViewById(R.id.txtDangKyKhoiPhuc);
        btnGuiMailKP = (Button) findViewById(R.id.btnGuiMailKhoiPhuc);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id)
        {
            case R.id.btnGuiMailKhoiPhuc:
                String Email = edEmailKP.getText().toString();
                if(KiemTraEmail(Email))
                {
                        firebaseAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(KhoiPhucMatKhauActivity.this,getString(R.string.ThongBaoGuiMailThanhCong), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
                else
                {
                    Toast.makeText(this,getString(R.string.ThongBaoEmailKhongHopLe), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtDangKyKhoiPhuc:
                Intent iDangKy = new Intent(KhoiPhucMatKhauActivity.this,DangKyActivity.class);
                startActivity(iDangKy);
                break;
        }

    }

    private  boolean KiemTraEmail(String Email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(Email).matches();
    }
}
