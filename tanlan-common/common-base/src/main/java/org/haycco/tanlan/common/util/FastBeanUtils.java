package org.haycco.tanlan.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.cglib.beans.BeanCopier;

/**
 *@Description: 基于Cglib的高性能对象属性复制工具
*/
public class FastBeanUtils {

    //缓存
    static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>();

    /**
     *@Description:
     *@param: [srcObj 原对象, destObj 目标对象, cache 是否缓存]
    */
    private static void copy(Object srcObj, Object destObj,boolean cache) {
        if(cache){
            String key = genKey(srcObj.getClass(), destObj.getClass());
            BeanCopier copier;
            if (!BEAN_COPIERS.containsKey(key)) {
                copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), false);
                BEAN_COPIERS.put(key, copier);
            } else {
                copier = BEAN_COPIERS.get(key);
            }
            copier.copy(srcObj, destObj, null);
        }else{
            copy(srcObj,destObj);
        }

    }

    /**
     *@Description:
     *@param: [srcObj 原对象, destObj 目标对象]
     */
    public static void copy(Object srcObj, Object destObj) {
        BeanCopier copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), false);
        copier.copy(srcObj, destObj, null);
    }

    //构建缓存key
    private static String genKey(Class<?> srcClazz, Class<?> destClazz) {  
        return srcClazz.getName() + destClazz.getName();  
    }

}  