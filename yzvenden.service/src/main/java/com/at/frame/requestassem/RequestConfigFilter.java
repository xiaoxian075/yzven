package com.at.frame.requestassem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/19.
 * 自动化地址请求接口的拦截器
 * 只能配置1个实例。如果配置多个，将会抛出异常
 */
public interface RequestConfigFilter{
    public static final String SUCCESS = "success";
    /**
     * 接口地址拦截器
     * @param req
     * @param resp
     * @return　　如果为success则认为是成功，返回自动自动生成的信息，否则为失败，失败将直接返回该信息
     */
    public String accept(HttpServletRequest req, HttpServletResponse resp);

    /**
     * 添加请求地址配置
     * @return Map<name,url>
     */
    public Map<String,String> addReq();
}
