package org.yixun.support.cache.interceptor;

import javax.inject.Named;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.yixun.support.cache.annotation.Cache;

import com.dayatang.domain.InstanceFactory;

@Named
@Aspect
public class CacheInterceptor {
	@Pointcut("execution(* org.yixun.platform..*.*(..))")
	private void anyMethod() {
	}
	
	@Around("anyMethod() && @annotation(cacheAnnotation)")
	public Object doAround(ProceedingJoinPoint pjp,Cache cacheAnnotation){
		String cacheName;
		if(null == cacheAnnotation.value() || "".equals(cacheAnnotation.value())){
			cacheName = pjp.getTarget().getClass().getName();
		} else {
			cacheName = cacheAnnotation.value();
		}
		
		org.yixun.support.cache.Cache cache = InstanceFactory.getInstance(org.yixun.support.cache.Cache.class, cacheName);
		
		String cacheKey = getCacheKey(pjp.getTarget().getClass().getName(), pjp.getSignature().getName(), pjp.getArgs());
		Object result = null;
		synchronized (this) {
			if (!cache.isKeyInCache(cacheKey)) {
				try {
					result = pjp.proceed();
					cache.put(cacheKey, result);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			} else {
				result = cache.get(cacheKey);
			}
		}
		return result;
	}
	
	private String getCacheKey(String targetName, String methodName, Object[] arguments) {
		StringBuffer sb = new StringBuffer();
		sb.append(targetName).append(".").append(methodName);
		if ((arguments != null) && (arguments.length != 0)) {
			for (int i = 0; i < arguments.length; i++) {
				sb.append(".").append(arguments[i].toString());
			}
		}
		return sb.toString();
	}
}
