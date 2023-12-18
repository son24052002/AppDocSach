package com.example.adminapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.adminapp.BookUpdateActivity;
import com.example.adminapp.Model.Book;
import com.example.adminapp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.HolderBookAdmin> {
    //context
    private Context context;
    private ArrayList<Book> bookArrayList;
    private ProgressDialog progressDialog;


    public BookAdapter(Context context, ArrayList<Book> bookArrayList) {
        this.context = context;
        this.bookArrayList = bookArrayList;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public HolderBookAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_books, parent,false);
        return new HolderBookAdmin(itemView.getRootView());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBookAdmin holder, int position) {
        //getdata
        Book model = bookArrayList.get(position);
        String bookTitle = model.getBookTitle();
        String bookDes = model.getBookDescription();

        //setdata
        holder.bookName.setText(bookTitle);
        holder.bookDes.setText(bookDes);

        //load data
        loadCategory(model, holder);
//        loadPdfFromUrl(model, holder);
        loadImgFromUrl(model, holder);

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreOpitionsDialog(model, holder);
            }
        });

    }

    private void moreOpitionsDialog(Book model, HolderBookAdmin holder) {
        String bookId = model.getId();
        String bookUrl = model.getUrl();
        String imgUrl = model.getImgUrl();
        String bookTitle = model.getBookTitle();

        //option dialog
        String[] options = {"Cập nhật", "Xóa"};

        //elrt dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            //sang activity sua
                            Intent intent = new Intent(context, BookUpdateActivity.class);
                            intent.putExtra("bookId", bookId );
                            intent.putExtra("imgUrl", imgUrl);
                            context.startActivity(intent);
                        } else if (which==1) {
                            //xoa
                            deleteBook(model, holder);
                        }
                    }
                })
                .show();
    }

    private void deleteBook(Book model, HolderBookAdmin holder) {
        String bookId = model.getId();
        String bookUrl = model.getUrl();
        String imgUrl = model.getImgUrl();
        String bookTitle = model.getBookTitle();

        Log.d("deleteBook", "deleting");
        progressDialog.setMessage("Đang xóa "+bookTitle);
        progressDialog.show();

        Log.d("deletebook","delete from storage");
        StorageReference bookRef = FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl);
        bookRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("deleteStorageBook", "success");
                        //xoa anh trong storage
                        StorageReference imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(imgUrl);
                        imgRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("deleteStorageImage", "success");
                                Log.d("deletebook", "xoa db");
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
                                reference.child(bookId)
                                        .removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, "Xóa thành công",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("deleteStorageBook", "erorr"+e.getMessage());
                                                progressDialog.dismiss();
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("deleteStorageImage", "error"+e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("deleteStorageBook", "error"+e.getMessage());
                    }
                });
    }

    private void loadImgFromUrl(Book model, HolderBookAdmin holder) {

        String urlImg = model.getImgUrl();
        Log.d("loadImgFromUrl", "URL: " + urlImg);
        if (urlImg != null && !urlImg.isEmpty()) {
            Log.d("loadImgFromUrl", "URL: " + urlImg);


            Glide.with(context)
                    .load(urlImg)

                    .into(holder.imgBook);
        } else {
            Log.e("loadImgFromUrl", "Invalid URL");
        }
    }



    private void loadCategory(Book model, HolderBookAdmin holder) {
        String CatId = model.getCategoryId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(CatId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get category
                        String category = ""+snapshot.child("categoryName").getValue();

                        //set category for textview
                        holder.Category.setText(category);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    class HolderBookAdmin extends RecyclerView.ViewHolder{

        //UI view
        PDFView pdfView;

        TextView bookName;
        TextView bookDes;
        TextView Category;
        ImageButton btnMore;
        ImageView imgBook;

        public HolderBookAdmin(@NonNull View itemView) {
            super(itemView);

//            pdfView = itemView.findViewById(R.id.pdfView);
            //progressBar = itemView.findViewById(R.id.progressBar);
            bookName = itemView.findViewById(R.id.bookName);
            bookDes = itemView.findViewById(R.id.bookDes);
            Category = itemView.findViewById(R.id.Category);
            btnMore = itemView.findViewById(R.id.btnMore);
            imgBook = itemView.findViewById(R.id.imgBook_rowbook);

        }
    }
}
