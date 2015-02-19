package com.njupt.safe.engine;

import java.io.File;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class AddressQueryService {
	
	public boolean isExist(){//从SD卡中读取
		File file = new File(Environment.getExternalStorageDirectory(),"address.db");
		return file.exists();
	}
	
	
	public boolean isExistInSDCard(){//从SD卡中读取
		File file = new File(Environment.getExternalStorageDirectory(),"address.db");
		return file.exists();
	}
	
	public String query(String number){//从SDCard中的归属地数据库进行查询...
		String address = null;
		File file = new File(Environment.getExternalStorageDirectory(),"address.db");
		SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		
		if(db.isOpen()){
			String regularExpression = "^1[358]\\d{9}$"; 
			boolean isphone = number.matches(regularExpression);
		    
			if(isphone){
				String prefix_number = number.substring(0,7);
				Cursor c = db.query("info", new String[]{"city"}, " mobileprefix = ? ", new String[]{prefix_number}, null, null, null);
				
				if(c.moveToFirst()){
					address = c.getString(0);
				}
				c.close();
			}else{
				
				if(number.length() == 10){
					String area = number.substring(0,3);
					Cursor c = db.query("info", new String[]{"city"}, " area = ? ", new String[]{area}, null,null,null);
					
					if(c.moveToFirst()){
						address = c.getString(0);
					}
					c.close();
					
				}else if(number.length() == 11){
					String area1 = number.substring(0,3);
					Cursor c1 = db.query("info", new String[]{"city"}, " area = ? ", new String[]{area1},null,null,null);
					
					if(c1.moveToFirst()){
						address = c1.getString(0);
					}
					c1.close();
					
					String area2 = number.substring(0,4);
					Cursor c2 = db.query("info", new String[]{"city"},
							" area = ?", 
							new String[]{area2}, null, null, null);
					if(c2.moveToFirst()){
						address = c2.getString(0);
					}
					c2.close();
					
				}else if(number.length() == 12){
					//4位区号 + 8位电话号码
					String area = number.substring(0, 4);
					Cursor c = db.query("info", new String[]{"city"},
							" area = ?", 
							new String[]{area}, null, null, null);
					if(c.moveToFirst()){
						address = c.getString(0);
					}
					c.close();
				}else if(number.length() == 7 || number.length() == 8){
					address = "本地号码";
				}else if(number.length() == 4){
					address = "模拟器";
				}else if(number.length() == 3){
					address = "紧急号码";
				}
			}
			
			db.close();
		}
		
		
		if(address == null){
			address = "未知号码";
		}
		return address;
	}
}
