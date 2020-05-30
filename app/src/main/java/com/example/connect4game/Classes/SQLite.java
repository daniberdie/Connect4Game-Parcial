package com.example.connect4game.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {
    private final static String DataBaseName = "Connect4DB";
    private final static int VERSION = 1;
    private final static String sql = GameTable.CREATE + GameTable.TABLE_NAME + GameTable.HEADER +
            GameTable.USER + GameTable.TEXT_TYPE + GameTable.DATE + GameTable.TEXT_TYPE +
            GameTable.SIZE + GameTable.INT_TYPE + GameTable.TIME + GameTable.BOOL_TYPE +
            GameTable.FINAL_TIME + GameTable.TEXT_TYPE + GameTable.RESULT + GameTable.TEXT_TYPE + GameTable.DETAIL_RESULT + " TEXT)";

    public SQLite(Context context) {
        super(context, DataBaseName, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(GameTable.DROP + GameTable.TABLE_NAME);
        sqLiteDatabase.execSQL(sql);
    }

    public void addResult(String user, String date, int size, Boolean time_control, long final_time, String result, String detail_result){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GameTable.USER, user);
        contentValues.put(GameTable.DATE, date);
        contentValues.put(GameTable.SIZE, size);
        contentValues.put(GameTable.TIME, time_control);
        contentValues.put(GameTable.FINAL_TIME,final_time);
        contentValues.put(GameTable.RESULT, result);
        contentValues.put(GameTable.DETAIL_RESULT, detail_result);

        sqLiteDatabase.insert(GameTable.TABLE_NAME, null, contentValues);

    }

    public Cursor getResult(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + GameTable.TABLE_NAME;
        return sqLiteDatabase.rawQuery(query, null);
    }

    public class GameTable {
        public static final String USER = "alias";
        public static final String DATE = "date";
        public static final String SIZE = "size";
        public static final String TIME = "control";
        public static final String FINAL_TIME = "time";
        public static final String RESULT = "result";
        public static final String DETAIL_RESULT = "detail";
        private static final String TABLE_NAME = "GameHistorial";
        private static final String HEADER = "(_id INTEGER PRIMARY KEY AUTOINCREMENT, ";
        private static final String TEXT_TYPE = " TEXT NOT NULL, ";
        private static final String INT_TYPE = " INTEGER NOT NULL, ";
        private static final String BOOL_TYPE = " BOOLEAN, ";
        private static final String CREATE = "CREATE TABLE ";
        private static final String DROP = "DROP TABLE IF EXIST";
    }
}
