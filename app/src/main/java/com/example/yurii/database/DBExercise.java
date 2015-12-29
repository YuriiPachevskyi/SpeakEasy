package com.example.yurii.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBExercise extends SQLiteOpenHelper  {
    public static String TAG = "DBExercise";
    public static final String DATABASE    = "content.db";
    public static final String SIGNATURE   = "Signature";
    public static final String EXAMPLE     = "Example";
    public static final String CONTENT     = "Content";
    public static final String SUB_CONTENT = "SubContent";

    protected int       lesson_;
    protected int       section_;

    public DBExercise(Context context, int lesson, int section) {
        super(context, DATABASE, null, 1);
        lesson_      = lesson;
        section_     = section;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public String getSignature() {
        String result;
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select Signature from exercise where Lesson = " + lesson_
                + " and Section = " + section_;
        Cursor res =  db.rawQuery(query, null);
        res.moveToNext();

        result = lesson_ + "." + section_ + " " +
                new String(res.getString(res.getColumnIndex(SIGNATURE)));

        db.close();
        res.close();
        return result;
    }

    public String getExamples() {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select Example from exercise where Example is not null and Lesson = "
                + lesson_ + " and Section = " + section_;
        Cursor res        =  db.rawQuery(query, null);

        if ( res.getCount() == 0 ) {
            db.close();
            res.close();

            return result;
        }
        res.moveToNext();
        result = new String(res.getString(res.getColumnIndex(EXAMPLE)));
        db.close();
        res.close();

        return result;
    }

    public String getContent() {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select Content from exercise where Content is not null and Lesson = "
                + lesson_ + " and Section = " + section_;
        Cursor res        =  db.rawQuery(query, null);

        if ( res.getCount() == 0 ) {
            db.close();
            res.close();

            return new String();
        }
        res.moveToNext();
        result = new String(res.getString(res.getColumnIndex(CONTENT)));
        db.close();
        res.close();

        return result;
    }

    public String getSubContent() {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select SubContent from exercise where Lesson = "
                + lesson_ + " and Section = " + section_;
        Cursor res        =  db.rawQuery(query, null);

        if ( res.getCount() == 0 ) {
            db.close();
            res.close();

            return new String();
        }
        Log.e(TAG, " count = " + res.getCount());

        res.moveToNext();
        result = new String(res.getString(res.getColumnIndex(SUB_CONTENT)));
        db.close();
        res.close();

        return result;
    }
}
