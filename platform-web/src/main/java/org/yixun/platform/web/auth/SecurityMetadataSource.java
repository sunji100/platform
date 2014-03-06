package org.yixun.platform.web.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.yixun.platform.application.security.AuthDataService;

public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	
	@Inject
	private AuthDataService authDataService;
	
	private Map<String, List<String>> allResourceAndRoles;
	
	private void loadResources(){
		allResourceAndRoles = authDataService.getAllResourceAndRoles();
	}
	
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		if(null == allResourceAndRoles){
			loadResources();
		}
		String url = ((FilterInvocation) object).getRequestUrl();
		int position = url.indexOf("?");
		if (-1 != position) {
			url = url.substring(0, position);
		}
		
		url = url.substring(url.indexOf("/")+1);
		System.out.println(url);
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
