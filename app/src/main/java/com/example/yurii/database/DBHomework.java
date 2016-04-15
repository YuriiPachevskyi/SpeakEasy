package com.example.yurii.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHomework extends DBExercise {
    public static String TAG = "DBLesson";
    public static final String SIGNATURE = "Signature";
    public static final String EXAMPLE   = "Example";
    public static final String CONTENT   = "Content";
    public static final String ANSWER    = "Answer";
    public static final String VARIANTS  = "Variants";

    public DBHomework(Context context, int lesson) {
        super(context, lesson);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public String getSignature(int section) {
        String result;
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select Signature from homework where Lesson = " + lesson_
                + " and Section = " + section;

        Cursor res =  db.rawQuery(query, null);
        res.moveToNext();

        result = lesson_ + "." + section + " " +
                new String(res.getString(res.getColumnIndex(SIGNATURE)));

        db.close();
        res.close();
        return result;
    }

    public String getExamples(int section) {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select Example from homework where Example is not null and Lesson = "
                + lesson_ + " and Section = " + section;
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

    public String getContent(int section) {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select Content from homework where Content is not null and Lesson = "
                + lesson_ + " and Section = " + section;
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

    public String getAnswer(int section) {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select Answer from homework where Lesson = "
                + lesson_ + " and Section = " + section;
        Cursor res        =  db.rawQuery(query, null);

        if ( res.getCount() == 0 ) {
            db.close();
            res.close();

            return new String();
        }

        res.moveToNext();
        result = new String(res.getString(res.getColumnIndex(ANSWER)));
        db.close();
        res.close();

        return result;
    }

    public String getVariants(int section) {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select Variants from homework where Lesson = "
                + lesson_ + " and Section = " + section;
        Cursor res        =  db.rawQuery(query, null);

        if ( res.getCount() == 0 ) {
            db.close();
            res.close();

            return new String();
        }

        res.moveToNext();
        result = new String(res.getString(res.getColumnIndex(VARIANTS)));
        db.close();
        res.close();

        return result;
    }
}

