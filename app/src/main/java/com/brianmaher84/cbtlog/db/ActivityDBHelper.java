package com.brianmaher84.cbtlog.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ActivityDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CBTLog.db";
    private static final int DATABASE_VERSION = 2;

    public static final String ACTIVITY_TABLE_NAME = "activity";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SITUATION = "situation";
    public static final String COLUMN_FEELINGS = "feelings";
    public static final String COLUMN_THOUGHTS = "thoughts";

    public ActivityDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + ACTIVITY_TABLE_NAME +
                        "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_SITUATION + " TEXT, " +
                        COLUMN_FEELINGS + " TEXT, " +
                        COLUMN_THOUGHTS + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nothing for moment
    }

    public boolean insertActivity(String situation, String feelings, String thoughts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SITUATION, situation);
        contentValues.put(COLUMN_FEELINGS, feelings);
        contentValues.put(COLUMN_THOUGHTS, thoughts);

        db.insert(ACTIVITY_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACTIVITY_TABLE_NAME);
        return numRows;
    }

    public boolean updateActivity(Integer id, String situation, String feelings, String thoughts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SITUATION, situation);
        contentValues.put(COLUMN_FEELINGS, feelings);
        contentValues.put(COLUMN_THOUGHTS, thoughts);
        db.update(ACTIVITY_TABLE_NAME, contentValues, COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteActivity(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ACTIVITY_TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public Cursor getActivity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + ACTIVITY_TABLE_NAME + " WHERE " +
                COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllActivities() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + ACTIVITY_TABLE_NAME, null );
        return res;
    }
}