package org.yixun.support.cache.interceptor;

import javax.inject.Named;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.yixun.support.cache.Cache;
import org.yixun.support.cache.annotation.EvictCache;

import com.dayatang.domain.InstanceFactory;

/**
 * 清除缓存切面类
 * @author sunji
 *
 */
@Named
@Aspect
public class EvictCacheInterceptor {
	@Pointcut("execution(* org.yixun.platform..*.*(..))")
	private void anyMethod() {
	}
	
	@Before("anyMethod() && @annotation(evictCache)")
	public void doBefore(JoinPoint jp,EvictCache evictCache){
		String cacheName;
		if(null == evictCache.cacheName() || "".equals(evictCache.cacheName())){
			cacheName = jp.getTarget().getClass().getName();
		} else {
			cacheName = evictCache.cacheName();
		}
		
		//缓存key名称
		String cacheKey = evictCache.cacheKey();
		
		Cache cache = InstanceFactory.getInstance(Cache.class, cacheName);
		if(null == cacheKey || "".equals(cacheKey)){
			cache.clearCache();//清除缓存中全部内容
		} else {
			cache.remove(cacheKey);//清除指定key的缓存
		}
		
	}
}
