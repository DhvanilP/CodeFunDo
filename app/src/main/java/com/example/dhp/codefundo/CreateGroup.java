package com.example.dhp.codefundo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.microsoft.projectoxford.face.FaceServiceRestClient;

public class CreateGroup extends AppCompatActivity {

    static final String SERVER_HOST = "https://southeastasia.api.cognitive.microsoft.com/face/v1.0";
    static final String SUBSCRIPTION_KEY = "eb5c5e259ead4741b0e2792b17fbc98c";
    String persongroupId;
    String persongroupname;
    String persongroupuserdata;
    FaceServiceRestClient faceServiceClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        EditText groupid = (EditText) findViewById(R.id.groupidname);
        EditText groupname = (EditText) findViewById(R.id.groupname);
        EditText groupuserdata = (EditText) findViewById(R.id.groupuserdata);
        persongroupId = groupid.getText().toString().trim();
        persongroupname = groupname.getText().toString().trim();
        persongroupuserdata = groupuserdata.getText().toString().trim();

    }

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

}

