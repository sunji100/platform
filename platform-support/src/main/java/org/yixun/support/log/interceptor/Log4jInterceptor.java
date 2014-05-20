package org.yixun.support.log.interceptor;

import javax.inject.Named;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Aspect
public class Log4jInterceptor {
	@Pointcut("execution(* org.yixun.platform..*.*(..))")
	private void anyMethod() {
	}
	
	@AfterReturning("anyMethod()")
	public void doAfter(JoinPoint jp){
		Logger logger = LoggerFactory.getLogger(jp.getSignature().getDeclaringType());
		
		StringBuilder sbArgs = new StringBuilder();
		Object[] args = jp.getArgs();
		if(null != args){
			for (Object arg : jp.getArgs()) {
				sbArgs.append(arg.toString());
				sbArgs.append(",");
			}
		}
		logger.debug("-execute:[class:"+jp.getSignature().getDeclaringType().getName()+"][method:"+ jp.getSignature().getName() +"][args:"+ sbArgs +"]");
	}
}
