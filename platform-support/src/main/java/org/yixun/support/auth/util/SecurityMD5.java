package org.yixun.support.auth.util;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class SecurityMD5 {
	private static Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
	
	public static String encode(String password, String salt){
		return md5PasswordEncoder.encodePassword(password, salt);
	}
}
