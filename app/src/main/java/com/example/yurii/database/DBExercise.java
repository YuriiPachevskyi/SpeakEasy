package com.example.yurii.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBExercise extends SQLiteOpenHelper  {
    public static String       TAG      = "DBExercise";
    public static final String DATABASE = "content.db";
    protected int              lesson_;

    public DBExercise(Context context, int lesson) {
        super(context, DATABASE, null, 1);
        lesson_ = lesson;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public String getSignature(int section) {
        return  new String();
    }

    public String getExamples(int section) {
        return new String();
    }

    public String getContent(int section) {
        return new String();
    }

    public String getSubContent(int section) {
        return  new String();
    }
}
