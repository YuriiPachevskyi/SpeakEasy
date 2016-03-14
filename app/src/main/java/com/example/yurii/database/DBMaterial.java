package com.example.yurii.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class DBMaterial {
    public static String TAG = "DBMaterial";
    private DBContent contentDB_;
    private DBTag     tagDB_;
    protected int     lesson_;

    public DBMaterial(Context context, int lesson) {
        contentDB_ = new DBContent(context, this);
        tagDB_     = new DBTag(context, this);
        lesson_    = lesson;
    }

    public int getTagId(String tag) {
        return  tagDB_.getTagId(tag);
    }

    public int getLesson() {
        return lesson_;
    }

    public int getPriorityLevel(String tag) {
        return tagDB_.getPriorityLevel(tag);
    }

    public String getInstructionByTag(String tag) {
        return contentDB_.getInstructionByTag(getTagId(tag));
    }

    public String getInstructionContentByTag(String tag) {
        return contentDB_.getInstructionContentByTag(getTagId(tag));
    }

    public List<String> getContentList(String tag) {
        return contentDB_.getContentListByTag(getTagId(tag));
    }

    public List<String> getTagsList() {
        return tagDB_.getTagsListByLesson(lesson_);
    }

    public List<String> getFullTagsList() {
        return tagDB_.getFullTagsList();
    }

    public static class DBTag extends SQLiteOpenHelper {
        public static final String DATABASE       = "content.db";
        public static final String ELEMENT_ID     = "Id";
        public static final String LESSON         = "Lesson";
        public static final String PRIORITY_LEVEL = "PriorityLevel";
        public static final String CONTENT        = "Content";

        private DBMaterial material_;

        public DBTag(Context context, DBMaterial material) {
            super(context, DATABASE, null, 1);
            material_ = material;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

        public boolean isTagExist(String tag) {
            boolean result = false;
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select * from tag where Content = \"" + tag + "\"";
            Cursor res =  db.rawQuery(query, null);

            if ( res.getCount() > 0 ) {
                result = true;
            }
            db.close();
            res.close();
            return result;
        }

        public Cursor getNextTagCursor(String tag) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select * from tag where Content = \"" + tag + "\"";
            Cursor res =  db.rawQuery(query, null);
            res.moveToNext();
            db.close();
            return res;
        }

        public int getTagId(String tag) {
            int result = 0;
            if ( isTagExist(tag) ) {
                Cursor nextRes = getNextTagCursor(tag);
                result = nextRes.getInt(nextRes.getColumnIndex(ELEMENT_ID));
                nextRes.close();
            }
            return result;
        }

        public int getLesson(String tag) {
            int result = 0;
            if ( isTagExist(tag) ) {
                Cursor nextRes = getNextTagCursor(tag);
                result = nextRes.getInt(nextRes.getColumnIndex(LESSON));
                nextRes.close();
            }
            return result;
        }

        public int getPriorityLevel(String tag) {
            int result = 4;
            if ( isTagExist(tag) ) {
                Cursor nextRes = getNextTagCursor(tag);
                result = nextRes.getInt(nextRes.getColumnIndex(PRIORITY_LEVEL));
                nextRes.close();
            }
            return result;
        }

        public List<String> getTagsListByLesson(int lesson) {
            List<String> result = new ArrayList<String>();
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select Content from tag where Lesson = " + lesson;
            Cursor res =  db.rawQuery(query, null);

            for ( int i = 0; i < res.getCount(); i++ ) {
                res.moveToNext();
                result.add(new String(res.getString(res.getColumnIndex(CONTENT))));
            }
            db.close();
            res.close();

            return result;
        }

        public List<String> getFullTagsList() {
            List<String> result = new ArrayList<String>();

            for ( int i = 3; i > 0; i-- ) {
                SQLiteDatabase db = this.getReadableDatabase();
                String query = "select Content from tag where PriorityLevel = " + i;
                Cursor res =  db.rawQuery(query, null);

                for ( int j = 0; j < res.getCount(); j++ ) {
                    res.moveToNext();
                    result.add(new String(res.getString(res.getColumnIndex(CONTENT))));
                }
                db.close();
                res.close();
            }

            return result;
        }
    }

    public static class DBContent extends SQLiteOpenHelper {
        public static final String DATABASE            = "content.db";
        public static final String CONTENT             = "Content";
        public static final String INSTRUCTION         = "Instruction";
        public static final String INSTRUCTION_CONTENT = "InstructionContent";
        private DBMaterial material_;

        public DBContent(Context context, DBMaterial material) {
            super(context, DATABASE, null, 1);
            material_ = material;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

        public List<String> getContentListByTag(int tagId) {
            List<String> result = new ArrayList<String>();
            SQLiteDatabase db   = this.getReadableDatabase();
            String query        = "select Content from tag_content where TagId = " + tagId;
            Cursor res          =  db.rawQuery(query, null);

            for ( int i = 0; i < res.getCount(); i++ ) {
                res.moveToNext();
                result.add(new String(res.getString(res.getColumnIndex(CONTENT))));
            }
            db.close();
            res.close();

            return result;
        }

        public String getInstructionByTag(int tagId) {
            String result     = new String();
            SQLiteDatabase db = this.getReadableDatabase();
            String query      = "select Instruction from tag_content where Instruction is not null" +
                    " and TagId = " + tagId;
            Cursor res        =  db.rawQuery(query, null);

            if ( res.getCount() == 0 ) {
                db.close();
                res.close();

                return result;
            }

            res.moveToNext();
            result = new String(res.getString(res.getColumnIndex(INSTRUCTION)));
            db.close();
            res.close();

            return result;
        }

        public String getInstructionContentByTag(int tagId) {
            String result     = new String();
            SQLiteDatabase db = this.getReadableDatabase();
            String query      = "select InstructionContent from tag_content where " +
                    "InstructionContent is not null and TagId = " + tagId;
            Cursor res        =  db.rawQuery(query, null);

            if ( res.getCount() == 0 ) {
                db.close();
                res.close();

                return result;
            }
            res.moveToNext();
            result = new String(res.getString(res.getColumnIndex(INSTRUCTION_CONTENT)));
            db.close();
            res.close();

            return result;
        }
    }
}
