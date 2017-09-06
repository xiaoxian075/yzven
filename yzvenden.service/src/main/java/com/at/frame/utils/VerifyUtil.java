package com.at.frame.utils;


/**
 * Created by Administrator on 2017/5/19.
 */
public class VerifyUtil {
    public static boolean isAnyEmpty(String param){
        return StringUtils.isEmpty(param);
    }
    public static boolean isAnyEmpty(String...params){
        return StringUtils.isAnyEmpty(params);
    }
    public static boolean isAnyNull(Object param){
        return param == null;
    }
    public static boolean isAnyNull(Object...params){
        int len;
        if(params == null || (len = params.length) == 0) return true;
        Object param;
        for(int i=0;i<len;i++){
            param = params[i];
            if(param == null) return true;
        }
        return false;
    }
    public static boolean isAnyLengthLess(int min,String str){
        if(str == null) return true;
        int len = str.length();
        return len < min;
    }
    public static boolean isAnyLengthLess(int min,String... strs){
        int l;
        if(strs == null || (l = strs.length) == 0) return true;
        for(String str  : strs){
            if(str == null) return true;
            int len = str.length();
            if(len < min) return true;
        }
        return false;
    }
    public static boolean isAnyLengthGreat(int max,String str){
        if(str == null) return false;
        int len = str.length();
        return len > max;
    }
    public static boolean isAnyLengthGreat(int max,String... strs){
        int l;
        if(strs == null || (l = strs.length) == 0) return false;
        for(String str  : strs){
            if(str == null) return false;
            int len = str.length();
            if(len > max) return true;
        }
        return false;
    }

    public static boolean isAnyLengthNotIn(int min, int max, String str){
        if(str == null) return true;
        int len = str.length();
        return len < min || len > max;
    }
    public static boolean isAnyLengthNotIn(int min, int max, String... strs){
        int l;
        if(strs == null || (l = strs.length) == 0) return true;
        for(String str : strs){
            if(str == null) return true;
            int len = str.length();
            if(len < min || len > max) return true;
        }
        return false;
    }

    public static boolean isAnyLess(int param, int less){
        return param < less;
    }
    public static boolean isAnyLess(float param, int less){
        return param < less;
    }
    public static boolean isAnyLess(double param, int less){
        return param < less;
    }
    public static boolean isAnyLess(long param, int less){
        return param < less;
    }
    public static <T extends Number> boolean isAnyLess(T t, int less){
        return t == null || t.doubleValue() < less;
    }
    public static <T extends Number> boolean isAnyLess(T... nums){
        int l;
        if(nums == null || (l = nums.length) == 1) return true;
        int less = nums[--l].intValue();
        T t;
        for(int i=0;i<l;i++){
            t = nums[i];
            if(t == null || t.doubleValue() < less) return true;
        }
        return false;
    }
}
