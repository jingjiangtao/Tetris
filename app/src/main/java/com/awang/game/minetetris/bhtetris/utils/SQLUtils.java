package com.awang.game.minetetris.bhtetris.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jing on 17-10-29.
 */

public class SQLUtils extends SQLiteOpenHelper{

    public static final String CREATE_SCORE_TABLE = "create table scoreTable(" +
                                                    "id integer primary key autoincrement, " +
                                                    "score integer);";

    private Context context;

    public SQLUtils(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){

        super(context,name,factory,version);
        this.context = context;
    }



    //从数据库读最高分
    public int getMaxScore(){

        int maxScore=0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select score from scoreTable",null);
        if(cursor.moveToFirst()){
            int temp = 0;
            do{
                temp = cursor.getInt(cursor.getColumnIndex("score"));
                if(temp > maxScore){
                    maxScore = temp;
                }
            }while (cursor.moveToNext());
        }

        db.close();
        return maxScore;
    }

    //将分数写入数据库写入数据库
    public void putScore(int Score){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into scoreTable(score) values(?)",new Object[]{Score});
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_SCORE_TABLE);

    }

    //得到数据库中所有分数，返回一个cursor对象
    public Cursor getScore(){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("SELECT distinct score  FROM scoreTable  ORDER BY score DESC",null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
