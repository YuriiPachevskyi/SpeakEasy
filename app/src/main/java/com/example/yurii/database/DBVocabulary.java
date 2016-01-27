package com.example.yurii.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBVocabulary extends SQLiteOpenHelper {
    public static final String TAG        = "DBhelper";
    public static final String DATABASE   = "content.db";
    public static final String TABLE_NAME = "vocabulary";
    public static final String ELEMENT_ID = "Id";
    public static final String LESSON_ID  = "LessonId";
    public static final String WORLD      = "Word";
    public static final String TRANSL_RUS = "TranslateRus";
    public static final String TRANSL_UKR = "TranslateUkr";

    public DBVocabulary(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public List<String> getVocabularyByLesson(int lesson) {
        List<String> result = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from vocabulary where " + LESSON_ID + " = " + lesson;
        Cursor res =  db.rawQuery(query, null);

        for ( int i = 0; i < res.getCount(); i++ ) {
            res.moveToNext();
            String element = new String(res.getString(res.getColumnIndex(WORLD))
                    + " - " + res.getString(res.getColumnIndex(TRANSL_RUS)));
            result.add(element);
        }
        db.close();
        res.close();

        return result;
    }

    public List<String> getVocabularyByLessonWithoutTranslate(int lesson) {
        List<String> result = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from vocabulary where " + LESSON_ID + " = " + lesson;
        Cursor res =  db.rawQuery(query, null);

        for ( int i = 0; i < res.getCount(); i++ ) {
            res.moveToNext();
            String element = new String(res.getString(res.getColumnIndex(WORLD)));
            result.add(element);
        }
        db.close();
        res.close();

        return result;
    }
}
