package example.android.mydbapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
/**
 * Created by とし on 2016/10/14.
 */

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb.db";

    private static final int DB_VERSION = 1;

    public DbOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DaoItem.create());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //データベースに変更が生じたらここに処理を記述する。
    }
}

