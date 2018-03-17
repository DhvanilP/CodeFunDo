package com.example.dhp.codefundo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Person;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class CreatePerson extends AppCompatActivity {
    EditText personName;
    EditText personGroup;
    EditText personroll;
    Button submitPerson;
    ProgressDialog detectionProgressDialog;
    private FaceServiceClient faceServiceClient;
    HashMap<String, Person> personsData;
    Person[] persons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);
        personName = findViewById(R.id.personName);
        personGroup = findViewById(R.id.personGroup);
        personroll = findViewById(R.id.personRoll);
        personsData = new HashMap<>();
        faceServiceClient = new FaceServiceRestClient(MainActivity.SERVER_HOST, MainActivity.SUBSCRIPTION_KEY);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    persons = faceServiceClient.getPersons("testing");
                    for (Person person : persons) {
                        personsData.put(person.userData, person);
                    }
                    Iterator it = personsData.entrySet().iterator();
                    while (it.hasNext()) {
                        HashMap.Entry pair = (HashMap.Entry) it.next();
                        Log.v("addddddddd", pair.getKey().toString());
//                Log.v("addddddddd", pair.getValue().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();


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


        String rollNumber = personroll.getText().toString();
        if (personsData.get(rollNumber) == null) {
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
        } else {
            Toast.makeText(getApplicationContext(), "Person exists", Toast.LENGTH_SHORT).show();
            detectionProgressDialog.dismiss();
        }
    }

}
