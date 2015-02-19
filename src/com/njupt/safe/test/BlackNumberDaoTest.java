package com.njupt.safe.test;

import java.util.List;

import com.njupt.safe.db.BlackNumberDao;

import android.test.AndroidTestCase;

public class BlackNumberDaoTest extends AndroidTestCase {
	public void testAdd(){
		BlackNumberDao dao = new BlackNumberDao(getContext());
		for(int i = 0 ; i < 10 ; ++i){
			dao.add("555" + i);
		}
		
	}
	
	
	public void testIsBlackNumber() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean result = dao.isBlackNumber("5558");
		System.out.println( " is black number " + result);
	}
	
	
	public void testDelete() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.delete("5558");
	}
	
	public void testUpdate() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.update(1, "8888");
	}
	
	
	public void testFindAll() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		List<String> list = dao.findAll();
		for(int i = 0;i< list.size();i++){
			System.out.println(list.get(i));
		}
	}
	
}
