package com.at.frame.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/4/18.
 * 自动化控制器的请求地址Url注解
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Controller
@ResponseBody
public @interface AutoCtrl {
    public static final String DEFAULT = "__default__group__1_6_7_";

    int id();//控制器Id
    /**
     * 设置地址分组
     * 默如为无归属组，如果配置，则为组别： value  的组
     * 要获取该组地址配置时，使用 ?g=value 获取该组别信息
     */
    @AliasFor("value")
    String[] name() default DEFAULT;

    @AliasFor("name")
    String[] value() default DEFAULT;

    /**
     * 配置所属方法的方法名称前缀，将自动化使用陀锋命名法
     */
    String prefix() default "";

    int version() default 1;

    /**
     * 控制器描述
     * @return
     */
    String desc() default "";

    int parentId() default 0;

    String url() default "-";

    String[] params() default {};

    /**
     * 是否保存到数据库
     * 配置合Api2Db接口使用
     */
    boolean api2Db() default true;
}
