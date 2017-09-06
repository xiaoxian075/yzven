package com.at.frame.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/5/4.
 */

@ConfigurationProperties(
        prefix = "at.frame"
)
@Component
public class AtFrameProperties {


    /**
     * 对象池大小
     */
    private int objectPoolCapacity = 60;
    /**
     * 控制器名称前缀
     * 格式：
     * 1.直接前缀，如：ctrl
     * 2.包名前缀区别：如 com.at.controller.admin:c;com.at.app:app
     * 表示：com.at.controller.admin下的控制器的前缀为c
     * 而com.at.app下的控制器的前缀为app
     */
    private String controllerAbbr = "c";
    /**
     * 自动生成url方法的控制器缩写
     */
    private String methodAbbr = "m";
    /**
     * 控制器拦截
     * 如：com.at.controller
     */
    private String controllerHandler;
    /**
     * 配置swagger
     */
    private boolean swagger;
    /**
     * 配置是否启用查询拦截
     */
    private boolean mybatisQueryInterceptor;
    /**
     * 配置是否使用Id做为URL地址的生成
     * 如果否，则使用name做地址的生成
     * 如果name为空，则使用id
     */
    private boolean urlId = true;

    public boolean isSwagger() {
        return swagger;
    }

    public void setSwagger(boolean swagger) {
        this.swagger = swagger;
    }

    public boolean isMybatisQueryInterceptor() {
        return mybatisQueryInterceptor;
    }

    public void setMybatisQueryInterceptor(boolean mybatisQueryInterceptor) {
        this.mybatisQueryInterceptor = mybatisQueryInterceptor;
    }

    public String getControllerHandler() {
        return controllerHandler;
    }

    public void setControllerHandler(String controllerHandler) {
        this.controllerHandler = controllerHandler;
    }

    public int getObjectPoolCapacity() {
        return objectPoolCapacity;
    }

    public void setObjectPoolCapacity(int objectPoolCapacity) {
        this.objectPoolCapacity = objectPoolCapacity;
    }

    public String getControllerAbbr() {
        return controllerAbbr;
    }

    public void setControllerAbbr(String controllerAbbr) {
        this.controllerAbbr = controllerAbbr;
    }

    public String getMethodAbbr() {
        return methodAbbr;
    }

    public void setMethodAbbr(String methodAbbr) {
        this.methodAbbr = methodAbbr;
    }
}
