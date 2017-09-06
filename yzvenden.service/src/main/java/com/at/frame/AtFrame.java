package com.at.frame;

import com.at.frame.annotation.AutoCtrl;
import com.at.frame.entity.ReqInfo;
import com.at.frame.mybatis.QueryExecutorFilter;
import com.at.frame.plugin.BeanPostProcessorCustom;
import com.at.frame.plugin.RequestMappingHandlerMappingCustom;
import com.at.frame.properties.AtFrameProperties;
import com.at.frame.requestassem.RequestConfigServlet;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.*;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 自动化框架初始化
 * Created by Administrator on 2017/5/4.
 */
@Component
@Configurable
@AutoConfigureAfter
@EnableWebMvc
public class AtFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtFrame.class);
    private static final int[] EMPTY_IDS = new int[]{-1,-1};
    public static final String ENCODE = Charset.defaultCharset().displayName();
    public static final String JSON_CONTENT_TYPE = "application/json;charset=" + ENCODE;


    @Autowired
    private AtFrameProperties properties;
    //解析完成之后的控制器前缀内容
    private static HashMap<String,String> C;
    //静态化控制器方法前缀
    public static String M;

    @PostConstruct
    public void postConstruct(){
        String c = properties.getControllerAbbr();
        C = new HashMap<>();
        if(c.indexOf(":") != -1){
            C = new HashMap<>();
            String[] pkgs = c.split(";");
            for (String pkg : pkgs) {
                String[] info = pkg.split(":");
                C.put(info[0] , info[1]);
            }
        }
        M = "/" + properties.getMethodAbbr();
    }

    //-----------bean配置------------

    /**
     * 配置自动化控制器&方法数据请求地址
     * @return  接口请求数据
     */
    @Bean
    public ServletRegistrationBean requestUrlServlet(){
        return new ServletRegistrationBean(new RequestConfigServlet(),"/req.htm");
    }

    @Bean
    public ServletRegistrationBean swaggerApiServlet(){
        return new ServletRegistrationBean(new Swagger2Servlet(),"/api.htm");
    }

    /**
     * 处理控制器拦截
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "at.frame" ,name = "controller-handler")
    public DefaultPointcutAdvisor controllerHandler(){
        AspectJExpressionPointcut cut = new AspectJExpressionPointcut();

        String execution = properties.getControllerHandler();
        if(execution == null){
            return null;
        }
        cut.setExpression("execution(* " + execution + ".*.*(..))");
        Advice advice;
        advice = (MethodInterceptor) methodInvocation -> {
            AutoCtrl ctrl = methodInvocation.getThis().getClass().getAnnotation(AutoCtrl.class);
            if(ctrl == null){
                return methodInvocation.proceed();
            }
            ControllerHandler ch = BeanPostProcessorCustom.getControllerHandler();
            ReqInfo info = getReqInfo();
            if(ch != null && !ch.before(info)){
                return null;
            }
            try{
                Object obj = methodInvocation.proceed();
                if(ch != null && !ch.after(info)){
                    return null;
                }
                return obj;
            }catch (Throwable e){
                if(ch != null){
                   return ch.exception(e,info);
                }
            }
            return null;
        };
        return new DefaultPointcutAdvisor(cut,advice);
    }

    //--------------end bean 配置--------------

    private static final ThreadLocal<ReqInfo> REQINFOS = new ThreadLocal<>();
    private static ReqInfo getReqInfo(){
        ReqInfo info = REQINFOS.get();
        if(info == null){
            HttpServletRequest request = null;
            HttpServletResponse response = null;
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            if(ra != null){
                ServletRequestAttributes sra = (ServletRequestAttributes) ra;
                request = sra.getRequest();
                response = sra.getResponse();
            }else{
                return null;
            }
            info = new ReqInfo();
            info.setRequest(request);
            info.setResponse(response);
            String requestUrl = request.getRequestURI();
            int[] ids = RequestMappingHandlerMappingCustom.UrlAssemInfo.getIds(requestUrl);
            if(ids == null){
                LOGGER.error("url:[{}]在映射列表UrlAssemInfo.ids中不存在",requestUrl);
                ids = EMPTY_IDS;
            }
            info.setCtrlId(ids[0]);
            info.setMethodId(ids[1]);
            info.setUrl(requestUrl);
//            int indexOfAct = requestUrl.indexOf("!");
//            int indexOfMethod = requestUrl.indexOf(M);
//            info.ctrlId = Integer.parseInt(requestUrl.substring(indexOfAct+1,indexOfMethod));
//            info.methodId = Integer.parseInt(requestUrl.substring(indexOfMethod+2,requestUrl.indexOf("/v")));
        }
        return info;
    }

    /**
     * 用来处理查询的字段过滤
     * @return
     */
    public static HashSet<String> getResultSetFilter(){
        QueryExecutorFilter resultSetFilter = BeanPostProcessorCustom.getResultSetFilter();
        if(resultSetFilter != null){
            ReqInfo info = getReqInfo();
            return resultSetFilter.filterFields(info);
        }
        return null;
    }

    /**
     * 取得控制器前缀
     * @param pkg
     * @return
     */
    public static String getC(String pkg){
        String abbr = C.get(pkg);
        if(abbr == null || abbr.length() == 0) return "/c";
        return abbr;
    }

}
