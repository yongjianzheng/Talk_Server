package com.yongjian.english_tranning_talk.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketAddress;

import com.yongjian.english_tranning_talk.bean.TranObject;
import com.yongjian.english_tranning_talk.bean.TranObjectType;

/*
 * 服务器端对客户端的监听
 */
public class ClientListenThread implements Runnable {
	
	private ClientActivity mClientActivity;
	private ObjectInputStream read;
	private boolean isRunning;

	public ClientListenThread(ClientActivity mClientActivity, ObjectInputStream read) {
		super();
		this.mClientActivity = mClientActivity;
		this.read = read;
		isRunning = true;
	}

	@Override
	public void run() {
		while(isRunning){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			readMsg();
		}
		
	}
	public void readMsg(){
		SocketAddress s=mClientActivity.getmClientSocket().getRemoteSocketAddress();
		TranObject object;
		try {
			object = (TranObject) read.readObject();
			TranObjectType type = object.getTranType();
			switch (type) {
			case REGISTER:         //    注册
				mClientActivity.register(object);
				break;
			case REGISTER_PHONE:   // 验证注册手机号是否可用
				String phone = (String) object.getObject();
				mClientActivity.checkPhone(phone);
				break;
			case LOGIN:        //  登录
				mClientActivity.login(object);
				break;
			case LOGOUT:      // 退出登录
				mClientActivity.close();
				break;
//			case SEARCH_ASSESS:   // 找特定老师的评价
//				mClientActivity.searchAss(object);
//				break;
			case SEARCH_TEACHER1:   //找符合条件的老师
				mClientActivity.searchTeachers(object,"口语四级");
				System.out.println("口语四级的");
				break;
			case SEARCH_TEACHER2:   //找符合条件的老师
				mClientActivity.searchTeachers(object,"口语六级");
				System.out.println("口语六级的");
				break;
			case SEARCH_TEACHER3:   //找符合条件的老师
				mClientActivity.searchTeachers(object,"教师水平");
				System.out.println("教师水平的");
				break;
			case SEARCH_TEACHER4:   //找符合条件的老师
				mClientActivity.searchTeachers(object,"教授水平");
				System.out.println("教授水平的");
				break;
			case SEND_REQUEST:   //    发送连接请求
				mClientActivity.requestToTeacher(object);
				System.out.println("收到学生连接教师请求");
				break;
			case SEND_LOGIN:       // 教师发送开始服务请求
				mClientActivity.sendLogin(object);
				break;
			case SEND_LOGINOUT:    //教师发送停止服务请求
				mClientActivity.sendLoginOut(object);
				break;
			case PRODECE_ORDER:    //教师确认订单
				mClientActivity.produceOrder(object);
				break;
			case PRODUCE_ASS:   //学生确认评价
				mClientActivity.produceAss(object);
				break;
			case QUERY_ORDER:  //查询订单
				mClientActivity.queryOrder(object);
				break;
			case QUERY_ASS:    //查询评价
				mClientActivity.queryAss(object);
				break;
			case FILE:       // 传输文件
				break;
			case REFRESH:    //刷新
				break;
			default:
				break;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close() {
		isRunning = false;
	}

}
