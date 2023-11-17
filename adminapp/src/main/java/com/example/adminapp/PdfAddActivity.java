package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminapp.Model.Book;
import com.example.adminapp.Model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PdfAddActivity extends AppCompatActivity {

    //TAG for debugging
    private static final String TAG = "ADD_PDF_TAG";

    private static final int PDF_PICK_CODE = 1000;

    //uri pdf
    private Uri pdfUri = null;
    private Uri imgUri = null;
    ImageView imgBook;
    private ArrayList<String> categoryTitleArrayList, categoryIdArrayList;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_add);

        //load category
        loadCategories();

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng đợi...");
        progressDialog.setCanceledOnTouchOutside(false);

        imgBook = findViewById(R.id.imgBook);

        //nut tro lai
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //nut gan pdf
        ImageButton btnAttach = findViewById(R.id.btnAttach);
        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfPickIntent();
            }
        });

        //chon category
        TextView tvCat = findViewById(R.id.tvCat);
        tvCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryPickDialog();
            }
        });

        //upload pdf
        Button btnAdd = findViewById(R.id.btnAddBook);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        //chon img
        Button btnSelectImg = findViewById(R.id.btnSelectImg);

        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg();
            }
        });
    }

    private void selectImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    private String tieude="", mota = "";
    private void validateData() {
        EditText edtTitle = findViewById(R.id.edtTitle);
        EditText edtDescription = findViewById(R.id.edtDescription);
        TextView tvCategory = findViewById(R.id.tvCat);

        tieude = edtTitle.getText().toString().trim();
        mota = edtDescription.getText().toString().trim();


        if(TextUtils.isEmpty(tieude)){
            Toast.makeText(this, "Nhập tiêu đề sách...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mota)) {
            Toast.makeText(this, "Nhập mô tả sách...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(selectedCategoryName)) {
            Toast.makeText(this, "Chọn thể loại sách...", Toast.LENGTH_SHORT).show();
        } else if (pdfUri == null) {
            Toast.makeText(this, "Chọn Pdf...", Toast.LENGTH_SHORT).show();
        } else if (imgUri == null) {
            Toast.makeText(this, "Chọn ảnh...",Toast.LENGTH_SHORT).show();
        } else {
            uploadPdfToStorageAndImage();

        }
    }

    public void uploadImageToStorage(String uploadedPdfUrl, long timestamp, Uri imgUri) {

        //path cua img tren firebase storage
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        Date dateNow = new Date();
        String fileName = format.format(dateNow);


        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);
        storageReference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //get url imgbook
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadedImgBookUrl = ""+uriTask.getResult();

                        //upload to firebase db
                        uploadToDatabase(uploadedImgBookUrl, uploadedPdfUrl, timestamp);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PdfAddActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadPdfToStorageAndImage() {
        //show progress dialog
        progressDialog.setMessage("Đang tải lên...");
        progressDialog.show();

        //timestamp
        long timestamp = System.currentTimeMillis();

        //path cua img tren firebase storage
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        Date dateNow = new Date();
        String fileName = format.format(dateNow);


        StorageReference imgStorageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);

        //path of pdf on firebase storage
        String filePathAndName = "Books/"+timestamp;
        StorageReference pdfStorageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        pdfStorageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: PDF uploaded to storage");

                        //get url pdf
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadedPdfUrl = ""+uriTask.getResult();


                        uploadImageToStorage(uploadedPdfUrl, timestamp, imgUri);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: "+e.getMessage());
                        Toast.makeText(PdfAddActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadToDatabase(String uploadedImgUrl, String uploadedPdfUrl,  long timestamp) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id",""+timestamp);
        hashMap.put("BookTitle", ""+tieude);
        hashMap.put("BookDescription", ""+mota);
        hashMap.put("CategoryId",""+selectedCategoryId);
        hashMap.put("url",""+uploadedPdfUrl);
        hashMap.put("imgUrl",""+uploadedImgUrl);
        hashMap.put("timestamp",timestamp);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Books");
        databaseReference.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfAddActivity.this, "Sucessfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfAddActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCategories() {
        categoryTitleArrayList = new ArrayList<>();
        categoryIdArrayList = new ArrayList<>();
        //load data tu firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryTitleArrayList.clear(); //xoa du lieu da add vao tu truoc
                categoryIdArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //lay id va ten của category
                    String categoryId = ""+dataSnapshot.child("id").getValue();
                    String categoryName = ""+dataSnapshot.child("categoryName").getValue();

                    categoryIdArrayList.add(categoryId);
                    categoryTitleArrayList.add(categoryName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String selectedCategoryId, selectedCategoryName;
    private void categoryPickDialog() {
        String[] categoriesArray = new String[categoryTitleArrayList.size()];
        for(int i = 0; i < categoryTitleArrayList.size(); i++){
            categoriesArray[i] = categoryTitleArrayList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thể loại")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCategoryName = categoryTitleArrayList.get(which);
                        selectedCategoryId = categoryIdArrayList.get(which);

                        TextView tvCat = findViewById(R.id.tvCat);
                        tvCat.setText(selectedCategoryName);
                    }
                })
                .show();
    }

    private void pdfPickIntent() {
        Log.d(TAG, "pdfPickIntent: starting pdf pick intent");

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select Pdf "),PDF_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if (requestCode == PDF_PICK_CODE){
                Log.d(TAG, "onActivityResult: PDF Picked");

                pdfUri = data.getData();
                Log.d(TAG, "onActivityResult: URI: "+pdfUri);
            }
        }

        if(requestCode == 100 && resultCode == RESULT_OK){
            if(data != null){
                imgUri = data.getData();
                imgBook.setImageURI(imgUri);
            }
            else {
                // Xử lý khi không có dữ liệu trả về từ việc chọn ảnh
                Toast.makeText(this, "Không có dữ liệu ảnh được chọn", Toast.LENGTH_SHORT).show();
            }
        }

    }
}