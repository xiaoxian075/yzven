package com.at.frame.druid;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Created by Administrator on 2017/4/18.
 */
@WebServlet(
        name = "druidWebServlet",
        urlPatterns = "/druid/*",
        initParams = {
                @WebInitParam(name = "allow", value = ""),
                @WebInitParam(name = "loginUsername", value = "admin"),
                @WebInitParam(name = "loginPassword", value = "123123"),
                @WebInitParam(name = "restEnable", value = "false")
        })
public class DruidStaViewServlet extends StatViewServlet{
    //配置druid的监控访问
}
