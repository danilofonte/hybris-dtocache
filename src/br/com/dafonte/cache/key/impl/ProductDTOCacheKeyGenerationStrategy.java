package br.com.dafonte.cache.key.impl;

import br.com.dafonte.cache.key.DTOCacheKeyGenerationStrategy;
import de.hybris.platform.core.model.product.ProductModel;

public class ProductDTOCacheKeyGenerationStrategy implements DTOCacheKeyGenerationStrategy {

    @Override
    public String generateCacheKey(String className, Object data) {
        if (data instanceof ProductModel) {
            return className.concat(":").concat(((ProductModel) data).getCode());
        }
        return null;
    }
}
