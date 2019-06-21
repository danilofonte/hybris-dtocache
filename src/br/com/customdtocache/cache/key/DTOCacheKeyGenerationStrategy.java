package br.com.customdtocache.cache.key;

public interface DTOCacheKeyGenerationStrategy {

    String generateCacheKey(String myBeanName, Object data);

}
