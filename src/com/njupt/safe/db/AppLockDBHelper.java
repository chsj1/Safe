package com.njupt.safe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLockDBHelper extends SQLiteOpenHelper {

	private static SQLiteOpenHelper mInstance;
	private final static String name = "applock.db";
	
	public static synchronized SQLiteOpenHelper getInstance(Context context){
		if(mInstance == null){
			mInstance = new AppLockDBHelper(context, name, null, 1);
		}
		return mInstance;
	}
	
	private AppLockDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table applock(_id integer primary key autoincrement," +
        		"packageName text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
