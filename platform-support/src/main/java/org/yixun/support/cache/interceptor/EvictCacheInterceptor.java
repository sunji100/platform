package org.yixun.support.cache.interceptor;

import javax.inject.Named;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.yixun.support.cache.Cache;
import org.yixun.support.cache.annotation.EvictCache;

import com.dayatang.domain.InstanceFactory;

@Named
@Aspect
public class EvictCacheInterceptor {
	@Pointcut("execution(* org.yixun.platform..*.*(..))")
	private void anyMethod() {
	}
	
	@Before("anyMethod() && @annotation(evictCache)")
	public void doBefore(JoinPoint jp,EvictCache evictCache){
		String cacheName;
		if(null == evictCache.value() || "".equals(evictCache.value())){
			cacheName = jp.getTarget().getClass().getName();
		} else {
			cacheName = evictCache.value();
		}
		
		Cache cache = InstanceFactory.getInstance(Cache.class, cacheName);
		cache.clearCache();
	}
}
