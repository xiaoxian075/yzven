package com.at.frame.plugin;

import com.at.frame.Api2DB;
import com.at.frame.AtFrame;
import com.at.frame.ControllerHandler;
import com.at.frame.ResultConfiguation;
import com.at.frame.annotation.AutoMethod;
import com.at.frame.mybatis.QueryExecutorFilter;
import com.at.frame.annotation.AutoCtrl;
import com.at.frame.requestassem.RequestConfigFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Spring bean 创建时的Bean处理器
 * 功能：
 *      1.处理自动化Url地址的装配
 * Created by Administrator on 2017/4/24.
 */
@Component
@AutoConfigureAfter
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class BeanPostProcessorCustom implements org.springframework.beans.factory.config.BeanPostProcessor {

    @Value("${at.frame.url:http://localhost}")
    private String root;

    private static final Logger LOG = LoggerFactory.getLogger(BeanPostProcessorCustom.class);
    //请求url时的自定义处理方法
    private static RequestConfigFilter filter = null;
    //查询结果的字段过滤器
    private static QueryExecutorFilter resultSetFilter = null;
    public static QueryExecutorFilter getResultSetFilter(){
        return resultSetFilter;
    }
    //控制器处理类
    private static ControllerHandler controllerHandler = null;
    public static ControllerHandler getControllerHandler(){
        return controllerHandler;
    }
    protected static Api2DB api2DB = null;
    public static Api2DB getApi2DB() {
        return api2DB;
    }
    protected static ResultConfiguation resultConfiguation;

    public static void write(HttpServletRequest req, HttpServletResponse resp){
        resp.setCharacterEncoding(AtFrame.ENCODE);
        resp.setHeader("Content-type",AtFrame.JSON_CONTENT_TYPE);
        String group = req.getParameter("g");
        String res;
        PrintWriter pw = null;
        try{
            pw = resp.getWriter();
            if(filter != null){
                res = filter.accept(req,resp);
                if(!RequestConfigFilter.SUCCESS.equals(res)){
                    pw.write(res);
                    return ;
                }
            }
            res = RequestMappingHandlerMappingCustom.UrlAssemInfo.groupUrls.get(group == null || group.length() < 1 ? AutoCtrl.DEFAULT : group);
            pw.write(res == null || res.length() < 1 ? "" : res);
        } catch (IOException e) {
            LOG.error("error for get writer ",e);
        } finally {
            if(pw != null){
                pw.close();
            }
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if(o instanceof RequestConfigFilter){
            if(filter != null){
                throw new MultipartException("AutoUrlFilter 只能配置1个实例");
            }
            filter = (RequestConfigFilter) o;
            RequestMappingHandlerMappingCustom.ua.addReq(root,filter.addReq());
        }else if(o instanceof QueryExecutorFilter){
            if(resultSetFilter != null){
                throw new MultipartException("QueryExecutorFilter 只能配置1个实例");
            }
            resultSetFilter = (QueryExecutorFilter) o;
        }else if(o instanceof ControllerHandler){
            if(controllerHandler != null){
                throw new MultipartException("ControllerHandler 只能配置1个实例");
            }
            controllerHandler = (ControllerHandler) o;
        }else if(o instanceof Api2DB){
            if(api2DB != null){
                throw new MultipartException("Api2DB 只能配置1个实例");
            }
            api2DB = (Api2DB)o;
        }else if(o instanceof ResultConfiguation){
            if(resultConfiguation != null){
                throw new MultipartException("ResultConfiguation 只能配置1个实例");
            }
            resultConfiguation = (ResultConfiguation) o;
        }
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
       return o;
    }
}
