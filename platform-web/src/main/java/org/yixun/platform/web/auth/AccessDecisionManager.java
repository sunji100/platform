package org.yixun.platform.web.auth;

import java.util.Collection;




import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AccessDecisionManager implements org.springframework.security.access.AccessDecisionManager {

	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		//请求拥有的权限
		for (ConfigAttribute configAttribute : configAttributes) {
			String roleName = configAttribute.getAttribute();
			
			//用户拥有的权限
			Collection<GrantedAuthority> authorities = authentication.getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {
				if(roleName.equals(grantedAuthority.getAuthority())){
					return;
				}
			}
		}
		
		throw new AccessDeniedException("没有访问的权限!!");
		
	}

	public boolean supports(ConfigAttribute attribute) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return true;
	}

}
