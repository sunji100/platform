package org.yixun.platform.web.auth.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;

public class ValidateCodeUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		//checkValidateCode(request);
		//登录失败
		setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login.do?login_error=1"));
		return super.attemptAuthentication(request, response);
	}
	
	/**
	 * 验证码校验
	 * @param request
	 */
	protected void checkValidateCode(HttpServletRequest request) {
		
		 String userCaptchaResponse = request.getParameter("jcaptcha");  
	     boolean captchaPassed = SimpleImageCaptchaServlet.validateResponse(request,  
	                userCaptchaResponse); 
	    if (captchaPassed==false) {
	    	//验证码错误
	    	setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login.do?login_error=2"));
			throw new AuthenticationServiceException("验证码错误");
		}
	}

}
