package com.yongjian.english_tranning_talk.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.yongjian.english_tranning_talk.bean.Asses;
import com.yongjian.english_tranning_talk.bean.OrderList;
import com.yongjian.english_tranning_talk.bean.Result;
import com.yongjian.english_tranning_talk.bean.TranObject;
import com.yongjian.english_tranning_talk.bean.TranObjectType;
import com.yongjian.english_tranning_talk.bean.User;
import com.yongjian.english_tranning_talk.database.AssesDao;
import com.yongjian.english_tranning_talk.database.OrderDao;
import com.yongjian.english_tranning_talk.database.UserDao;
import com.yongjian.english_tranning_talk.server.ServerListen;
import com.yongjian.english_tranning_talk.server.TeacherList;

/*
 * 客户端线程
 */

public class ClientActivity {
	private LinkedList<TranObject> sendQueue;
	private User user;
	private Socket mClientSocket;  //客户端连接
	private ServerListen serverListen;   //服务器
	private ClientListenThread listenThread;  //监听 客户端进程
	private ClientSendThread sendThread; // 发送到客户端进程
	private ObjectOutputStream mOutput;
	private ObjectInputStream mInput;
	
	public ClientActivity(ServerListen serverListen,Socket mClientSocket) {

		this.mClientSocket = mClientSocket;
		this.serverListen = serverListen;
		user = new User();
		sendQueue = new LinkedList<>();
		try {
			mOutput = new ObjectOutputStream(mClientSocket.getOutputStream());
			mInput = new ObjectInputStream(mClientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		listenThread = new ClientListenThread(this, mInput);
		sendThread = new ClientSendThread(this);
		Thread  listen = new Thread(listenThread);
		Thread send = new Thread(sendThread);
		listen.start();
		send.start();		
	}
	public Socket getmClientSocket() {
		return mClientSocket;
	}

	public void setmClientSocket(Socket mClientSocket) {
		this.mClientSocket = mClientSocket;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user.setId(user.getId());
		this.user.setName(user.getName());
		this.user.setSex(user.getSex());
		this.user.setPhnum(user.getPhnum());
		this.user.setPwd(user.getPwd());
		this.user.setWallet(user.getWallet());
		this.user.setType(user.getType());
		if ("teacher".equals(user.getType())) {
			this.user.setGrade(user.getGrade());
			this.user.setHourypay(user.getHourypay());
		}
	}
	/*
	 * 验证手机号是否可用
	 */
	public void checkPhone(String phone){
		boolean isExited = UserDao.isRegister(phone);
		TranObject tranObject=new TranObject("", TranObjectType.REGISTER_PHONE);
		if (isExited) {
			tranObject.setResult(Result.PHONE_EXISTED);
		}else{
			tranObject.setResult(Result.PHONE_CAN_USE);
		}
		send(tranObject);
	}
	/*
	 * 注册
	 */
	public void register(TranObject tran){
		User user = (User) tran.getObject();
		System.out.println(user.getName());
		System.out.println(user.getPwd());
		int id = UserDao.insertInfo(user);
		//user.setId(id);
		if (id == -1)
			tran.setResult(Result.REGISTER_FAILED);
		else
			tran.setResult(Result.REGISTER_SUCCESS);
		System.out.println("发送注册结果...");
		send(tran);
	}
	/*
	 * 验证是否成功登陆
	 */
	public void login(TranObject tran) {
		User user = (User) tran.getObject();
		// 验证密码和用户名是否存在，若存在则为user对象赋值
		boolean isExisted = UserDao.login(user);
		System.out.println(user.getType());
		if (isExisted == true) {
			UserDao.updateIsOnline(user.getId(), 1);
			setUser(user);
			System.out.println(user.getName() + "上线了");
			tran.setResult(Result.LOGIN_SUCCESS);
			serverListen.addClient(user.getId(), this);
			ArrayList<OrderList> orderLists = OrderDao.selectMyOrder(user);
			user.setOrderlists(orderLists);
			if (user.getType().equals("teacher")) {
				ArrayList<Asses> asslist = AssesDao.selectAssById(user.getId());
				user.setAsseslist(asslist);
			}	
			tran.setObject(user);
		}else {
			tran.setResult(Result.LOGIN_FAILED);
		}
		send(tran);
		
	}
	/*
	 * 查找在线的某一等级教师
	 */
	public void searchTeachers(TranObject tran,String grade){
	//	User user = (User) tran.getObject();
		HashMap<Integer, User> list;
	//	String grade = user.getGrade();
		System.out.println("........等级为"+grade);
		if (grade.equals("口语四级")) {
			list = TeacherList.list1;
			System.out.println("查找四级老师");
			System.out.println(list.size());
		}else if (grade.equals("口语六级")) {
			list= TeacherList.list2;
			System.out.println("查找六级老师");
			System.out.println(list.size());
		}else if (grade.equals("教师水平")) {
			list = TeacherList.list3;
			System.out.println("查找教师老师");
			System.out.println(list.size());
		}else {
			list =TeacherList.list4;
			System.out.println("查找教授老师");
			System.out.println(list.size());
		}
		tran.setObject(list);
		send(tran);
	}
	/*
	 * 查找某一老师的评价
	 */
//	public void searchAss(TranObject tran){
//		User user = (User) tran.getObject();
//		ArrayList<Asses> list = AssesDao.selectAssById(user.getId());
//		tran.setObject(list);
//		send(tran);
//	}
	/*
	 * 教师请求上线服务
	 */
	public void sendLogin(TranObject tran){
		User user = (User) tran.getObject();
		String grade = user.getType();
		if (grade.equals("口语四级")) {
			TeacherList.addToList1(user.getId(),user);
			System.out.println("四级人数"+TeacherList.list1.size());
		}else if (grade.equals("口语六级")) {
			TeacherList.addToList2(user.getId(),user);
			System.out.println("六级人数"+TeacherList.list2.size());
		}else if (grade.equals("教师水平")) {
			TeacherList.addToList3(user.getId(),user);
			System.out.println("教师人数"+TeacherList.list3.size());
		}else {
			TeacherList.addToList4(user.getId(),user);
			System.out.println("教授人数"+TeacherList.list4.size());
		}
	}
	/*
	 * 教师请求停止服务
	 */
	public void sendLoginOut(TranObject tran){
		User user = (User) tran.getObject();
		String grade = user.getType();
		if (grade.equals("口语四级")) {
			TeacherList.removeList1(user.getId());
			System.out.println("四级人数"+TeacherList.list1.size());
		}else if (grade.equals("口语六级")) {
			TeacherList.removeList2(user.getId());
			System.out.println("六级人数"+TeacherList.list2.size());
		}else if (grade.equals("教师水平")) {
			TeacherList.removeList3(user.getId());
			System.out.println("教师人数"+TeacherList.list3.size());
		}else {
			TeacherList.removeList4(user.getId());
			System.out.println("教授人数"+TeacherList.list4.size());
		}
	}
	/*
	 * 请求连线教师
	 */
	public void requestToTeacher(TranObject tran){
		ClientActivity teacherClient = null;
		teacherClient = serverListen.getClientByID(tran.getReceiveId());
		System.out.println("开始向教师发送学生的请求");
		teacherClient.insertQueue(tran);
	}
	
	/*
	 * 确认订单
	 */
	public void produceOrder(TranObject tran) {
		OrderList order = (OrderList) tran.getObject();
		OrderDao.produceOrder(order);
	}
	/*
	 * 查询订单
	 */
	public void queryOrder(TranObject tran) {
		System.out.println("收到客户端的查询订单");
		User user = (User) tran.getObject();
		ArrayList<OrderList> list = OrderDao.selectMyOrder(user);
		System.out.println(list.size());
		tran.setObject(list);
		send(tran);
		System.out.println("向客户端发送结果");
	}
	/*
	 * 确认评价
	 */
	public void produceAss(TranObject tran){
		Asses asses  =  (Asses) tran.getObject();
		System.out.println(asses.getContent());
		System.out.println(asses.getT_name());
		System.out.println(asses.getAssgrade());
		AssesDao.produceAsses(asses);
	}
	/*
	 * 查询评价
	 */
	public void queryAss(TranObject tran) {
		System.out.println("收到客户端的查询评价");
		User user= (User) tran.getObject();
		ArrayList<Asses> list = AssesDao.selectAssById(user.getId());
		System.out.println("得到评价");
		System.out.println(list.size());
		tran.setObject(list);
		send(tran);
		System.out.println("向客户端发送结果");
	}

	public synchronized void send(TranObject tranObject){
		try {
			mOutput.writeObject(tranObject);
			mOutput.flush();
			notify();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 关闭与客户端的连接
	 */
	public void close(){
		try {
			mClientSocket.close();
			listenThread.close();
			sendThread.close();
			if (user.getId() != 0) {
				getOffLine();
			}
		} catch (IOException e) {
			System.out.println("关闭异常");
			e.printStackTrace();
		}
	}
	/*
	 * 客户端下线
	 */
	public void getOffLine() {
		serverListen.closeClientByID(user.getId());
		UserDao.updateIsOnline(user.getId(), 0);
	}
	
	public synchronized void insertQueue(TranObject tran) {
		sendQueue.add(tran);
	}

	public synchronized int sizeOfQueue() {
		return sendQueue.size();
	}

	public synchronized TranObject removeQueueEle(int i) {
		return sendQueue.remove(i);
	}

}
