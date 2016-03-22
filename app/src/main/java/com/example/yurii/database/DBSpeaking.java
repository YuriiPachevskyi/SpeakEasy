package com.example.yurii.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBSpeaking extends DBExercise {
    public static String TAG = "DBLesson";
    public static final String SPEAKING_SIGNATURE   = "SpeakingSignature";
    public static final String SPEAKING_EXAMPLE     = "SpeakingExample";
    public static final String SPEAKING_CONTENT     = "SpeakingContent";
    public static final String SPEAKING_SUB_CONTENT = "SpeakingSubContent";

    public DBSpeaking(Context context, int lesson) {
        super(context, lesson);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public String getSignature(int section) {
        String result;
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select SpeakingSignature from exercise_full where Lesson = " + lesson_
                + " and LessonSection = " + section;

        Cursor res =  db.rawQuery(query, null);
        res.moveToNext();

        if ( section == 0 ) {
            result = new String(res.getString(res.getColumnIndex(SPEAKING_SIGNATURE)));
        } else  {
            result = lesson_ + "." + section + " " +
                    new String(res.getString(res.getColumnIndex(SPEAKING_SIGNATURE)));
        }

        db.close();
        res.close();
        return result;
    }

    public String getExamples(int section) {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select SpeakingExample from exercise_full where SpeakingExample is not null and Lesson = "
                + lesson_ + " and LessonSection = " + section;
        Cursor res        =  db.rawQuery(query, null);

        if ( res.getCount() == 0 ) {
            db.close();
            res.close();

            return result;
        }
        res.moveToNext();
        result = new String(res.getString(res.getColumnIndex(SPEAKING_EXAMPLE)));
        db.close();
        res.close();

        return result;
    }

    public String getContent(int section) {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select SpeakingContent from exercise_full where SpeakingContent is not null and Lesson = "
                + lesson_ + " and LessonSection = " + section;
        Cursor res        =  db.rawQuery(query, null);

        if ( res.getCount() == 0 ) {
            db.close();
            res.close();

            return new String();
        }
        res.moveToNext();
        result = new String(res.getString(res.getColumnIndex(SPEAKING_CONTENT)));
        db.close();
        res.close();

        return result;
    }

    public String getSubContent(int section) {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select SpeakingSubContent from exercise_full where Lesson = "
                + lesson_ + " and LessonSection = " + section;
        Cursor res        =  db.rawQuery(query, null);

        if ( res.getCount() == 0 ) {
            db.close();
            res.close();

            return new String();
        }

        res.moveToNext();
        result = new String(res.getString(res.getColumnIndex(SPEAKING_SUB_CONTENT)));
        db.close();
        res.close();

        return result;
    }
}

