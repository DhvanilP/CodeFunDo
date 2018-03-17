package com.example.dhp.codefundo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.IOException;

public class CreatePerson extends AppCompatActivity {
    EditText personName;
    EditText personGroup;
    EditText personroll;
    Button submitPerson;
    ProgressDialog detectionProgressDialog;
    private FaceServiceClient faceServiceClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);
        personName = findViewById(R.id.personName);
        personGroup = findViewById(R.id.personGroup);
        personroll = findViewById(R.id.personRoll);
        faceServiceClient = new FaceServiceRestClient(MainActivity.SERVER_HOST, MainActivity.SUBSCRIPTION_KEY);

        submitPerson = findViewById(R.id.submitPerson);
        submitPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    createPerson();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void createPerson() throws IOException, ClientException {
        detectionProgressDialog = new ProgressDialog(this);
        detectionProgressDialog.setMessage("Creating person");
        detectionProgressDialog.show();
        detectionProgressDialog.setCanceledOnTouchOutside(false);

        Thread background = new Thread() {
            @Override
            public void run() {
                String name = personName.getText().toString();
                String group = personGroup.getText().toString();
                String rollNumber = personroll.getText().toString();
                try {
                    faceServiceClient.createPerson(group, name, rollNumber);
                    detectionProgressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    detectionProgressDialog.dismiss();
                }

            }
        };
        background.start();
    }

}
