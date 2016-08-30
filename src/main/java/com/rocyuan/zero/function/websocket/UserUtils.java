package com.rocyuan.zero.function.websocket;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rocyuan on 15/12/11.
 */
public abstract class UserUtils implements UserListener{

    private static final Logger LOG = LoggerFactory.getLogger(UserUtils.class);
    public static final String USER_KEY = "userName";
    private static Map<String,WebSocketSession> users;

    public UserUtils() {
        users = Maps.newConcurrentMap();
    }

    @Override
    public void addUser(WebSocketSession session) {
        LOG.info("New user: {}",session.getAttributes().get(USER_KEY));
        String userName = (String) session.getAttributes().get(USER_KEY);
        if (StringUtils.isNotBlank(userName)) {
            users.put(userName,session);
        }
    }

    /**
     * 连接退出了
     * @param session
     */
    @Override
    public synchronized void delUser(WebSocketSession session) {
        LOG.info("Leave user: {}",session.getAttributes().get(USER_KEY));
        String userName = (String) session.getAttributes().get(USER_KEY);
        if (StringUtils.isNotBlank(userName)) {
            users.remove(userName);
        }
    }

    /**
     * 发送群发消息
     * @param userName 发送给谁
     * @param msg 消息内容 群选择json条件
     */
    public static void sendGroupMsg(String userName,String msg,Map<String,Object> map) {
        LOG.info("send msg:{} to {} with condition={} ",msg,userName, JSON.toJSONString(map));
        WebSocketSession session = users.get(userName);
        if (session != null) {
            ImmutableMap mapContent = ImmutableMap.builder().put("user",userName).put("condition",map).build();
            try {
                if(session.isOpen())
                    session.sendMessage(new TextMessage(JSON.toJSONString(mapContent)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static Set<String> getUserNameList() {
        return users.keySet();
    }

}
