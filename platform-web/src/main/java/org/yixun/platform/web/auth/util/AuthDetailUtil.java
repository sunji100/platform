package org.yixun.platform.web.auth.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.yixun.platform.web.auth.CustomUserDetails;

public class AuthDetailUtil {
	public static String getLoginName(){
		UserDetails userDetails;
		try {
			userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			e.printStackTrace();
			return "user1";
		} 
		return userDetails.getUsername();
	}
	
	public static CustomUserDetails getLoginInfo(){
		CustomUserDetails userDetails = null;
		try {
			userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return userDetails;
	}
	
	public static Long getLoginUserId(){
		CustomUserDetails userDetails = getLoginInfo();
		if(null != userDetails){
			return userDetails.getUserId();
		}
		return new Long(0);
	} 
}
