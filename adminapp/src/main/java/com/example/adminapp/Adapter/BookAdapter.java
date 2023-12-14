package com.example.adminapp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;
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
    //view binding


    public BookAdapter(Context context, ArrayList<Book> bookArrayList) {
        this.context = context;
        this.bookArrayList = bookArrayList;
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
        String bookDes = model.getBookDes();

        //setdata
        holder.bookName.setText(bookTitle);
        holder.bookDes.setText(bookDes);

        //load data
        loadCategory(model, holder);
        loadPdfFromUrl(model, holder);
        loadImgFromUrl(model, holder);


    }

    private void loadImgFromUrl(Book model, HolderBookAdmin holder) {
        String urlImg = model.getUrlImg();
//        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(urlImg);
//        ref.getMetadata()
//                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
//                    @Override
//                    public void onSuccess(StorageMetadata storageMetadata) {
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("imgUrl");
        ref.child(urlImg)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Glide.with(context)
                                .load(urlImg)
                                .into(holder.imgBook);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadPdfFromUrl(Book model, HolderBookAdmin holder) {

    }

    private void loadCategory(Book model, HolderBookAdmin holder) {
        String CatId = model.getCategoryId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(CatId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get category
                        String category = ""+snapshot.child("Categories").getValue();

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
        ProgressBar progressBar;
        TextView bookName;
        TextView bookDes;
        TextView Category;
        ImageButton btnMore;
        ImageView imgBook;

        public HolderBookAdmin(@NonNull View itemView) {
            super(itemView);

            pdfView = itemView.findViewById(R.id.pdfView);
            //progressBar = itemView.findViewById(R.id.progressBar);
            bookName = itemView.findViewById(R.id.bookName);
            bookDes = itemView.findViewById(R.id.bookDes);
            Category = itemView.findViewById(R.id.Category);
            btnMore = itemView.findViewById(R.id.btnMore);
            imgBook = itemView.findViewById(R.id.imgBook);

        }
    }
}
