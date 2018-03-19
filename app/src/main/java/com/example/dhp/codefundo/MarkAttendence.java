package com.example.dhp.codefundo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class MarkAttendence extends AppCompatActivity {
    ListView simpleList;
    UUID[] b;
    String groupid;
    private String[] personnames, personrolls;
    String date;
    int[] mAttendance, tAttendance;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),PersonGroup.class);
        i.putExtra("groupId",groupid);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendence);

        personnames = getIntent().getStringArrayExtra("personsnames");
        personrolls=getIntent().getStringArrayExtra("personrolls");
        groupid=getIntent().getStringExtra("groupId");
        simpleList = (ListView) findViewById(R.id.simpleListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview, R.id.textView, personnames);
        simpleList.setAdapter(arrayAdapter);

        mAttendance = new int[personrolls.length];
        tAttendance = new int[personrolls.length];

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.v("Date is: ", date);

        AttendanceDbHelper attendanceDbHelper = new AttendanceDbHelper(getApplicationContext());
        SQLiteDatabase db = attendanceDbHelper.getReadableDatabase();
        for(int i = 0; i < personrolls.length; i++)
        {
            String query = "select markedAttendence, totalAttendence from " + groupid + " where rollNumber = \""+personrolls[i]+"\"";
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Log.v("rollNumber ", c.getString(0));
                mAttendance[i] = c.getInt(0);
                tAttendance[i] = c.getInt(1);
                c.moveToNext();
            }
        }
        db.close();

        SQLiteDatabase dbs = attendanceDbHelper.getWritableDatabase();
        for(int i = 0; i < personrolls.length; i++)
        {
            ContentValues values = new ContentValues();
            values.put(BatchEntry.markedAttendence, mAttendance[i] + 1);
            values.put(BatchEntry.totalAttendence, tAttendance[i] + 1);
            dbs.update(groupid, values, "rollNumber = \"" + personrolls[i] + "\"", null);
        }
        dbs.close();

        SQLiteDatabase db1 = attendanceDbHelper.getReadableDatabase();
        for(int i = 0; i < personrolls.length; i++)
        {
            String query = "select markedAttendence, totalAttendence from " + groupid + " where rollNumber = \""+personrolls[i]+"\"";
            Cursor c = db1.rawQuery(query, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Log.v("Attendance ", c.getInt(0) + "" + c.getInt(1));
                c.moveToNext();
            }
        }
        db1.close();

    }
}

