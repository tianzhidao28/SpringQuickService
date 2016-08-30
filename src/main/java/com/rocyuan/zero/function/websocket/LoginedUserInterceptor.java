package com.rocyuan.zero.function.websocket;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;

/**
 *
 * Created by rocyuan on 15/12/11.
 */
public class LoginedUserInterceptor extends HttpSessionHandshakeInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginedUserInterceptor.class);

    public LoginedUserInterceptor() {
        super();
    }

    public LoginedUserInterceptor(Collection<String> attributeNames) {
        super(attributeNames);
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        LOG.info("before httpsession handsShake..");
        if(request instanceof ServletServerHttpRequest){
            HttpSession session = ((ServletServerHttpRequest) request).getServletRequest().getSession(false);
            if(session!=null){

                String loginedUser = (String) session.getAttribute(UserUtils.USER_KEY);
                if(StringUtils.isNotBlank(loginedUser)) {
                    attributes.put(UserUtils.USER_KEY,loginedUser);
                    attributes.put("HTTPSESSIONID",session.getId());
                    LOG.info("before httpsession ,userName={},sessionId={}",loginedUser,session.getId());

                }
            }
        }

        LOG.info("before httpsession handshake ,http header = {}: ",request.getHeaders());
        return super.beforeHandshake(request,response,wsHandler,attributes);

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        LOG.info("after handshake {}",request.getHeaders().toString());
        super.afterHandshake(request, response, wsHandler, ex);

    }
}
