package org.yixun.platform.infra.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthDetailUtil {
	public static String getLoginName(){
		UserDetails userDetails;
		try {
			userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return "user1";
		} 
		return userDetails.getUsername();
		
	}
}
