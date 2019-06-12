package br.com.dafonte.cache.region;

import de.hybris.platform.regioncache.CacheValueLoader;

import java.util.Set;

public interface DTOCacheRegion {

    public Object get(Object key);

    public Set<Object> getKeys();

    public void put(Object key, Object object);

    public void putIfAbsent(Object name, Object object);

    public void remove(Object key);

    public int getNumObjects();

    public Object getWithLoader(Object key, CacheValueLoader<?> loader);

    public void clearCache();
}
