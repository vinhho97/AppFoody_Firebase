package com.example.huynhvinh.appfoody_firebase.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huynhvinh.appfoody_firebase.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;

public class DangNhapActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, FirebaseAuth.AuthStateListener {

    Button btnDangNhapGoogle;
    Button btnDangNhapFacebook;
    Button btnDangNhap;
    TextView txtDangKyMoi,txtQuenMatKhau;
    EditText edEmail,edPassWord;
    ProgressDialog progressDialog;

    GoogleApiClient signInApi;
    FirebaseAuth firebaseAuth;
    CallbackManager callbackManager;
    LoginManager loginManager;
    List<String> permissionFacebook = Arrays.asList("email","public_profile");
    SharedPreferences sharedPreferences;

    public static int CODE_DANG_NHAP_GOOGLE = 123;
    public static  int  KIEMTRA_PROVIDER_DANGNHAP =0; // Kiểm tra xem người dùng đang đăng nhập bằng facebook hay google

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext()); // Khởi tạo facebookSDK

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangnhap);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        AnhXa();

        btnDangNhapGoogle.setOnClickListener(this);
        TaoClientDangNhapGoogle();          // Khởi tạo để lấy thông tin người dùng

        // start facebook
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        btnDangNhapFacebook.setOnClickListener(this);
        // end facebook

        // Đăng ký mới,Quên mật khẩu
        txtDangKyMoi.setOnClickListener(this);
        txtQuenMatKhau.setOnClickListener(this);
        // End

        // Đăng nhập
        btnDangNhap.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("luudangnhap",MODE_PRIVATE);


    }

    private void AnhXa() {
        btnDangNhapGoogle = (Button) findViewById(R.id.btnDangNhapGoogle);
        btnDangNhapFacebook = (Button) findViewById(R.id.btnDangNhapFacebook);
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        txtDangKyMoi = (TextView) findViewById(R.id.txtDangKyMoi);
        txtQuenMatKhau = (TextView) findViewById(R.id.txtQuenMatKhau);
        edEmail = (EditText) findViewById(R.id.edEmailDN);
        edPassWord = (EditText) findViewById(R.id.edPasswordDN);
        progressDialog = new ProgressDialog(this);
    }

    private  void DangNhapFacebook(){
        loginManager.logInWithReadPermissions(this,permissionFacebook);  // Ta xin quyền lấy email và public profile
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
                public void onSuccess(LoginResult loginResult) {
                KIEMTRA_PROVIDER_DANGNHAP = 2;
                String tokenID = loginResult.getAccessToken().getToken();
                ChungThucDangNhapFirebase(tokenID);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    // Khởi tạo cho google để lấy thông tin người dùng
    private  void TaoClientDangNhapGoogle()
    {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  // Khởi tạo để lấy thông tin tài khoảng google
                .requestEmail().requestIdToken(getString(R.string.default_web_client_id))  // để chứng thực google
                .requestProfile()
                .build();

        signInApi = new GoogleApiClient.Builder(this)   // Khởi tạo API cho google để có thể kết nối
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();
    }

    // end Khởi tạo cho google để lấy thông tin người dùng

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    // Mở form đăng nhập bằng google
    private void DangNhapGoogle(GoogleApiClient apiClient)
    {
        KIEMTRA_PROVIDER_DANGNHAP = 1;
        Intent iDNGoogle = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(iDNGoogle,CODE_DANG_NHAP_GOOGLE);
    }
    // end Mở form đăng nhập bằng google

    // Lấy tokenID đã đăng nhập bằng google để đăng nhập trên firebase
    private void ChungThucDangNhapFirebase(String tokenID){

        if(KIEMTRA_PROVIDER_DANGNHAP==1)
        {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(tokenID,null); // Tạo chứng thực cho google thông qua provider
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                }
            });
        }
        else if(KIEMTRA_PROVIDER_DANGNHAP==2)
        {

            AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenID);
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                }
            });
        }

    }

    // End

    // end Lấy tokenID đã đăng nhập bằng google để đăng nhập trên firebase

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODE_DANG_NHAP_GOOGLE)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(this,data.toString(), Toast.LENGTH_SHORT).show();

                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data); // lấy thông tin người dùng đăng nhập được
                GoogleSignInAccount account = result.getSignInAccount(); // Lấy tài khoản google
                String tokenID = account.getIdToken();      // Lấy tokenID thông qua account
                ChungThucDangNhapFirebase(tokenID);
            }
        }
        else{
            callbackManager.onActivityResult(requestCode,resultCode,data); // lấy dữ liệu để kích hoạt hàm onsuccess khi đăng nhập facebook thành công
        }
    }

    // End
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    // Lắng nghe sự kiện user click  vào button đăng nhập google, facebook và email
    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id)
        {
            case R.id.btnDangNhapGoogle:
                DangNhapGoogle(signInApi);
                break;
            case R.id.btnDangNhapFacebook:
                DangNhapFacebook();
                break;
            case R.id.txtDangKyMoi:
                Intent iDangKy = new Intent(DangNhapActivity.this,DangKyActivity.class);
                startActivity(iDangKy);
                break;
            case R.id.btnDangNhap:
                DanhNhap();
                break;
            case R.id.txtQuenMatKhau:
                Intent iQuenMatKhau = new Intent(DangNhapActivity.this,KhoiPhucMatKhauActivity.class);
                startActivity(iQuenMatKhau);
                break;
        }

    }

    // end Lắng nghe sự kiện user click  vào button đăng nhập google, facebook và email

    // Đăng nhập bằng google và password vừa tạo

    private  void DanhNhap(){
        progressDialog.setMessage(getString(R.string.DangXuLy));
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        String Email = edEmail.getText().toString();
        String PassWord = edPassWord.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(Email,PassWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(DangNhapActivity.this,getString(R.string.KiemTraDangNhap), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.dismiss();
                }
            }
        });

    }

    // khi đăng nhập bằng google, hoặc Email,Password(tự tạo)  thành công hàm này sẽ kích hoạt (thay đổi trạng thái) (Để kiểm tra người dùng đã đăng nhập thành công hay chưa)
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        FirebaseUser user   = firebaseAuth.getCurrentUser();        // Lấy user vừa đăng nhập được
        if(user!=null)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("mauser",user.getUid());
            editor.commit();

            Intent iTrangChu = new Intent(this,TrangChuActivity.class);
            startActivity(iTrangChu);

        }

    }
    // end
}