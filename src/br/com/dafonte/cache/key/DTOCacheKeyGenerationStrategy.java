package br.com.dafonte.cache.key;

public interface DTOCacheKeyGenerationStrategy {

    String generateCacheKey(String className, Object data);

}
