package org.yixun.support.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;




public class EhCacheImpl implements org.yixun.support.cache.Cache {
	private Cache cache;
	private String name;
	
	public EhCacheImpl(){
		
	}
	
	public EhCacheImpl(String name, int maxElementsInMemory, boolean overflowToDisk, boolean eternal, long timeToLiveSeconds, long timeToIdleSeconds){
		this.name = name;
		if (!CacheManager.getInstance().cacheExists(name)) {
			cache = new Cache(name,maxElementsInMemory,overflowToDisk,eternal,timeToLiveSeconds,timeToIdleSeconds);
			CacheManager.getInstance().addCache(cache);
		} else {
			cache = CacheManager.getInstance().getCache(name);
		}
	}
	
	@Override
	public void put(String key, Object value) {
		cache.put(new Element(key, value));
	}

	@Override
	public Object get(String key) {
		Element element = cache.get(key);
		if(null != element){
			return element.getObjectValue();
		}
		return null;
	}

	@Override
	public boolean isKeyInCache(String key) {
		return cache.isKeyInCache(key);
	}

	@Override
	public void removeCache()
	{
		if (CacheManager.getInstance().cacheExists(name)) {
			CacheManager.getInstance().removeCache(name);
		}
	}

	@Override
	public void clearCache() {
		cache.removeAll();
		
	}

}
