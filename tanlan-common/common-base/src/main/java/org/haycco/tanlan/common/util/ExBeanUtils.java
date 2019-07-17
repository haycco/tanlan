package org.haycco.tanlan.common.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * BeanUtils 工具类的增强版
 *
 * @author Orochi-Yzh
 * @dateTime 2018/3/16 14:11
 */
public class ExBeanUtils extends BeanUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExBeanUtils.class);

    private static String[] getNullPropertyNames (Object source,String... excludProperties) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        emptyNames.addAll(Arrays.asList(excludProperties));
        String[] result = new String[emptyNames.size() + excludProperties.length];
        return emptyNames.toArray(result);
    }

    /**
     * 忽略源的空字段
     * @param src
     * @param target
     */
    public static void copyIgnoreNull(Object src, Object target){
        if(src == null){
            return;
        }
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 忽略源的空字段和排除的字段
     * @param src
     * @param target
     * @param excludeProperties
     */
    public static void copyIgnoreNullAndExcludeProperties(Object src, Object target,String... excludeProperties){
        if(src == null){
            return;
        }
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src,excludeProperties));
    }

}
