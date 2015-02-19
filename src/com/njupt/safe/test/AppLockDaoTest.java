package com.njupt.safe.test;

import java.util.List;

import com.njupt.safe.db.AppLockDao;

import android.test.AndroidTestCase;

public class AppLockDaoTest extends AndroidTestCase {
	
	public void testAdd() throws Exception{
		AppLockDao dao = new AppLockDao(getContext());
		dao.add("com.android.service");
		dao.add("com.android.alarmclock");
	}
	
	public void testDelete() throws Exception{
		AppLockDao dao = new AppLockDao(getContext());
		dao.delete("com.android.service");
	}
	
	public void testIsLockApp() throws Exception{
		AppLockDao dao = new AppLockDao(getContext());
		Boolean isLockApp = dao.isLockApp("com.android.service");
		
		System.out.println(" isLockApp "  + isLockApp);
	}
	
	public void testFindAll() throws Exception{
		AppLockDao dao = new AppLockDao(getContext());
		List<String> appLocks = dao.findAll();
		for(String packageName: appLocks){
			
			System.out.println( " packageName "  + packageName);
		}
		
	}
	
	
}
