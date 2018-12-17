package com.example.huynhvinh.appfoody_firebase.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.huynhvinh.appfoody_firebase.Controller.DangKyController;
import com.example.huynhvinh.appfoody_firebase.Model.ThanhVienModel;
import com.example.huynhvinh.appfoody_firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DangKyActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDangKy;
    EditText edEmailDK,edPassWordDK,edNhapLaiPassWordDK;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DangKyController dangKyController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangky);

        AnhXa();

        btnDangKy.setOnClickListener(this);
    }

    private void AnhXa() {
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        btnDangKy= (Button) findViewById(R.id.btnDangKy);
        edEmailDK = (EditText) findViewById(R.id.edEmailDK);
        edPassWordDK = (EditText) findViewById(R.id.edPasswordDK);
        edNhapLaiPassWordDK = (EditText) findViewById(R.id.edNhapLaiPasswordDK);
    }

    @Override
    public void onClick(View view) {
            progressDialog.setMessage(getString(R.string.DangXuLy));
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            final String Email = edEmailDK.getText().toString();
            String MatKhau = edPassWordDK.getText().toString();
            String NhapLaiMatKhau = edNhapLaiPassWordDK.getText().toString();
            String ThongBaoLoi = getString(R.string.ThongBaoLoiDangKy);

            if(Email.trim().length() == 0)
            {
                ThongBaoLoi +=getString(R.string.Email);
                Toast.makeText(this,ThongBaoLoi, Toast.LENGTH_SHORT).show();
            }
            else if(MatKhau.trim().length() ==0)
            {
                ThongBaoLoi +=getString(R.string.MatKhau);
                Toast.makeText(this,ThongBaoLoi, Toast.LENGTH_SHORT).show();

            }
            else if(!NhapLaiMatKhau.equals(MatKhau))
            {
                Toast.makeText(this,getString(R.string.ThongBaoLoiNhapLaiMatKhau), Toast.LENGTH_SHORT).show();
            }
            else
            {
                firebaseAuth.createUserWithEmailAndPassword(Email,MatKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            ThanhVienModel thanhVienModel = new ThanhVienModel();       // set Thông tin thành viên
                            thanhVienModel.setHoten(Email);
                            thanhVienModel.setHinhanh("user.png");
                            thanhVienModel.setRole("false");
                            String uid = task.getResult().getUser().getUid();  // Lấy tên uid của người đăng nhập trên firebasae
                            dangKyController = new DangKyController();
                            dangKyController.ThemThanhVienController(thanhVienModel,uid); // Gọi qua controller để thêm thành viên
                            Toast.makeText(DangKyActivity.this,getString(R.string.DangKyThanhCong), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
    }
}