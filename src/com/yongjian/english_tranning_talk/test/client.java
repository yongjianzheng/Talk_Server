package com.yongjian.english_tranning_talk.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.yongjian.english_tranning_talk.bean.TranObject;
import com.yongjian.english_tranning_talk.bean.TranObjectType;
import com.yongjian.english_tranning_talk.bean.User;

public class client {
	
	public static void main(String []args){
		try {
			Socket s =new Socket("127.0.0.1",8399);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			TranObject tran = new TranObject();
			User user = new User();
			user.setPhnum("15521396280");
			user.setPwd("11111");
			tran.setObject(user);
			tran.setTranType(TranObjectType.LOGIN);
			out.writeObject(tran);
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			while(true){}//保持程序不退出
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
