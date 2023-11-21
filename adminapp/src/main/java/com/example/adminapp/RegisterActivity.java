package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.adminapp.databinding.ActivityDashboardBinding;
import com.example.adminapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

private ActivityRegisterBinding binding;

private FirebaseAuth firebaseAuth;



private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

    progressDialog = new ProgressDialog(this);
    progressDialog.setTitle("Vui lòng đợi");
    progressDialog.setCanceledOnTouchOutside(false);
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

   private String name = "", user = "" , password = "";
    private void validateData() {

        name = binding.edtName.getText().toString().trim();
        user = binding.edtUser.getText().toString().trim();
        password = binding.edtPass.getText().toString().trim();
        String repassword = binding.edtRePass.getText().toString().trim();

        if (TextUtils.isEmpty(name)){

            Toast.makeText(this , "Nhập Tên....",Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(user)){
            Toast.makeText(this , "Nhập Tên Tài Khoản....",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Nhập Mật Khẩu....", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(repassword)) {
            Toast.makeText(this, "Xác Thực lại mật khẩu....", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(repassword)){
            Toast.makeText(this, "Mật khẩu không giống....", Toast.LENGTH_SHORT).show();
        }
        else {
            CreateUserAccount();
        }
    }

    private void CreateUserAccount() {

        progressDialog.setMessage("Đang tạo....");

        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(user ,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                            UpdateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void UpdateUserInfo() {
        progressDialog.setMessage("Đang lưu.....");

        long timestamp = System.currentTimeMillis();

        String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("user", user);
        hashMap.put("name", name);
        hashMap.put("timestamp", timestamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Tài khoản đã được tạo...",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this,activity_Dashboard.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


    }


}
