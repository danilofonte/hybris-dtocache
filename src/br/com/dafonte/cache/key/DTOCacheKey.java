package br.com.dafonte.cache.key;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;

public class DTOCacheKey implements CacheKey
{

    public static final String DEFAULT_DTO_TYPECODE = "DTOObjects";


    private final Object[] keys;


    private final Object typeCode;


    private String tenant;


    private CacheUnitValueType valueType;


    public DTOCacheKey(final Object[] keys, final String typeCode)
    {
        this.keys = keys;
        this.typeCode = typeCode;
    }


    public DTOCacheKey(final Object[] keys, final String typeCode, final String tenant, final CacheUnitValueType valueType)
    {
        this(keys, typeCode);
        this.tenant = tenant;
        this.valueType = valueType;
    }


    public DTOCacheKey(final Object key, final String typeCode)
    {
        this.keys = new Object[]
                { key };
        this.typeCode = typeCode;
    }


    public DTOCacheKey(final Object key, final String typeCode, final String tenant, final CacheUnitValueType valueType)
    {
        this(key, typeCode);
        this.tenant = tenant;
        this.valueType = valueType;
    }


    @Override
    public int hashCode()
    {
        int ret = 0;

        for (int i = 0; i < keys.length; i++)
        {
            if (keys[i] != null)
            {
                ret = ret ^ keys[i].hashCode();
            }
        }
        return ret;
    }


    @Override
    public boolean equals(final Object o)
    {
        if (o == null)
        {
            return false;
        }
        else if (o == this)
        {
            return true;
        }
        else if (!(o instanceof DTOCacheKey))
        {
            return false;
        }
        else
        {
            final DTOCacheKey command = (DTOCacheKey) o;

            if (command.keys.length != keys.length)
            {
                return false;
            }

            for (int i = 0; i < keys.length; i++)
            {
                if (keys[i] != null)
                {
                    if (command.keys[i] == null)
                    {
                        return false;
                    }
                    else
                    {
                        if (!(keys[i].equals(command.keys[i])))
                        {
                            return false;
                        }
                    }
                }
                else
                {
                    if (command.keys[i] != null)
                    {
                        return false;
                    }
                }
            }

            return true;
        }
    }


    @Override
    public String toString()
    {
        final StringBuffer ret = new StringBuffer();

        for (int i = 0; i < keys.length; i++)
        {
            ret.append(keys[i].toString());
            if (i < keys.length - 1)
            {
                ret.append('+');
            }
        }
        return ret.toString();
    }

    @Override
    public CacheUnitValueType getCacheValueType()
    {
        return valueType;
    }

    @Override
    public String getTenantId()
    {
        return tenant;
    }

    @Override
    public Object getTypeCode()
    {
        if (typeCode != null)
        {
            return typeCode.toString();
        }
        return DEFAULT_DTO_TYPECODE;
    }
}
