package com.example.dhp.codefundo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Candidate;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;
import com.microsoft.projectoxford.face.contract.IdentifyResult;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    static final String SERVER_HOST = "https://southeastasia.api.cognitive.microsoft.com/face/v1.0";
    static final String SUBSCRIPTION_KEY = "eb5c5e259ead4741b0e2792b17fbc98c";
    private static FaceServiceClient faceServiceClient;
    private final int PICK_IMAGE = 1;
    ProgressDialog detectionProgressDialog;

    private static Bitmap drawFaceRectanglesOnBitmap(Bitmap originalBitmap, Face[] faces) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        int stokeWidth = 2;
        paint.setStrokeWidth(stokeWidth);
        if (faces != null) {
            for (Face face : faces) {
                FaceRectangle faceRectangle = face.faceRectangle;
                UUID[] faceuuid = new UUID[1];
                faceuuid[0] = face.faceId;
                Log.d("print", "*********************");
                findname(faceuuid, "testing");
                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);
            }
        }
        return bitmap;
    }

    private static void findname(final UUID[] faceofperson, final String groupname) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.print("hello world");
                    IdentifyResult[] identifyResults = faceServiceClient.identity(groupname, faceofperson, 1);
                    for (Candidate candidate : identifyResults[0].candidates) {
                        Log.d("print", faceServiceClient.getPerson("testing", candidate.personId).name);
                        Log.d("print", faceServiceClient.getPerson("testing", candidate.personId).userData);
                    }
                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }

    // Detect faces by uploading face images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detectionProgressDialog = new ProgressDialog(this);
        detectionProgressDialog.setMessage("Creating person");
        detectionProgressDialog.setCanceledOnTouchOutside(false);


        faceServiceClient = new FaceServiceRestClient(SERVER_HOST, SUBSCRIPTION_KEY);
//        createGroup("lol2");
//        deleteGroup("lol");


        Button browse = findViewById(R.id.browse);
        Button createPerson = findViewById(R.id.createPerson);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallIntent = new Intent(Intent.ACTION_GET_CONTENT);
                gallIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(gallIntent, "Select Picture"), PICK_IMAGE);
                detectionProgressDialog.show();

            }
        });
        createPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreatePerson.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ImageView imageView = (ImageView) findViewById(R.id.imageView1);
                imageView.setImageBitmap(bitmap);
                detectAndFrame(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void detectAndFrame(final Bitmap imageBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, Face[]>() {
            @Override
            protected Face[] doInBackground(InputStream... params) {
                try {
                    publishProgress("Detecting...");
                    Face[] result = faceServiceClient.detect(
                            params[0],
                            true,         // returnFaceId
                            false,        // returnFaceLandmarks
                            null           // returnFaceAttributes: a string like "age, gender"
                    );
                    if (result == null) {
                        publishProgress("Detection Finished. Nothing detected");
                        return null;
                    }
                    publishProgress(
                            String.format("Detection Finished. %d face(s) detected",
                                    result.length));
                    return result;
                } catch (Exception e) {
                    publishProgress("Detection failed");
                    return null;
                }
            }

            @Override
            protected void onPreExecute() {
                //TODO: show progress dialog
                detectionProgressDialog.show();
                detectionProgressDialog.setCanceledOnTouchOutside(false);
            }

            @Override
            protected void onProgressUpdate(String... progress) {
                //TODO: update progress
                detectionProgressDialog.setMessage(progress[0]);
            }

            @Override
            protected void onPostExecute(Face[] result) {
                //TODO: update face frames
                detectionProgressDialog.dismiss();
                if (result == null) return;
                ImageView imageView = (ImageView) findViewById(R.id.imageView1);
                imageView.setImageBitmap(drawFaceRectanglesOnBitmap(imageBitmap, result));
                imageBitmap.recycle();
            }
        };
        detectTask.execute(inputStream);
    }

    // Frame faces after detection
    private void createGroup(final String student) {
        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    faceServiceClient = new FaceServiceRestClient(SERVER_HOST, SUBSCRIPTION_KEY);
                    faceServiceClient.createPersonGroup(student, "try", "lol2");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        background.start();
    }

    private void deleteGroup(final String groupName) {
        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    faceServiceClient = new FaceServiceRestClient(SERVER_HOST, SUBSCRIPTION_KEY);
                    faceServiceClient.deletePersonGroup(groupName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        background.start();
    }


}
