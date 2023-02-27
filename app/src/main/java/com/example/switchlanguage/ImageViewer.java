package com.example.switchlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewer extends AppCompatActivity {

    ImageView myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        myImage=findViewById(R.id.myImageView);
        myImage.setImageBitmap(AppRunTester.finalBitmap);
    }
}