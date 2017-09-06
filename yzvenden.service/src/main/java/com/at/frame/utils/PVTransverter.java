package com.at.frame.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;

import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/5/12.
 */
public class PVTransverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PVTransverter.class);
    private static final ConcurrentHashMap<String,BeanCopier> COIPERS = new ConcurrentHashMap<>();

    /**
     * PV值转化
     * @apiNote
     * 需要注意的，该转化为支持深度的克隆转化，但是，必须保证
     * 要化转的对象p中的其他对象继承Cloneable接口，否则将会是浅克隆
     * @param p         要转化的对象p
     * @param v         转化到对象v
     * @return          转到之后的对象v
     */
    public static <P,V> V transfer(P p,Class<V> v){
        if(p == null || v == null) throw new NullPointerException();
        final Class<?> clazz = p.getClass();
        if(clazz.isArray() || v.isArray()){
            throw new IllegalArgumentException("不能为数组");
        }
        BeanCopier copier = getCopier(clazz,v);
        V obj = null;
        try {
            obj = v.newInstance();
            copier.copy(p,obj,(pojo,fieldType,fieldName)->{
                if(pojo instanceof Cloneable){
                    return clone(pojo);
                }
                return pojo;
            });
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("po 2 vo exception",e);
        }
        return obj;
    }

    private static <P> P clone(P p){
        if(p == null) return null;
        final Class<?> clazz = p.getClass();
        if(clazz.isArray()
                && !clazz.getComponentType().equals(byte.class)){
            final int len = Array.getLength(p);
            Object array = Array.newInstance(clazz.getComponentType(),len);
            for(int i = 0 ; i < len ; i ++){
                Array.set(array,i,clone(Array.get(array,i)));
            }
            return (P)array;
        }else{
            try {
                P cloneBean = (P)clazz.newInstance();
                BeanCopier copier = getCopier(clazz,clazz);
                copier.copy(p,cloneBean,(pojo,fieldType,fieldName)->{
                    if(pojo instanceof Cloneable){
                        return clone(pojo);
                    }
                    return pojo;
                });
                return cloneBean;
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("clone exception",e);
            }
            return null;
        }
    }

    private static BeanCopier getCopier(Class p,Class v){
        String coiperId = p.toString().concat(v.toString());
        BeanCopier copier ;
        if(!COIPERS.contains(coiperId)){
            copier = BeanCopier.create(p,v,true);
            COIPERS.put(coiperId,copier);
        }else{
            copier = COIPERS.get(coiperId);
        }
        return copier;
    }

}
