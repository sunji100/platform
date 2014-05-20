package org.yixun.platform.web.cxf.interceptor;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yixun.platform.application.wssecurity.ResourceApplication;
import org.yixun.platform.application.wssecurity.UserApplication;
import org.yixun.platform.application.wssecurity.dto.UserDTO;
import org.yixun.support.auth.util.SecurityMD5;
import org.yixun.support.cache.Cache;

import com.dayatang.domain.InstanceFactory;

public class AuthorizationInterceptor extends AbstractOutDatabindingInterceptor {
	private Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);
	@Inject
	private UserApplication userApplication;
	@Inject
	private ResourceApplication resourceApplication;
	
	private Cache resourceCache;
	
	public AuthorizationInterceptor() {
		super(Phase.PRE_INVOKE);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		Map<String, List<String>> headers = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
		if (headers.get("username") == null || headers.get("password") == null) {
			throw new SecurityException("未填写用户信息.");
		}
		String username = headers.get("username").get(0);
		String password = headers.get("password").get(0);
		UserDTO userDTO = null;
		try {
			userDTO = userApplication.loadUserByUseraccount(username);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new SecurityException("用户不存在.");
		}
		if(null == userDTO){
			throw new SecurityException("用户不存在.");
		}
		if(!SecurityMD5.isPasswordValid(userDTO.getUserPassword(), password, username)){
			throw new SecurityException("密码不正确.");
		}
		
		String requestUri = String.valueOf(message.get(Message.REQUEST_URI));
		String requestUrl = String.valueOf(message.get(Message.REQUEST_URL));
		if(!getResourceCache().isKeyInCache("wsResource")){
			loadResources();
		}
		
		Map<String, List<String>> allWsResourceAndRoles = (Map<String, List<String>>) getResourceCache().get("wsResource");
		List<String> roleNames = userDTO.getRoles();
		
		
		message.getInterceptorChain().doIntercept(message);
		
//		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}
	
	private Cache getResourceCache(){
		if(null == resourceCache){
			resourceCache = InstanceFactory.getInstance(Cache.class, "allWsResourceAndRoles");
		}
		return resourceCache;
	}
	
	private void loadResources(){
		Map<String, List<String>> allWsResourceAndRoles = null;
		try {
			allWsResourceAndRoles = resourceApplication.getAllResourceAndRoles();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new SecurityException("权限不足.");
		}
		getResourceCache().put("wsResource", allWsResourceAndRoles);
	}
}
