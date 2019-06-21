package br.com.customdtocache.cache.region.impl;

import br.com.customdtocache.cache.key.impl.DTOCacheKey;
import br.com.customdtocache.cache.region.DTOCacheRegion;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.impl.EHCacheRegion;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DefaultDTOCacheRegion extends EHCacheRegion implements DTOCacheRegion {
    static final Logger log = Logger.getLogger(DefaultDTOCacheRegion.class.getName());

    public DefaultDTOCacheRegion(final String name, final int maxEntries) {
        super(name, maxEntries, "LRU", false, true, null);
        if (log.isDebugEnabled()) {
            log.debug("CacheAccessImpl created for " + this.toString());
        }
    }

    public DefaultDTOCacheRegion(final String name, final int maxEntries, final String evictionPolicy) {
        super(name, maxEntries, evictionPolicy, false, true, null);
        if (log.isDebugEnabled()) {
            log.debug("CacheAccessImpl created for " + this.toString());
        }
    }


    public DefaultDTOCacheRegion(final String name, final int maxEntries, final String evictionPolicy,
                                 final boolean exclusiveComputation, final boolean statsEnabled) {
        super(name, maxEntries, evictionPolicy, exclusiveComputation, statsEnabled, null);
        if (log.isDebugEnabled()) {
            log.debug("CacheAccessImpl created for " + this.toString());
        }
    }

    public DefaultDTOCacheRegion(final String name, final int maxEntries, final String evictionPolicy,
                                 final boolean exclusiveComputation, final boolean statsEnabled, final Long ttlSeconds) {
        super(name, maxEntries, evictionPolicy, exclusiveComputation, statsEnabled, ttlSeconds);
        if (log.isDebugEnabled()) {
            log.debug("CacheAccessImpl created for " + this.toString());
        }
    }

    @Override
    public Object get(final Object key) {
        final DTOCacheKey genericCacheKey = generateDTOCacheKey(key);
        try {
            return super.get(genericCacheKey);
        } catch (final IllegalStateException e) {
            log.error("IllegalStateException occured", e);
            return null;
        }
    }

    @Override
    public Set<Object> getKeys() {
        final Set<Object> ret = new HashSet<Object>();
        final Collection<CacheKey> allKeys = super.getAllKeys();
        for (final CacheKey theKey : allKeys) {
            ret.add(theKey);
        }
        return ret;
    }

    @Override
    public void put(final Object key, final Object object) {
        final DTOCacheKey genericCacheKey = generateDTOCacheKey(key);
        final DefaultCacheValueLoaderImpl<Object> loader = new DefaultCacheValueLoaderImpl<Object>();
        loader.setValue(object);
        remove(genericCacheKey);
        if (log.isDebugEnabled()) {
            log.debug("Object with following key(s) put into cache region " + this.getName() + ": " + genericCacheKey.toString());
        }
        super.getWithLoader(genericCacheKey, loader);
    }

    @Override
    public void putIfAbsent(final Object key, final Object object) {
        if (isAbsent(key)) {
            put(key, object);
        }
    }

    private boolean isAbsent(final Object key) {
        final DTOCacheKey genericCacheKey = generateDTOCacheKey(key);
        if (super.containsKey(genericCacheKey)) {
            return false;
        }
        return true;
    }

    @Override
    public void remove(final Object key) {
        final DTOCacheKey genericCacheKey = generateDTOCacheKey(key);
        if (log.isDebugEnabled()) {
            log.debug(
                    "Object with following key(s) removed from cache region " + this.getName() + ": " + genericCacheKey.toString());
        }
        super.remove(genericCacheKey, false);
    }

    @Override
    public int getNumObjects() {
        return super.getAllKeys().size();
    }

    @Override
    public Object getWithLoader(final Object key, final CacheValueLoader<?> loader) {
        final DTOCacheKey genericCacheKey = generateDTOCacheKey(key);
        return super.getWithLoader(genericCacheKey, loader);
    }

    @Override
    public void clearCache() {
        if (log.isDebugEnabled()) {
            log.debug("Cache region " + this.getName() + "cleared.");
        }
        super.clearCache();
    }


    private DTOCacheKey generateDTOCacheKey(final Object key) {
        if (key instanceof DTOCacheKey) {
            return (DTOCacheKey) key;
        } else {
            return new DTOCacheKey(key, DTOCacheKey.DEFAULT_DTO_TYPECODE);
        }
    }

    private class DefaultCacheValueLoaderImpl<V> implements CacheValueLoader<V> {

        private V obj;


        public DefaultCacheValueLoaderImpl() {
            super();
        }

        @Override
        public V load(final CacheKey arg0) throws CacheValueLoadException {
            return this.obj;
        }

        public void setValue(final Object obj) {
            this.obj = (V) obj;
        }

    }
}
