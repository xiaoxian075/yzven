package com.at.frame.annotation;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/5/15.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MapperCustom {
   Class value();
}
