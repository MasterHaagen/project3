package com.nttu.csie.project3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by MasterHa on 2016/9/28.
 */

public class MyDB {
    //表格名稱
    public static final String TABLE_NAME = "history";
    public static final String TABLE_NAME_2 = "location";
    public static final String TABLE_NAME_3 = "fall";
    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";
    // 其它表格欄位名稱
    public static final String START_TIME = "start_time";
    public static final String ACTIVITY_TYPE = "activity_type";
    public static final String END_TIME = "end_time";
    public static final String STEP_COUNT = "step_count";
    public static final String LOCATION = "location";
    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " +
                    TABLE_NAME + "  ( " +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    START_TIME + " INTEGER NOT NULL, " +
                    ACTIVITY_TYPE + " INTEGER NOT NULL, " +
                    END_TIME + " INTEGER NOT NULL, " +
                    STEP_COUNT + " INTEGER NOT NULL)";

    public static final String CREATE_TABLE_2 =
            "CREATE TABLE " +
                    TABLE_NAME_2 + "  ( " +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    START_TIME + " INTEGER NOT NULL, " +
                    END_TIME + " INTEGER NOT NULL, " +
                    LOCATION + " INTEGER NOT NULL)";

    public static final String CREATE_TABLE_3 =
            "CREATE TABLE " +
                    TABLE_NAME_3 + "  ( " +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    START_TIME + " INTEGER NOT NULL, " +
                    LOCATION + " INTEGER NOT NULL)";

    //資料庫物件
    private SQLiteDatabase db;

    //建構子
    public MyDB(Context context) {
        db = MyDBHelper.getDatabase(context);
    }

    //關閉資料庫
    public void close() {
        //db.execSQL("DROP TABLE if exists history");
        db.close();
    }

    //新增參數指定的物件
    public Item insert(Item item) {
        //建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        //加入ContentValues物件包裝的新增資料
        //第一個參數是欄位名稱，第二個參數是欄位的資料
        cv.put(START_TIME, item.getStarttime());
        cv.put(ACTIVITY_TYPE, item.getActivitytype());
        cv.put(END_TIME, item.getEndtime());
        cv.put(STEP_COUNT, item.getSteps());

        //新增一筆資料並取得編號
        long id = db.insert(TABLE_NAME, null, cv);

        item.setId(id);
        return item;
    }

    //修改參數指定的物件
    public boolean update(Item item) {
        //建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        //加入ContentValues物件包裝的新增資料
        //第一個參數是欄位名稱，第二個參數是欄位的資料
        cv.put(START_TIME, item.getStarttime());
        cv.put(ACTIVITY_TYPE, item.getActivitytype());
        cv.put(END_TIME, item.getEndtime());
        cv.put(STEP_COUNT, item.getSteps());

        //設定修改資料的條件為編號
        //格式為欄位名稱=資料
        String where = KEY_ID + "=" + item.getId();

        //執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    //    //刪除參數指定編號的資料
//    public boolean delete(long id) {
//        //設定條件為編號，格式為欄位名稱=資料
//        String where = KEY_ID + "=" + id;
//        //刪除指定編號資料並回傳刪除是否成功
//        return db.delete(TABLE_NAME, where, null) > 0;
//    }
//
    // 讀取所有記事資料
//    public Cursor getAll() {
//
//        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
//
//        return cursor;
//
//    }

        // 取得指定編號的資料物件
    public Cursor get(long startTime) {
        long endTime = startTime + 2 * 3600 * 1000 - 1;
        // 準備回傳結果用的物件
        //Item item = null;
        // 使用編號為查詢條件
        //String where = KEY_ID + "=" + id;
        String where = " start_time>= " + startTime + " and start_time< " + endTime;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME,  new String[]{"sum(step_count) sum"}, where, null, null, null, null, null);
//        Cursor result = db.rawQuery("SELECT * FROM history WHERE " +
//                "start_time>=? and end_time<?"
//                , new String[] { String.valueOf(startTime), String.valueOf(endTime) });

        result.moveToFirst();
        // 如果有查詢結果
//        if (result.moveToFirst()) {
//            // 讀取包裝一筆資料的物件
//            item = getRecord(result);
//            }

        // 關閉Cursor物件
        //result.close();
        // 回傳結果
        return result;
    }

    // 把Cursor目前的資料包裝為物件
//    public Item getRecord(Cursor cursor) {
//        // 準備回傳結果用的物件
//        Item result = new Item();
//
//        result.setId(cursor.getInt(0));
//        result.setDatetime(cursor.getLong(1));
//        result.setSteps(cursor.getInt(2));
//
//        // 回傳結果
//        return result;
//    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    // 建立範例資料
    public void sample() {
        for(int i=0;i<12;i++) {
            Item item = new Item(0, new Date().getTime()+(i*2) * 3600 * 1000, 0, new Date().getTime() + (i*2) * 3600 * 1000, (int)(Math.random()*1000));
            insert(item);
        }
    }
}
