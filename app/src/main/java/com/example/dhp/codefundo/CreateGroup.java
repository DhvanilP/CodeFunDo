package com.example.dhp.codefundo;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.PersonGroup;

public class CreateGroup extends AppCompatActivity {

    static final String SERVER_HOST = "https://southeastasia.api.cognitive.microsoft.com/face/v1.0";
    static final String SUBSCRIPTION_KEY = "eb5c5e259ead4741b0e2792b17fbc98c";
    String persongroupId;
    String persongroupname;
    String persongroupuserdata;
    FaceServiceRestClient faceServiceClient;
    EditText groupid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        groupid = (EditText) findViewById(R.id.groupidname);
        final EditText groupname = (EditText) findViewById(R.id.groupname);
        final EditText groupuserdata = (EditText) findViewById(R.id.groupuserdata);


        Button create = (Button)findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                persongroupId = groupid.getText().toString().trim();
                persongroupname = groupname.getText().toString().trim();
                persongroupuserdata = groupuserdata.getText().toString().trim();
                createGroup(persongroupId,persongroupname,persongroupuserdata);

            }
        });

    }

    private void createGroup(final String grpid,final String grpname,final String grpuserdata ) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
        StrictMode.setThreadPolicy(policy);

        try {
            faceServiceClient = new FaceServiceRestClient(SERVER_HOST, SUBSCRIPTION_KEY);
            PersonGroup[] all_person_groups = faceServiceClient.getPersonGroups();
            int flag=0;
            for (PersonGroup p : all_person_groups) {
                if(p.personGroupId.equals(grpid)){
                    flag=1;
                    groupid.setError("This group already exist");
                    groupid.requestFocus();
                    return;
                }
            }
            if(flag==0){
                faceServiceClient.createPersonGroup(grpid,grpname,grpuserdata);
                Toast.makeText(getApplicationContext(),"Person Group has been created",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(),HomePage.class);
                startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

