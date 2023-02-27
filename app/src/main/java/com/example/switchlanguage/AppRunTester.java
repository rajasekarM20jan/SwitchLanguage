package com.example.switchlanguage;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static com.example.switchlanguage.MainActivity.loadlocale;
import static com.example.switchlanguage.MainActivity.setLocale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppRunTester extends AppCompatActivity {
    Button languageSwitchButton;
    Button openCamera;
    public Bitmap rotatedBitmap, userImageBitmap;
    public static Bitmap finalBitmap;
    public static String userImageName;
    String currentPhotoPath;
    private static final int IMAGE_TAKE_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadlocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_run_tester);
        languageSwitchButton=findViewById(R.id.languageSwitchButton);
        openCamera=findViewById(R.id.cameraButton);
        openCamera.setOnClickListener(l->{
            capturePatientImage();
        });
        languageSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangelanguage(AppRunTester.this);
            }
        });
    }

    public void showChangelanguage(Activity activity) {
        try {
            final String[] items = {"Hindi", "English"};
            android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(activity);
            mBuilder.setTitle("Choose Language");
            mBuilder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        setLocale(activity, "hi");
                        recreate();
                    } else if (i == 1) {
                        setLocale(activity, "en");
                        recreate();
                    }
                    dialogInterface.dismiss();
                }
            });
            android.app.AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(activity, ex.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
    }
    //method for creating the image file
    private File createImageFile() throws IOException {
        File image=null;
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.getAbsolutePath();
            return image;
        }catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }

    void capturePatientImage() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.starhealth.hospitaltpa.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, IMAGE_TAKE_CODE);
                }
            }
            else{
                //For Pixel Mobile phones
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.switchlanguage.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takePictureIntent, IMAGE_TAKE_CODE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //method for activity result for the image
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == IMAGE_TAKE_CODE) {
                userImageBitmap = getBitmap(currentPhotoPath);
                userImageName = currentPhotoPath;

                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(currentPhotoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);


                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(userImageBitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(userImageBitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(userImageBitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = userImageBitmap;
                }
                //we must use this finalBitmap for api validations
                finalBitmap=rotatedBitmap;

                //calling the api method for validating customer picture
                //validatePicture();

                //calling the intent to next page for the time being.
                try{
                    if(finalBitmap!=null) {
                        startActivity(new Intent(AppRunTester.this,ImageViewer.class));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //method for getting the bitmap
    public Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        try {
            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            //image.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //method for rotating image
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}