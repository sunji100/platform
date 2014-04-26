package org.yixun.platform.web.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.yixun.platform.application.security.AuthDataService;
import org.yixun.support.cache.Cache;

import com.dayatang.domain.InstanceFactory;

/**
 * 获得请求所拥有的权限
 * @author sunji
 *
 */
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	
	@Inject
	private AuthDataService authDataService;
	
	private Cache resourceCache;
	/**
	 * 获得缓存对象
	 * @return
	 */
	private Cache getResourceCache(){
		if(null == resourceCache){
			resourceCache = InstanceFactory.getInstance(Cache.class, "allResourceAndRoles");
		}
		return resourceCache;
	}
	/**
	 * 加载请求url与role对应关系
	 */
	private void loadResources(){
		Map<String, List<String>> allResourceAndRoles = null;
		try {
			allResourceAndRoles = authDataService.getAllResourceAndRoles();
		} catch (Exception e) {
			e.printStackTrace();
		}
		getResourceCache().put("allResourceAndRoles", allResourceAndRoles);
	}
	
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		
		if(!getResourceCache().isKeyInCache("allResourceAndRoles")){
			loadResources();
		}
		
		@SuppressWarnings("unchecked")
		Map<String, List<String>> allResourceAndRoles = (Map<String, List<String>>) getResourceCache().get("allResourceAndRoles");
		
		String url = ((FilterInvocation) object).getRequestUrl();
		int position = url.indexOf("?");
		if (-1 != position) {
			url = url.substring(0, position);
		}
		
		//url = url.substring(url.indexOf("/")+1);
		//获得资源对应的权限
		List<String> roles = allResourceAndRoles.get(url);
		if(null != roles){
			Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
			for (String role : roles) {
				ConfigAttribute configAttribute = new SecurityConfig(role);
				configAttributes.add(configAttribute);
			}
			return configAttributes;
		}
//		else {
//			Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
//			return configAttributes;
//		}
		return null;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return true;
	}

}
