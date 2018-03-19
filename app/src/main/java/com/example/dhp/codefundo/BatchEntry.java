package com.example.dhp.codefundo;

/**
 * Created by Suyash on 19-03-2018.
 */

import android.provider.BaseColumns;

/**
 * API Contract for the Pets app.
 */
public final class BatchEntry {

    public static String TABLE_NAME;

    public final static String rollNumber = "rollNumber";

    public final static String studentName = "studentName";

    public final static String markedAttendence = "markedAttendence";

    public final static String totalAttendence = "totalAttendence";


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public BatchEntry(String tableName) {
        this.TABLE_NAME = tableName;
    }


}

