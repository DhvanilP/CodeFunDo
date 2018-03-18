package com.example.dhp.codefundo;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Person;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.IOException;
import java.util.UUID;

public class DeletePerson extends AppCompatActivity {
    static final String SERVER_HOST = "https://southeastasia.api.cognitive.microsoft.com/face/v1.0";
    static final String SUBSCRIPTION_KEY = "eb5c5e259ead4741b0e2792b17fbc98c";
    private static FaceServiceClient faceServiceClient;
    ListView simpleList;
    UUID[] b;
    String groupid;
    private String[] a;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("groupId", groupid);
        startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_person);
        faceServiceClient = new FaceServiceRestClient(SERVER_HOST, SUBSCRIPTION_KEY);

        if (getIntent().hasExtra("groupId")) {
            groupid = getIntent().getStringExtra("groupId");
            Log.d("print:", groupid);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Person[] personarray = faceServiceClient.getPersons(groupid);

            a = new String[personarray.length];
            b = new UUID[personarray.length];
            int i = 0;
            for (Person p : personarray) {
                a[i] = (i + 1) + ".)" + p.userData;
                b[i] = p.personId;
                Log.d("names:", a[i]);
                i++;
            }

            simpleList = findViewById(R.id.simpleListView);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview, R.id.textView, a);
            simpleList.setAdapter(arrayAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    faceServiceClient.deletePerson(groupid, b[position]);
                    Toast.makeText(getApplicationContext(), "Person has been Deleted", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("groupId", groupid);
                    startActivity(i);

                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}


