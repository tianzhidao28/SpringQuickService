package com.rocyuan.zero.function.weixin;

import com.github.sd4324530.fastweixin.handle.MessageHandle;
import com.github.sd4324530.fastweixin.message.BaseMsg;
import com.github.sd4324530.fastweixin.message.TextMsg;
import com.github.sd4324530.fastweixin.message.req.TextReqMsg;

/**
 * Created by rocyuan on 2015/11/1.
 */
public class TextMessageHandle implements MessageHandle<TextReqMsg> {
    @Override
    public BaseMsg handle(TextReqMsg textReqMsg) {
        return new TextMsg(textReqMsg.getContent());
    }

    @Override
    public boolean beforeHandle(TextReqMsg textReqMsg) {



        return false;
    }
}
