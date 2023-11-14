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
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;

public class PdfAddActivity extends AppCompatActivity {

    //TAG for debugging
    private static final String TAG = "ADD_PDF_TAG";

    private static final int PDF_PICK_CODE = 1000;

    //uri pdf
    private Uri pdfUri = null;

    private ArrayList<Category> categoryArrayList;

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
    }

    private String tieude="", mota = "", theloai ="";
    private void validateData() {
        EditText edtTitle = findViewById(R.id.edtTitle);
        EditText edtDescription = findViewById(R.id.edtDescription);
        TextView tvCategory = findViewById(R.id.tvCat);

        tieude = edtTitle.getText().toString().trim();
        mota = edtDescription.getText().toString().trim();
        theloai = tvCategory.getText().toString().trim();

        if(TextUtils.isEmpty(tieude)){
            Toast.makeText(this, "Nhập tiêu đề sách...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mota)) {
            Toast.makeText(this, "Nhập mô tả sách...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(theloai)) {
            Toast.makeText(this, "Chọn thể loại sách...", Toast.LENGTH_SHORT).show();
        } else if (pdfUri == null) {
            Toast.makeText(this, "Chọn Pdf...", Toast.LENGTH_SHORT).show();
        } else {
            uploadPdfToStorage();
        }
    }

    private void uploadPdfToStorage() {
        //show progress dialog
        progressDialog.setMessage("Đang tải lên...");
        progressDialog.show();

        //timestamp
        long timestamp = System.currentTimeMillis();

        //path of pdf on firebase storage
        String filePathAndName = "Books/"+timestamp;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: PDF uploaded to storage");

                        //get url pdf
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadedPdfUrl = ""+uriTask.getResult();

                        //upload to firebase db
                        uploadPdfToDB(uploadedPdfUrl, timestamp);

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

    private void uploadPdfToDB(String uploadedPdfUrl, long timestamp) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id",""+timestamp);
        hashMap.put("BookTitle", ""+tieude);
        hashMap.put("BookDescription", ""+mota);
        hashMap.put("Category",""+theloai);
        hashMap.put("url",""+uploadedPdfUrl);
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
        categoryArrayList = new ArrayList<>();

        //load data tu firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear(); //xoa du lieu da add vao tu truoc
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //getDAta
                    Category model = dataSnapshot.getValue(Category.class);
                    //add to arraylist
                    categoryArrayList.add(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void categoryPickDialog() {
        String[] categoriesArray = new String[categoryArrayList.size()];
        for(int i=0; i < categoryArrayList.size(); i++){
            categoriesArray[i] = categoryArrayList.get(i).getCategoryName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thể loại")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String category = categoriesArray[which];

                        TextView tvCat = findViewById(R.id.tvCat);
                        tvCat.setText(category);
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
        else{

            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }
}