package com.at.frame;

import com.at.frame.entity.ReqInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/5/12.
 * 控制器处理
 * 用来当配置了拦截器时作用
 */
public abstract class ControllerHandler{

    protected abstract boolean before(ReqInfo reqInfo);
    protected abstract boolean after(ReqInfo reqInfo);
    protected abstract Object exception(Throwable e,ReqInfo reqInfo);
}
