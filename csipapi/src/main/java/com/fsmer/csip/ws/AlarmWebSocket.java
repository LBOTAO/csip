package com.fsmer.csip.ws;

import com.fsmer.csip.controller.LoginController;
import com.fsmer.csip.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: csipapi
 * @description:
 * @author: Tracy
 * @create: 2021-03-15 16:10
 */
@ServerEndpoint("/ws/alarm/{accessToken}")
@Component
public class AlarmWebSocket {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static int onlineCount = 0;
    private static ConcurrentHashMap<String, AlarmWebSocket> webSocketMap = new ConcurrentHashMap<>();
    private Session session;
    private String accessToken = "";
    private String userId;
    @OnOpen
    public void onOpen(Session session, @PathParam("accessToken") String accessToken) {
        this.session = session;
        this.accessToken = accessToken;
        userId = JwtUtil.verify(accessToken);
        if (userId == null) {
            log.info("accessToken：" + accessToken + " 无效");
            this.onClose();
        }
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
        } else {
            webSocketMap.put(userId, this);
            addOnlineCount();
        }
        log.debug("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:" + userId + ",网络异常!!!!!!");
        }
    }

    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            subOnlineCount();
        }
        log.info("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:" + userId + ",报文:" + message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.accessToken + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    // 发送到某个客户端
    public static void sendMessage(String message, String userId) throws IOException {
        log.info("发送消息到:" + userId + "，报文:" + message);
        if (StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        } else {
            log.error("用户" + userId + ",不在线！");
        }
    }

    /**
     * 群发消息
     * @param message
     * @throws IOException
     */
    public static void sendMesage(String message) {
        for (Map.Entry<String, AlarmWebSocket> item : webSocketMap.entrySet()) {
            try {
                item.getValue().sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        AlarmWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        AlarmWebSocket.onlineCount--;
    }

}
