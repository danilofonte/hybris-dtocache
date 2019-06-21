package br.com.customdtocache.cache.key.impl;

import br.com.customdtocache.cache.key.DTOCacheKeyGenerationStrategy;
import de.hybris.platform.core.model.product.ProductModel;

public class ProductDTOCacheKeyGenerationStrategy implements DTOCacheKeyGenerationStrategy {

    @Override
    public String generateCacheKey(String myBeanName, Object data) {
        if (data instanceof ProductModel) {
            return myBeanName.concat(":").concat(((ProductModel) data).getCode());
        }
        return null;
    }
}
