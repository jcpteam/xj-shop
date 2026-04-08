package com.xiangjia.eshop.serial;

import com.xiangjia.eshop.server.WebsocketServer;
import com.xiangjia.eshop.util.EbamUtils;
import com.xiangjia.eshop.util.WebSocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
class CommDataObserver implements Observer {
	private String name;
	private CopyOnWriteArraySet<Session> webSocketSet;
	 public CommDataObserver() {}
	 public CommDataObserver(String name,CopyOnWriteArraySet<Session> webSocketSet) {
	    this.name = name;
	    this.webSocketSet = webSocketSet;
	 }
	
	public void update(Observable o, Object arg) {
	   String weight = new String((byte[]) arg).trim();
	   StringBuilder sb=new StringBuilder(weight);//0.000或者00.000
	   sb.insert(weight.length()-3,".");
	   System.out.println(sb.toString()+"kg");
		weight = sb.toString().replace(". kg","").replace("kg","");
		weight=weight.split("\r\n")[0].split(", ")[1].trim();
	   EbamUtils.GOODWEIGHT = weight;//将重量设置进全局变量
		try {
			WebSocketUtil.sendNotificationMsg(weight, this.webSocketSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	 
}
