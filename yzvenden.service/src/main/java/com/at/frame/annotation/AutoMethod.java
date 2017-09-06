package com.at.frame.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/4/18.
 * 用来配置自动化生成请求列表地址
  */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoMethod {

    int id();//方法Id

    @AliasFor("value")
    String[] name() default {};

    @AliasFor("name")
    String[] value() default {};

    String desc() default "";

    int version() default 0;

    RequestMethod[] method() default {RequestMethod.POST,RequestMethod.GET};

    String[] api() default {};

    ApiImplicitParam[] implicitParams() default {};
}
