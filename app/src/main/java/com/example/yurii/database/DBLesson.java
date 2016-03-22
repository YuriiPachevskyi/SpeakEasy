package com.example.yurii.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBLesson extends DBExercise {
    public static String TAG = "DBLesson";
    public static final String LESSON_SIGNATURE   = "LessonSignature";
    public static final String LESSON_EXAMPLE     = "LessonExample";
    public static final String LESSON_CONTENT     = "LessonContent";
    public static final String LESSON_SUB_CONTENT = "LessonSubContent";

    public DBLesson(Context context, int lesson) {
        super(context, lesson);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public String getSignature(int section) {
        String result;
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select LessonSignature from exercise_full where Lesson = " + lesson_
                + " and LessonSection = " + section;

        Cursor res =  db.rawQuery(query, null);
        res.moveToNext();

        result = lesson_ + "." + section + " " +
                new String(res.getString(res.getColumnIndex(LESSON_SIGNATURE)));

        db.close();
        res.close();
        return result;
    }

    public String getExamples(int section) {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select LessonExample from exercise_full where LessonExample is not null and Lesson = "
                + lesson_ + " and LessonSection = " + section;
        Cursor res        =  db.rawQuery(query, null);

        if ( res.getCount() == 0 ) {
            db.close();
            res.close();

            return result;
        }
        res.moveToNext();
        result = new String(res.getString(res.getColumnIndex(LESSON_EXAMPLE)));
        db.close();
        res.close();

        return result;
    }

    public String getContent(int section) {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select LessonContent from exercise_full where LessonContent is not null and Lesson = "
                + lesson_ + " and LessonSection = " + section;
        Cursor res        =  db.rawQuery(query, null);

        if ( res.getCount() == 0 ) {
            db.close();
            res.close();

            return new String();
        }
        res.moveToNext();
        result = new String(res.getString(res.getColumnIndex(LESSON_CONTENT)));
        db.close();
        res.close();

        return result;
    }

    public String getSubContent(int section) {
        String result     = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "select LessonSubContent from exercise_full where Lesson = "
                + lesson_ + " and LessonSection = " + section;
        Cursor res        =  db.rawQuery(query, null);

        if ( res.getCount() == 0 ) {
            db.close();
            res.close();

            return new String();
        }

        res.moveToNext();
        result = new String(res.getString(res.getColumnIndex(LESSON_SUB_CONTENT)));
        db.close();
        res.close();

        return result;
    }
}

