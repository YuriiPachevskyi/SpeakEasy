package com.example.yurii.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.yurii.speakeasy.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseCopyist extends SQLiteOpenHelper {
    public static String TAG      = "DataBaseCopyist";
    public static String DATABASE = "content.db";
    private Context myContext;

    public DataBaseCopyist(Context context) throws IOException {
        super(context, DATABASE, null, 1);
        myContext = context;
        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void createDataBase() throws IOException {
//        if ( checkDataBase() ){
//            Log.i(TAG, "Database already exist");
//        } else {
            Log.i(TAG, "Try to create database");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
//        }
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try {
            String myPath = "/data/data/" + myContext.getPackageName() + "/databases/" + DATABASE;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e(TAG, "database does't exist yet.");
        }

        if ( checkDB != null ){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException{
        InputStream myInput = myContext.getResources().openRawResource(R.raw.content);
        String outFileName = "/data/data/" + myContext.getPackageName() + "/databases/" + DATABASE;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;

        while ( (length = myInput.read(buffer)) > 0 ){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.i(TAG, "Copying data base finished");
    }

}
