package com.yongjian.english_tranning_talk.server;

import java.util.HashMap;

import com.yongjian.english_tranning_talk.client.ClientActivity;
import com.yongjian.english_tranning_talk.server.OnMap;

/**
 * 用于保存与所有在线用户的socket连接
 */

public class OnMap {
	//静态成员只要虚拟机加载了类 ，这个成员就一直存在
		private HashMap<Integer,ClientActivity> clientMap ; 
		private static OnMap instance ;//此静态实例类加载完成后就一直存在
		
		public static OnMap getInstance(){
			if(instance == null)
				instance = new OnMap();
			return instance;
		}
		private OnMap(){
			clientMap = new HashMap<Integer,ClientActivity>();
		}
	    public synchronized  ClientActivity getClientById(int id){
	    	return clientMap.get(id);
	    }
	    public synchronized void addClient(int id,ClientActivity ca0){
	    	clientMap.put(id, ca0);
	    }
	    public synchronized void removeClient(int id){
	    	clientMap.remove(id);
	    }
	    public synchronized boolean isContainId(int id){
	      	return clientMap.containsKey(id);
	    }

		public int size() {
			return clientMap.size();
		}
}
