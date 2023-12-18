package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adminapp.databinding.ActivityBookUpdateBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BookUpdateActivity extends AppCompatActivity {
    private ActivityBookUpdateBinding binding;

    //lay bookID tu BookApdapter
    private String bookId;
    //lay imgUrl tu BookApdapter
    private String imgUrl;
    //progress dialog
    private ProgressDialog progressDialog;

    private ArrayList<String> categoryNameList, categoryIdList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        bookId = getIntent().getStringExtra("bookId");
        imgUrl = getIntent().getStringExtra("imgUrl");

        //setup progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setCanceledOnTouchOutside(false);

        loadCategories();
        loadImage();
        loadBookInfo();

        binding.tvCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog();
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnUpdateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }
    private String title="", description="";
    private void validateData() {
        title = binding.edtTitle.getText().toString().trim();
        description = binding.edtDescription.getText().toString().trim();
        Log.d("aa","title "+title);
        Log.d("aa","description "+description);
        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Tên sách không được để trống",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Mô tả không được để trống",Toast.LENGTH_SHORT).show();
        }
        else {
            SuccessUpdate();
        }
    }

    private void SuccessUpdate() {
        progressDialog.setMessage("Đang cập nhật sách");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("BookTitle",""+title);
        hashMap.put("BookDescription", ""+description);
        hashMap.put("CategoryId",""+selectedCatId);
        hashMap.put("imgUrl", ""+imgUrl);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("updatebook","success");
                        progressDialog.dismiss();
                        Toast.makeText(BookUpdateActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("updatebook", "error"+e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(BookUpdateActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void loadImage() {
        Log.d("loadImg", "dang load");

    }

    private void loadBookInfo() {
        Log.d("loadbook", "dang load");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //get data tu firebase
                        selectedCatId = ""+snapshot.child("CategoryId").getValue();
                        String des = ""+snapshot.child("BookDescription").getValue();
                        String BookTitle = ""+snapshot.child("BookTitle").getValue();
                        String imgUrl = ""+snapshot.child("imgUrl").getValue();
                        Log.d("loadImgFromUrl", "URL: " + imgUrl);
                        //set data vao view
                        binding.edtTitle.setText(BookTitle);
                        binding.edtDescription.setText(des);

                        Glide.with(getApplicationContext())
                                .load(imgUrl)
                                .into(binding.imgBook);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private  String selectedCatId="", selectedCatName="";

    private void  categoryDialog(){
        String [] categoryArray = new String[categoryNameList.size()];
        for(int i=0; i<categoryNameList.size(); i++){
            categoryArray[i] = categoryNameList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn danh mục")
                .setItems(categoryArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCatId = categoryIdList.get(which);
                        selectedCatName = categoryNameList.get(which);

                        binding.tvCat.setText(selectedCatName);
                    }
                })
                .show();
    }
    private void loadCategories() {
        Log.d("updatebook", "dang load");
        categoryIdList = new ArrayList<>();
        categoryNameList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryIdList.clear();
                categoryNameList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String id = ""+dataSnapshot.child("id").getValue();
                    String category = ""+dataSnapshot.child("categoryName").getValue();
                    categoryIdList.add(id);
                    categoryNameList.add(category);

                    Log.d("updatebook", "onDataChange :id "+id);
                    Log.d("updatebook", "onDataChange :category "+category);

                    DatabaseReference refCategory = FirebaseDatabase.getInstance().getReference("Categories");
                    refCategory.child(selectedCatId)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String category = ""+snapshot.child("categoryName").getValue();

                                    binding.tvCat.setText(category);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}