package example.android.mydbapp;

/**
 * Created by とし on 2016/10/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DaoItem {
    public static String TABLE_NAME = "item";
    public static String COLUMN_ID = "_id";
    public static String COLUMN_NAME = "name";
    public static String COLUMN_PRICE = "price";

    public static String create(){
        return "create table " + TABLE_NAME + "(" +
                COLUMN_ID + " integer primary key autoincrement not null, " +
                COLUMN_NAME + "text not null, " +
                COLUMN_PRICE + " integer not null" +
                ");";
    }

    public static DtoItem findById(Context context, long id){
        SQLiteDatabase db = getReadableDB(context);

        DtoItem item = new DtoItem();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME +
        " where _id = ?", new String[] {String.valueOf(id)});
        if(cursor.moveToFirst()){
            item.id = cursor.getLong(0);
            item.name = cursor.getString(1);
            item.price = cursor.getInt(2);
        }
        cursor.close();
        return item;
    }

    public static List<DtoItem> findAll(Context context){
        SQLiteDatabase db = getReadableDB(context);

        List<DtoItem> listItem = new ArrayList<DtoItem>();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME +
        " order by " + COLUMN_ID, null);
        if(cursor.moveToFirst()){
            do{
                DtoItem item = new DtoItem();
                item.id = cursor.getLong(0);
                item.name = cursor.getString(1);
                item.price = cursor.getInt(2);
                listItem.add(item);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return listItem;
    }

    public static long insert(Context context, DtoItem item){
        SQLiteDatabase db = getWritableDB(context);

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.name);
        values.put(COLUMN_PRICE, item.price);
        return db.insert(TABLE_NAME, null, values);
    }

    public static long update(Context context, DtoItem item){
        SQLiteDatabase db = getWritableDB(context);
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.name);
        values.put(COLUMN_PRICE, item.price);
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[] {String.valueOf(item.id)});
    }

    public static long delete(Context context, long id){
        SQLiteDatabase db = getWritableDB(context);

        return db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] {String.valueOf(id)});
    }

    //読み取り専用
    private static SQLiteDatabase getReadableDB(Context context){
        DbOpenHelper helper = new DbOpenHelper(context);
        return helper.getReadableDatabase();
    }

    //書きこみ専用
    private static SQLiteDatabase getWritableDB(Context context){
        DbOpenHelper helper = new DbOpenHelper(context);
        return helper.getWritableDatabase();
    }
}
