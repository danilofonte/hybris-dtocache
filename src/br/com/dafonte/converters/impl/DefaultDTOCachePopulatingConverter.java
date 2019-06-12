package br.com.dafonte.converters.impl;

import br.com.dafonte.cache.key.DTOCacheKeyGenerationStrategy;
import br.com.dafonte.cache.region.DTOCacheRegion;
import de.hybris.platform.core.model.ItemModel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.annotation.Resource;
import java.util.Map;

@Aspect
public class DefaultDTOCachePopulatingConverter {

    private static final Logger LOG = Logger.getLogger(DefaultDTOCachePopulatingConverter.class);


    @Resource
    private DTOCacheRegion dtoCacheRegion;

    @Resource
    private Map<ItemModel, DTOCacheKeyGenerationStrategy> dtoCacheKeyGenerationStrategyMap;

    @Around("execution(public * de.hybris.platform.converters.impl.*.convert(..))")
    public Object convert(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String className = StringUtils.substringAfterLast(proceedingJoinPoint.getArgs()[0].getClass().getName().toLowerCase(),".");

        DTOCacheKeyGenerationStrategy dtoCacheKeyGenerationStrategy = dtoCacheKeyGenerationStrategyMap.get(className);

        if (dtoCacheKeyGenerationStrategy != null) {
            String dtoCacheKey = dtoCacheKeyGenerationStrategy.generateCacheKey(className,proceedingJoinPoint.getArgs()[0]);
            if (dtoCacheRegion.get(dtoCacheKey) != null) {
                return dtoCacheRegion.get(dtoCacheKey);

            } else {
                Object target = proceedingJoinPoint.proceed();

                dtoCacheRegion.put(dtoCacheKey, target);

                return target;
            }
        }

        return proceedingJoinPoint.proceed();
    }
}
