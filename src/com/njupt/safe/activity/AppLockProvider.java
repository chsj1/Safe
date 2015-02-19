package com.njupt.safe.activity;

import com.njupt.safe.db.AppLockDBHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class AppLockProvider extends ContentProvider {

	private SQLiteOpenHelper mOpenHelper;
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private final static String authority = "applock";
	private final static int APPLOCK = 10;
	static{
		matcher.addURI(authority, "applock", APPLOCK);
	}
	
	@Override
	public boolean onCreate() {
		mOpenHelper = AppLockDBHelper.getInstance(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int code = matcher.match(uri);
		switch (code) {
		case APPLOCK:
			db.insert("applock", "_id", values);
			getContext().getContentResolver().notifyChange(uri, null);
			break;

		default:
			break;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int code = matcher.match(uri);
		switch (code) {
		case APPLOCK:
			db.delete("applock", selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			break;

		default:
			break;
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}


}
