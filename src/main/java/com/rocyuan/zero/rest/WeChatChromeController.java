package com.rocyuan.zero.rest;

import com.rocyuan.zero.function.websocket.UserUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rocyuan on 15/12/12.
 */
//@RestController
//@RequestMapping("/weixin")
public class WeChatChromeController {

    @RequestMapping("/msgs/send")
    public void sendMsg(@RequestParam("user")String toUser,@RequestParam("message")String message) {
        Map<String,Object> m = new HashMap();
        m.put("Sex",1);
        UserUtils.sendGroupMsg(toUser,message,m);
    }

    @RequestMapping("/users/list")
    public Set<String> getUserLists(){
        return UserUtils.getUserNameList();
    }
}
