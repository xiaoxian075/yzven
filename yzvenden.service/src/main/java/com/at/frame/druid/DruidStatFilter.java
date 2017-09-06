package com.at.frame.druid;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * Created by Administrator on 2017/4/18.
 */
@WebFilter(
        filterName = "druidStatFilter",
        urlPatterns = "/*",
        initParams = {
                //忽略资源
                @WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")
        })
public class DruidStatFilter extends WebStatFilter{
    //druid监控filter
}
