package com.oakspro.customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OfflineDatabase extends SQLiteOpenHelper {


    public final static String DATABASE_NAME="tls_offline.db";
    public final static String TABLE_NAME="offline_registration";

    public static final String COL_1="ID";
    public static final String COL_2="NAME";
    public static final String COL_3="MOBILE";
    public static final String COL_4="ADDRESS";
    public static final String COL_5="CITY";
    public static final String COL_6="MOTHER";
    public static final String COL_7="EMAIL";
    public static final String COL_8="REFERENCE";
    public static final String COL_9="GENDER";
    public static final String COL_10="NATIONALID";
    public static final String COL_11="USER";
    public static final String COL_12="STATUS";


    public OfflineDatabase(Context context){
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, MOBILE TEXT, ADDRESS TEXT, CITY TEXT, MOTHER TEXT, EMAIL TEXT, REFERENCE TEXT, GENDER TEXT, NATIONALID TEXT, USER TEXT, STATUS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    //insert data into database
    public boolean insertData(String name, String mobile, String address, String city, String mother, String email, String reference, String gender, String nationalid, String user, String status){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL_2, name);
        cv.put(COL_3, mobile);
        cv.put(COL_4, address);
        cv.put(COL_5, city);
        cv.put(COL_6, mother);
        cv.put(COL_7, email);
        cv.put(COL_8, reference);
        cv.put(COL_9, gender);
        cv.put(COL_10, nationalid);
        cv.put(COL_11, user);
        cv.put(COL_12, status);

        long result=db.insert(TABLE_NAME,null, cv);
        if (result ==-1){
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor viewAll(){
        SQLiteDatabase db=this.getWritableDatabase();

        String st="0";
        String query="SELECT * FROM "+TABLE_NAME+ " WHERE STATUS='"+st+"'";
        Cursor cursor=db.rawQuery(query,null);
        return cursor;

    }

    public Integer deleteData(String mobile){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME, "MOBILE=?", new String[]{mobile});
    }


}
