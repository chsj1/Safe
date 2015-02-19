package com.njupt.safe.engine;

import android.content.Context;
import android.content.SharedPreferences;

public class SavePreference {
	
	public static void save(Context context ,String key , Object value){
		SharedPreferences sp = context.getSharedPreferences(Const.PFNAME, 0);
		if(value instanceof String){
			sp.edit().putString(key, (String)value).commit();
		}else if(value instanceof Boolean){
			sp.edit().putBoolean(key, (Boolean) value).commit();
		}
	}
	
	
	public static String getStr(Context context ,String key){
		SharedPreferences sp = context.getSharedPreferences(Const.PFNAME, 0);
		return sp.getString(key, "");
	}
	
	public static Boolean getBoolean(Context context , String key){
		SharedPreferences sp = context.getSharedPreferences(Const.PFNAME, 0);
		return sp.getBoolean(key, false);
	}
}
