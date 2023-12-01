package com.exmple.funnyvideo.LiveVideo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "demo1", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String qry="create table favdata(id INTEGER PRIMARY KEY AUTOINCREMENT,imageId TEXT,title TEXT,category TEXT,views TEXT,likes TEXT)";
        sqLiteDatabase.execSQL(qry);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public Cursor insertData(String imageId, String title, String category, Integer views, Integer likes) {
        Log.e( "datainsert","inrted" );

        SQLiteDatabase db=getWritableDatabase();

        String qry="insert into favdata(imageId,title,category,views,likes) values ('"+imageId+"','"+title+"','"+category+"','"+views+"','"+likes+"')";
        db.execSQL(qry);
            return null;
    }

    public Cursor readdata(){

        SQLiteDatabase db=getReadableDatabase();
        String qry="select * from favdata";
        Cursor cursor=db.rawQuery(qry,null);
        return cursor;
    }

    public void deleteData(String id) {
        SQLiteDatabase db=getWritableDatabase();

        String qry="delete from favdata where imageId='"+id+"'";
        db.execSQL(qry);
    }

//    public Integer checkDataContain(String imageId){
//
//        SQLiteDatabase db=getReadableDatabase();
//
////        String qry="select * from watchletter where imageId='"+imageId+"'";
////        Cursor cursor=db.rawQuery(qry,null);
//
////        Cursor cursor = db.rawQuery("SELECT * FROM watchletter WHERE imageId="+imageId, null);
////        Cursor cursor = db.rawQuery("select * from watchletter where imageId =?", new String[]{imageId});
//
//        Cursor cursor = db.rawQuery("SELECT * FROM watchletter WHERE imageId=" +imageId, null);
//
//        return cursor.getPosition();
//    }




}
