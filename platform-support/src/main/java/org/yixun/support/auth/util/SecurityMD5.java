package org.yixun.support.auth.util;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class SecurityMD5 {
	private static Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
	
	public static String encode(String password, String salt){
		return md5PasswordEncoder.encodePassword(password, salt);
	}
	/**
	 * 
	 * @param encPass 加密后的值
	 * @param rawPass 
	 * @param salt
	 * @return
	 */
	public static boolean isPasswordValid(String encPass,String rawPass,String salt){
		return md5PasswordEncoder.isPasswordValid(encPass, rawPass, salt);
	}
}
