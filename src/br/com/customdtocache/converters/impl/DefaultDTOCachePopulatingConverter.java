package br.com.customdtocache.converters.impl;

import br.com.customdtocache.cache.key.DTOCacheKeyGenerationStrategy;
import br.com.customdtocache.cache.region.DTOCacheRegion;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.annotation.Resource;
import java.util.Map;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

@Aspect
public class DefaultDTOCachePopulatingConverter {

    private static final Logger LOG = Logger.getLogger(DefaultDTOCachePopulatingConverter.class);

    @Resource
    private DTOCacheRegion dtoCacheRegion;

    @Resource
    private Map<String, DTOCacheKeyGenerationStrategy> defaultDtoCacheKeyGenerationStrategyMap;

    @Around("execution(public * de.hybris.platform.converters.impl.*.convert(..))")
    public Object convert(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String myBeanName = ((AbstractPopulatingConverter)proceedingJoinPoint.getTarget()).getMyBeanName();


        DTOCacheKeyGenerationStrategy dtoCacheKeyGenerationStrategy = defaultDtoCacheKeyGenerationStrategyMap.get(myBeanName);

        if (dtoCacheKeyGenerationStrategy != null) {
            return getCacheObject(dtoCacheKeyGenerationStrategy, myBeanName, proceedingJoinPoint);
        }

        return proceedingJoinPoint.proceed();
    }


    private Object getCacheObject(DTOCacheKeyGenerationStrategy dtoCacheKeyGenerationStrategy, String myBeanName, ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String dtoCacheKey = dtoCacheKeyGenerationStrategy.generateCacheKey(myBeanName, proceedingJoinPoint.getArgs()[0]);

        if (dtoCacheRegion.get(dtoCacheKey) != null) {
            return dtoCacheRegion.get(dtoCacheKey);

        } else {
            Object target = proceedingJoinPoint.proceed();

            dtoCacheRegion.put(dtoCacheKey, target);

            return target;
        }
    }
}
