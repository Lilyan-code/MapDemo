package com.example.tangwenyan.map.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SearchHistorySQLiteHelper extends SQLiteOpenHelper {

    private Context context;
    public SearchHistorySQLiteHelper(Context context) {
        super(context, "search.db", null, 1);
    }

    //数据库创建的时候执行方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table history(_id integer primary key autoincrement,name varchar(1000) not null)";
          db.execSQL(sql);
//        Toast.makeText(context, "Created Succeeded", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
