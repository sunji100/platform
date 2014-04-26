package org.yixun.platform.web.cxf.handler;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;



@Named("passwordCallbackHandler")
public class ServerPasswordCallbackHandler implements CallbackHandler {
	Logger logger = LoggerFactory.getLogger(ServerPasswordCallbackHandler.class);
	@Inject
	private AuthenticationManager authenticationManager;
	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		// 标识符
		String identifier = pc.getIdentifier();
		// 此处获取到的password为null，但是并不代表服务端没有拿到该属性。
		String password = pc.getPassword();
		logger.info("identifier:" + identifier);
		logger.info("password:" + password);
		
		if (identifier != null) {
			/**
			 * 此处应该这样做：
			 *  1. 查询数据库，得到数据库中该用户名对应密码
			 *  2. 设置密码，wss4j会自动将你设置的密码与客户端传递的密码进行匹配 
			 *  3. 如果相同，则放行，否则返回权限不足信息
			 * 
			 */
			try {
				Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
			} catch (AuthenticationException e) {
				logger.info("无效的用户信息." + e.getMessage());
				throw new SecurityException("无效的用户信息.");
			}
			pc.setPassword("password");
		} else {
			logger.info("未填写用户信息");
			throw new SecurityException("未填写用户信息.");
		}
	}

}
