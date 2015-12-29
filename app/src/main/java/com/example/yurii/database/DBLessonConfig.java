package com.example.yurii.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.yurii.speakeasy.StartUpMediator;

import java.util.ArrayList;
import java.util.List;


public class DBLessonConfig {
    public static String TAG = "DBLessonConfig";
    private DBLessonSignature lesonSignature_;
    private DBPageConfig      pageConfig_;
    protected int             lesson_;

    public DBLessonConfig(Context context, int lesson) {
        lesson_           = lesson;
        lesonSignature_ = new DBLessonSignature(context, this);
        pageConfig_     = new DBPageConfig(context, this);
    }

    public int getLesson() {
        return lesson_;
    }

    public String getSignature() {
        return lesonSignature_.getSignature();
    }

    public List<String> getLessonsList() {
        return lesonSignature_.getLessonsList();
    }

    public List<String> getKeyValuePageConfig() {
        return pageConfig_.getKeyValuePageConfig();
    }

    public static class DBLessonSignature extends SQLiteOpenHelper {
        public static final String DATABASE  = "content.db";
        public static final String SIGNATURE = "Signature";
        public static final String LESSON    = "Lesson";
        private DBLessonConfig lessonConfig_;

        public DBLessonSignature(Context context, DBLessonConfig lessonConfig) {
            super(context, DATABASE, null, 1);
            lessonConfig_ = lessonConfig;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

        public String getSignature() {
            String result;
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select * from lesson_signature where Lesson = "
                    + lessonConfig_.getLesson();
            Cursor res =  db.rawQuery(query, null);

            if ( res.getCount() ==  0 ) {
                db.close();
                res.close();
                return new String("");
            }
            res.moveToNext();
            result = new String(res.getString(res.getColumnIndex(SIGNATURE)));
            db.close();
            res.close();

            return result;
        }

        public List<String> getLessonsList() {
            List<String> result = new ArrayList<String>();
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select * from lesson_signature";
            Log.i(TAG, "--- " + query);
            Cursor res =  db.rawQuery(query, null);

            if ( res.getCount() ==  0 ) {
                db.close();
                res.close();
                return result;
            }

            for ( int i = 0; i < res.getCount(); i++ ) {
                res.moveToNext();
                if ( res.getInt(res.getColumnIndex(LESSON)) == 0 ) {
                    continue;
                }
                result.add(new String(res.getString(res.getColumnIndex(LESSON)))
                        + " " + new String(res.getString(res.getColumnIndex(SIGNATURE))));
            }

            db.close();
            res.close();

            return result;
        }
    }

    public static class DBPageConfig extends SQLiteOpenHelper {
        public static final String DATABASE   = "content.db";
        public static final String SECTION_ID = "SectionId";
        public static final String BLOCK_TYPE = "BlockType";
        private DBLessonConfig lessonConfig_;

        public DBPageConfig(Context context, DBLessonConfig lessonConfig) {
            super(context, DATABASE, null, 1);
            lessonConfig_ = lessonConfig;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

        public List<String> getKeyValuePageConfig() {
            List<String> resultList = new ArrayList<String>();
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select * from lesson_config where Lesson = " + lessonConfig_.getLesson();
            Cursor res =  db.rawQuery(query, null);

            for ( int i = 0; i < res.getCount(); i++ ) {
                res.moveToNext();
                String str = new String(res.getString(res.getColumnIndex(BLOCK_TYPE))) + ":"
                        + new String(res.getString(res.getColumnIndex(SECTION_ID)));
                resultList.add(str);
            }
            db.close();
            res.close();

            return resultList;
        }
    }

}
