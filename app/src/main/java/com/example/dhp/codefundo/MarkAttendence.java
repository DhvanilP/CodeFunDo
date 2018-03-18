package com.example.dhp.codefundo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.UUID;

public class MarkAttendence extends AppCompatActivity {
    ListView simpleList;
    UUID[] b;
    String groupid;
    private String[] personnames,personrolls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendence);
        personnames = getIntent().getStringArrayExtra("personsnames");
        personrolls=getIntent().getStringArrayExtra("personrolls");
        simpleList = (ListView) findViewById(R.id.simpleListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview, R.id.textView, personnames);
        simpleList.setAdapter(arrayAdapter);

    }
}

