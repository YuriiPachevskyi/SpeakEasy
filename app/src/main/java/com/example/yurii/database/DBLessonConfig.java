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

    public List<String> getKeyValueLessonConfig() {
        return pageConfig_.getKeyValueLessonConfig();
    }

    public List<String> getKeyValueSpeakingConfig() {
        return pageConfig_.getKeyValueSpeakingConfig();
    }

    public List<String> getKeyValueHomework() {
        return pageConfig_.getKeyValueHomework();
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
        public static final String DATABASE            = "content.db";
        public static final String LESSON_SECTION      = "LessonSection";
        public static final String LESSON_BLOCK_TYPE   = "LessonBlockType";
        public static final String SPEAKING_SECTION    = "SpeakingSection";
        public static final String SPEAKING_BLOCK_TYPE = "SpeakingBlockType";
        public static final String HOMEWORK_SECTION    = "HomeworkSection";
        public static final String HOMEWORK_BLOCK_TYPE = "HomeworkBlockType";
        private DBLessonConfig lessonConfig_;

        public DBPageConfig(Context context, DBLessonConfig lessonConfig) {
            super(context, DATABASE, null, 1);
            lessonConfig_ = lessonConfig;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

        public List<String> getKeyValueLessonConfig() {
            List<String> resultList = new ArrayList<String>();
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select * from lesson_config where Lesson = " + lessonConfig_.getLesson();
            Cursor res =  db.rawQuery(query, null);

            for ( int i = 0; i < res.getCount(); i++ ) {
                res.moveToNext();
                String str = new String(res.getString(res.getColumnIndex(LESSON_BLOCK_TYPE))) + ":"
                        + new String(res.getString(res.getColumnIndex(LESSON_SECTION)));
                resultList.add(str);
            }
            db.close();
            res.close();

            return resultList;
        }

        public List<String> getKeyValueSpeakingConfig() {
            List<String> resultList = new ArrayList<String>();
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select * from lesson_config where Lesson = " + lessonConfig_.getLesson()
                    + " and SpeakingSection not null";
            Cursor res =  db.rawQuery(query, null);

            for ( ; res.moveToNext() != false; ) {
                String str = new String(res.getString(res.getColumnIndex(SPEAKING_BLOCK_TYPE))) + ":"
                        + new String(res.getString(res.getColumnIndex(SPEAKING_SECTION)));
                resultList.add(str);
            }
            db.close();
            res.close();

            return resultList;
        }

        public List<String> getKeyValueHomework() {
            List<String> resultList = new ArrayList<String>();
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select * from lesson_config where Lesson = " + lessonConfig_.getLesson()
                    + " and HomeworkSection not null";
            Cursor res =  db.rawQuery(query, null);

            for ( ; res.moveToNext() != false; ) {
                String str = new String(res.getString(res.getColumnIndex(HOMEWORK_BLOCK_TYPE))) + ":"
                        + new String(res.getString(res.getColumnIndex(HOMEWORK_SECTION)));
                resultList.add(str);
            }
            db.close();
            res.close();

            return resultList;
        }
    }

}
