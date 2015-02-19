package com.njupt.safe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBHelper extends SQLiteOpenHelper {

	private static SQLiteOpenHelper mInstance;
	private final static String name = "blacknumber.db";
	
	public static SQLiteOpenHelper getInstance(Context context){
		if(mInstance == null){
			mInstance = new BlackNumberDBHelper(context,name,null,1);
		}
		
		return mInstance;
	}
	
	public BlackNumberDBHelper(Context context,String name , CursorFactory factory , int version) {
		super(context, name, factory, version);
		
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blacknumber(_id integer primary key autoincrement , number text)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
