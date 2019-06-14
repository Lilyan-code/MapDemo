package com.example.tangwenyan.map.DataBase;

        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.util.Log;

        import com.amap.api.services.help.Tip;
        import com.example.tangwenyan.map.DataBase.Util.History;

        import java.util.ArrayList;
        import java.util.LinkedList;
        import java.util.List;

public class HistroyDao {
    final static String TAG = "HistoryDao";
    private SQLiteDatabase db;
    public HistroyDao(Context context) {
        SearchHistorySQLiteHelper searchHistorySQLiteHelper = new SearchHistorySQLiteHelper(context);
        db = searchHistorySQLiteHelper.getReadableDatabase();
    }

    public void insert(History tmp) {
        Log.d(TAG, "insert " + tmp.getName());
         String sql = "insert into history(name) values('" + tmp.getName() + "')";
        db.execSQL(sql);
        Log.d(TAG, "insert: " + "插入成功");
        //db.close();
    }

    public void delete() {
        String sql = "delete from history";
        db.execSQL(sql);
        //db.close();
    }

    public LinkedList<String> query() {
        LinkedList<String> tmp = new LinkedList<String>();
        String sql = "select _id,name from history order by _id desc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            tmp.add(cursor.getString(1));
        }
        return tmp;
    }

    public void delete_sub(String tmpName) {
        String sql = "delete from history where name = 'tmpName'";
        db.execSQL(sql);
    }

}
