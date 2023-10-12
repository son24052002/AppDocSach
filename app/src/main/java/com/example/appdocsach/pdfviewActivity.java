package com.example.appdocsach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class pdfviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        PDFView pdfView = findViewById(R.id.pdfview);
        pdfView.fromAsset("meodayhaiaubay.pdf").load();
    }
}