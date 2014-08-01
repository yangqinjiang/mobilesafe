package com.itheima.mobilesafe.test;

import java.util.List;
import java.util.Random;

import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.db.domain.BlackNumberInfo;

import android.test.AndroidTestCase;

public class TestBlackNumberDB extends AndroidTestCase {

	public void testCreateDB() throws Exception{
		BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
		helper.getWritableDatabase();
	}
	public void testAddSomeNumber() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		long basenumber = 13500000000l;
		Random random = new Random();
		for(int i=0;i<100;i++){
			dao.add(String.valueOf(basenumber+i),String.valueOf(random.nextInt(3)+1));
		}
	}
	public void testAdd() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean result = dao.add("110","2");
		assertTrue(result);
	}
	public void testDelete() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean result = dao.delete("110");
		assertTrue(result);
	}
	public void testUpdate() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean result = dao.update("110","1");
		assertTrue(result);
	}
	public void testFind() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean result = dao.find("110");
		assertTrue(result);
	}
	public void testFindAll() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		List<BlackNumberInfo> infos = dao.findAll();
		for(BlackNumberInfo info:infos){
			System.out.println(info.toString());
		}
	}
}
