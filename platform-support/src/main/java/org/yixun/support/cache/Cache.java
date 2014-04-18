package org.yixun.support.cache;


public interface Cache {
	public void put(String key, Object value);
	public Object get(String key);
	public boolean isKeyInCache(String key);
	public void removeCache();
	public void clearCache();
	public void remove(String key);
}
