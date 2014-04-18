package org.yixun.support.cache.interceptor;

import javax.inject.Named;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.yixun.support.cache.annotation.Cache;

import com.dayatang.domain.InstanceFactory;

/**
 * 添加缓存切面类
 * @author sunji
 *
 */
@Named
@Aspect
public class CacheInterceptor {
	@Pointcut("execution(* org.yixun.platform..*.*(..))")
	private void anyMethod() {
	}
	
	@Around("anyMethod() && @annotation(cacheAnnotation)")
	public Object doAround(ProceedingJoinPoint pjp,Cache cacheAnnotation){
		//缓存名称
		String cacheName;
		if(null == cacheAnnotation.cacheName() || "".equals(cacheAnnotation.cacheName())){
			cacheName = pjp.getTarget().getClass().getName();
		} else {
			cacheName = cacheAnnotation.cacheName();
		}
		
		org.yixun.support.cache.Cache cache = InstanceFactory.getInstance(org.yixun.support.cache.Cache.class, cacheName);
		
		//缓存key名称
		String cacheKey;
		if(null == cacheAnnotation.cacheKey() || "".equals(cacheAnnotation.cacheKey())){
			cacheKey = getCacheKey(pjp.getTarget().getClass().getName(), pjp.getSignature().getName(), pjp.getArgs());
		} else {
			cacheKey = cacheAnnotation.cacheKey();
		}
		//添加或获取缓存数据
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
