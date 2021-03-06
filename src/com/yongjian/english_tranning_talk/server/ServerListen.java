package com.yongjian.english_tranning_talk.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.yongjian.english_tranning_talk.client.ClientActivity;
import com.yongjian.english_tranning_talk.server.OnMap;



/**
 * 客户端使用 Scoket(ip,port);参数是服务器的ip和端口号，因为没有指定
 * 客户端套接字的准确ip和端口，所以服务器发往一台机器上的任一客户端的消息，其他 客户端都能收到。所以创建套接字时，最好指定套接字端口
 */
public class ServerListen {

	private final int PORT = 8399;
	private ServerSocket serverSocket;
	public static void main(String args[]){
		   new ServerListen().begin();
		}
	public void begin(){
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("服务器已经启动");
		} catch (IOException e) {
			System.out.println("服务器启动失败");
			e.printStackTrace();
		}
		while(true){
			try {
				Socket client= serverSocket.accept();
				new ClientActivity(this,client);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	  public ClientActivity getClientByID(int id){
	    	return OnMap.getInstance().getClientById(id);
	    }
	    public void closeClientByID(int id){
	    	OnMap.getInstance().removeClient(id);
	    }
	    public void addClient(int id, ClientActivity ca0){
	    	OnMap.getInstance().addClient(id, ca0);
	    }
	    public boolean contatinId(int id){
	    	return OnMap.getInstance().isContainId(id);
	    }
	    public void close(){
	    	try {
	    		serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		public int  size() {
			return OnMap.getInstance().size();
		}
	
}
