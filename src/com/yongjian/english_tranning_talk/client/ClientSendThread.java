package com.yongjian.english_tranning_talk.client;

import java.util.concurrent.TimeUnit;

import com.yongjian.english_tranning_talk.bean.TranObject;

public class ClientSendThread implements  Runnable{

	private ClientActivity mClient;
	private boolean isRunning;
	
	public ClientSendThread(ClientActivity mClient) {
		this.mClient = mClient;
		this.isRunning = true;
	}

	@Override
	public void run() {
		while(isRunning){
			if (mClient.sizeOfQueue()==0) {
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				TranObject tranObject=mClient.removeQueueEle(0);
				mClient.send(tranObject);
			}
		}
		
	}
	public void close(){
		isRunning = false;
	}

	
	
}
