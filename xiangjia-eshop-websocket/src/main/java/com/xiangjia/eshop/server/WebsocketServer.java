package com.xiangjia.eshop.server;

import com.xiangjia.eshop.controller.AjaxResult;
import com.xiangjia.eshop.serial.SerialReader;
import com.xiangjia.eshop.util.EbamUtils;
import com.xiangjia.eshop.util.StringUtils;
import com.xiangjia.eshop.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author javaboot
 */
@Slf4j
@ServerEndpoint(value = "/websocket")
@Component
public class WebsocketServer {

    /**
     * 线程安全的socket集合
     */
    private static CopyOnWriteArraySet<Session> webSocketSet = new CopyOnWriteArraySet<>();
    /**
     * 初始在线人数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        webSocketSet.add(session);
        int count = onlineCount.incrementAndGet();
        System.out.println("[Socket] 有链接加入，当前在线人数为: {}" + count);
        try {
            WebSocketUtil.sendOnlineMsg(Integer.toString(count), webSocketSet);
            WebSocketUtil.sendNotificationMsg( StringUtils.listToString(SerialReader.getAvailableSerialPortsStr()), webSocketSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        int count = onlineCount.decrementAndGet();
        System.out.println("[Socket] 有链接关闭,当前在线人数为: {}" + count);
//        WebSocketUtil.sendOnlineMsg(Integer.toString(count)+"@"+ StringUtils.listToString(SerialReader.getAvailableSerialPortsStr()), webSocketSet);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("[Socket] {}来自客户端的消息:{}" + session.getId()+ message);
        if(StringUtils.isEmpty(message)){
            return ;
        }
        switch (message.split("@")[0]){
            case "CON":
                SerialReader.start(message.split("@")[1],webSocketSet);
                break;
            case "GET":
                WebSocketUtil.sendOnlineMsg(EbamUtils.GOODWEIGHT, webSocketSet);
                break;
            default:
                break;
        }
    }

    /**
     * 获取在线用户数量
     *
     * @return
     */
    public int getOnlineUserCount() {
        return onlineCount.get();
    }

    /**
     * 获取在线用户的会话信息
     *
     * @return
     */
    public CopyOnWriteArraySet<Session> getOnlineUsers() {
        return webSocketSet;
    }
}
