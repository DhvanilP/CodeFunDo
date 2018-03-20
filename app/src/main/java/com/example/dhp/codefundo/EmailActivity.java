package com.example.dhp.codefundo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class EmailActivity extends Activity implements Imageutils.ImageAttachmentListener {
    Button send;
    ImageView imageView;
    Imageutils imageutils;
    String pathOfImage;
    private Bitmap bitmap;
    private String file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        imageutils = new Imageutils(this);
        imageView = findViewById(R.id.imageViewEmail);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });


        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                // TODO Auto-generated method stub

                try {
                    GMailSender sender = new GMailSender(
                            "cfdwhitewalkers@gmail.com",
                            "whitewalkerscfd");

                    Log.d("get the path of storage", pathOfImage);
//                    File imgFile = new File(pathOfImage);
//                    if (imgFile.exists()) {
//                        Bitmap myBitmap = BitmapFactory.decodeFile(pathOfImage);
//                        Toast.makeText(getApplicationContext(), "Image found", Toast.LENGTH_LONG).show();
//                        imageView.setImageBitmap(myBitmap);
//                    }
                    sender.addAttachment(pathOfImage, "Try");
                    sender.sendMail("Test mail", "This mail has been sent from android app along with attachment",
                            "cfdwhitewalkers@gmail.com",
                            "suyashghuge@gmail.com");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.bitmap = file;
        this.file_name = filename;
        imageView.setImageBitmap(file);
        String path;
        path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);
        Log.d("get the path of storage", path + filename);
        pathOfImage = path + filename;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);
    }
}
