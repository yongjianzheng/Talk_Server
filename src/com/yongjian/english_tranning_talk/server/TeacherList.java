package com.yongjian.english_tranning_talk.server;

import java.util.HashMap;

import com.yongjian.english_tranning_talk.bean.User;

public class TeacherList {
	
	public static HashMap<Integer, User>  list1= new HashMap<>();
	public static HashMap<Integer, User>  list2= new HashMap<>();
	public static HashMap<Integer, User>  list3= new HashMap<>();
	public static HashMap<Integer, User>  list4= new HashMap<>();
	public static TeacherList instance;
	public static TeacherList getInstance(){
		if (instance == null) {
			instance = new TeacherList();
		}
		return instance;
	}
	private TeacherList(){
	}
	public synchronized static void addToList1(Integer id,User user) {
		list1.put(id, user);
	}
	public synchronized static void addToList2(Integer id,User user) {
		list2.put(id, user);
	}
	public synchronized static void addToList3(Integer id,User user) {
		list3.put(id, user);
	}
	public synchronized static void addToList4(Integer id,User user) {
		list4.put(id, user);
	}
	public synchronized static void removeList1(Integer id){
		list1.remove(id);
	}
	public synchronized static void removeList2(Integer id){
		list2.remove(id);
	}
	public synchronized static void removeList3(Integer id){
		list3.remove(id);
	}
	public synchronized static void removeList4(Integer id){
		list4.remove(id);
	}

}
