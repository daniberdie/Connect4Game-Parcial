package com.example.connect4game.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.connect4game.R;

import java.io.ByteArrayOutputStream;

public class SQLite extends SQLiteOpenHelper {
    //Versió 2 exàmen android
    private Context context;
    //----------------------------
    private final static String DataBaseName = "Connect4DB";
    private final static int VERSION = 1;
    private final static String sql = GameTable.CREATE + GameTable.TABLE_NAME + GameTable.HEADER +
            GameTable.USER + GameTable.TEXT_TYPE + GameTable.DATE + GameTable.TEXT_TYPE +
            GameTable.SIZE + GameTable.INT_TYPE + GameTable.TIME + GameTable.BOOL_TYPE +
            GameTable.FINAL_TIME + GameTable.TEXT_TYPE + GameTable.RESULT + GameTable.TEXT_TYPE + GameTable.DETAIL_RESULT + " TEXT)";

    //Versió 2 exàmen android
    private final static String sqlCreate2 = GameTable.CREATE + GameTable.TABLE_NAME_EXAM + GameTable.HEADER +
            GameTable.USER + GameTable.TEXT_TYPE + GameTable.DATE + GameTable.TEXT_TYPE +
            GameTable.SIZE + GameTable.INT_TYPE + GameTable.TIME + GameTable.BOOL_TYPE +
            GameTable.FINAL_TIME + GameTable.TEXT_TYPE + GameTable.RESULT + " BLOB)";

    public SQLite(Context context) {
        super(context, DataBaseName, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql);
        //Versió 2 exàmen android
        //sqLiteDatabase.execSQL(sqlCreate2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(GameTable.DROP + GameTable.TABLE_NAME);
        //Versió 2 exàmen android
        //sqLiteDatabase.execSQL(GameTable.DROP + GameTable.TABLE_NAME_EXAM);
        onCreate(sqLiteDatabase);
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

    //Versió 2 exàmen android insertar dades
    public void addResultToExamTable(String user, String date, int size, Boolean time_control, long final_time, String result){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GameTable.USER, user);
        contentValues.put(GameTable.DATE, date);
        contentValues.put(GameTable.SIZE, size);
        contentValues.put(GameTable.TIME, time_control);
        contentValues.put(GameTable.FINAL_TIME,final_time);

        int drawable;

        if(result.equals("Heu guanyat!!")){
            drawable = R.drawable.victoria;
        }else if (result.equals("Heu perdut.")){
            drawable =  R.drawable.derrota;
        }else if (result.equals("Heu empatat, Temps superat.")){
            drawable =  R.drawable.tiempoagotado;
        }else{
            drawable = R.drawable.empate;
        }

        byte[] blob = getBlob(drawable);

        contentValues.put(GameTable.RESULT, blob);

        sqLiteDatabase.insert(GameTable.TABLE_NAME_EXAM, null, contentValues);

    }

    //Versió 2 exàmen android insertar dades
    private byte[] getBlob(int drawable) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                drawable);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public Cursor getResult(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + GameTable.TABLE_NAME;
        return sqLiteDatabase.rawQuery(query, null);
    }

    //Versió 2 exàmen android
    public Cursor getResultForVersionTwo(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + GameTable.TABLE_NAME_EXAM;
        return sqLiteDatabase.rawQuery(query, null);
    }

    //Versió 2 exàmen android
    public void migrateDataFromFirstTable(int id, String alias, String date, int size, boolean control, String time, byte[] result_blob){
        String sql_sentence = "INSERT INTO Partidas2 (_id, alias, date, size, control, time, result) " + "VALUES (" + id + ", " +  alias + ", " + date + ", " + size + ", " + control + ", " +  time + ", " +  result_blob + ")";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(sql_sentence);
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
        private static final String TABLE_NAME_EXAM = "Partidas2";
        private static final String HEADER = "(_id INTEGER PRIMARY KEY AUTOINCREMENT, ";
        private static final String TEXT_TYPE = " TEXT NOT NULL, ";
        private static final String INT_TYPE = " INTEGER NOT NULL, ";
        private static final String BOOL_TYPE = " BOOLEAN, ";
        private static final String CREATE = "CREATE TABLE ";
        private static final String DROP = "DROP TABLE IF EXIST";
    }
}
