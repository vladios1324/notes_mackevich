package com.example.notes_mackevich6.datas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbContext extends SQLiteOpenHelper {
    public static SQLiteDatabase sqLiteDatabase;
    public DbContext(Context context) {
        super(context, "DbNotes", null, 1);
        sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Notes (" +
                "Id integer primary key autoincrement," +
                "Title text," +
                "Text text," +
                "Date text," +
                "Color text," +
                "IsFavorite integer default 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
