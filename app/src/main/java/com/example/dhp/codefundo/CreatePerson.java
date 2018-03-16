package com.example.dhp.codefundo;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;

public class CreatePerson extends AppCompatActivity {
    EditText personName;
    EditText personGroup;
    EditText personroll;
    Button submitPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);
        personName = findViewById(R.id.personName);
        personGroup = findViewById(R.id.personGroup);
        personroll = findViewById(R.id.personRoll);

        submitPerson = findViewById(R.id.submitPerson);
        submitPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPerson();
            }
        });

    }

    private void createPerson() {
        Thread backGround = new Thread() {
            @Override
            public void run() {
                String name = personName.getText().toString();
                String group = personGroup.getText().toString();
                String rollNumber = personroll.getText().toString();
                Log.d("createPerson", group + " ::::::::  " + name);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpClient httpclient = HttpClients.createDefault();

                try {
                    URIBuilder builder = new URIBuilder("https://southeastasia.api.cognitive.microsoft.com/face/v1.0/persongroups/" + group + "/persons");


                    URI uri = builder.build();
                    HttpPost request = new HttpPost(uri);
                    request.setHeader("Content-Type", "application/json");
                    request.setHeader("Ocp-Apim-Subscription-Key", MainActivity.SUBSCRIPTION_KEY);


                    // Request body
                    StringEntity reqEntity = new StringEntity("{ \"name\": \"" + name + "\",\"userData\": \"" + rollNumber + "\" }");
                    request.setEntity(reqEntity);

                    HttpResponse response = httpclient.execute(request);
                    Log.d("custom", "111111111111111111111111111111111111111");
                    Log.d("custom", response.toString());
                    HttpEntity entity = response.getEntity();


                    if (entity != null) {
                        Log.d("custom", "11111111111111dfghjkl;'");
                        System.out.println(EntityUtils.toString(entity));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        backGround.start();

    }

}
