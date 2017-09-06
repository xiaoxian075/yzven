package com.at.frame.plugin;

import com.at.frame.AtFrame;
import com.at.frame.annotation.AutoCtrl;
import com.at.frame.annotation.AutoMethod;
import com.at.frame.utils.StringCache;
import com.at.frame.utils.StringUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ResponseHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Administrator on 2017/5/10.
 * 自定义的
 * RequestMappingHandlerMapping
 */
@Component
@AutoConfigureAfter
@Order(Ordered.LOWEST_PRECEDENCE - 12)
public class RequestMappingHandlerMappingCustom extends RequestMappingHandlerMapping {

    protected static UrlAssem ua = new UrlAssem();//url装配类
    private Map<String,RequestMapping> ctrlBuilded = new HashMap<>();

    @Value("${at.frame.url:http://localhost}")
    private String root;
    @Value("${at.frame.url-id:true}")
    private boolean urlId;

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = this.createRequestMappingInfo(method);
        if(info != null) {
            RequestMappingInfo typeInfo = this.createRequestMappingInfo(handlerType);
            if(typeInfo != null) {
                info = typeInfo.combine(info);
            }
        }
        return info;
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        boolean isClass = element instanceof Class;
        RequestCondition condition = null;
        RequestMapping newRM = null;
        if(isClass){
            Class clazz = (Class)element;
            condition = super.getCustomTypeCondition(clazz);
            newRM = ctrlBuilded.get(clazz.getName());
            if(newRM == null){
                final AutoCtrl autoCtrl = AnnotatedElementUtils.findMergedAnnotation(element, AutoCtrl.class);
                if(autoCtrl != null){
                    final String cAbbr = AtFrame.getC(clazz.getPackage().getName())+"!";
                    newRM = new RequestMapping(){
                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return RequestMapping.class;
                        }

                        @Override
                        public String name() {
                            return "";
                        }

                        @Override
                        public String[] value() {
                            return new String[0];
                        }

                        @Override
                        public String[] path() {
                            if(urlId){
                                return new String[]{cAbbr + autoCtrl.id()};
                            }else{
                                String[] names = autoCtrl.name();
                                String[] paths = new String[names.length];
                                StringBuilder builder = new StringBuilder(cAbbr);
                                int len = cAbbr.length();
                                for(int  i = 0, l = names.length; i < l; i++){
                                    paths[i] = builder.append(names[i]).toString();
                                    builder.setLength(len);
                                }
                                builder = null;
                                return paths;
                            }
                        }

                        @Override
                        public RequestMethod[] method() {
                            return new RequestMethod[0];
                        }

                        @Override
                        public String[] params() {
                            return new String[0];
                        }

                        @Override
                        public String[] headers() {
                            return new String[0];
                        }

                        @Override
                        public String[] consumes() {
                            return new String[0];
                        }

                        @Override
                        public String[] produces() {
                            return new String[0];
                        }
                    };
                    ctrlBuilded.put(clazz.getName(),newRM);
                    Class assemClazz;
                    int ctrlId = autoCtrl.id();
                    if((assemClazz = ua.setCtrlId(Integer.valueOf(ctrlId),clazz)) != null){
                        throw new MultipartException("控制器[" + clazz + "]与[" + assemClazz + "]配置了相当的控制器Id,Id值：" + ctrlId);
                    }
                    StringBuilder requestUrl = new StringBuilder();
                    String[] ctrlPaths = newRM.path();
                    List<AutoMethod> autoMethods = new ArrayList<>();
                    for(String ctrlPath : ctrlPaths){
                        requestUrl.append(ctrlPath);
                        int index = requestUrl.length();
                        String[] ctrlNames = autoCtrl.name();//组别
                        String ctrlPrefix = autoCtrl.prefix();
                        boolean methodPrefix = ctrlPrefix != null && ctrlPrefix.length() > 0;
                        Method[] methods = clazz.getDeclaredMethods();
                        for (Method method : methods) {
                            AutoMethod autoMethod = method.getAnnotation(AutoMethod.class);
                            if (autoMethod == null) {
                                continue;
                            }
                            autoMethods.add(autoMethod);
                            int methodId = autoMethod.id();//方法id
                            String[] methodNames = autoMethod.name();//方法名称
                            String desc = autoMethod.desc();
                            int version = autoMethod.version();
                            for (String ctrlName : ctrlNames) {
                                if (ctrlName == null || ctrlName.length() == 0) continue;
                                for (String methodName : methodNames) {
                                    if (methodName == null || methodName.length() == 0) continue;
                                    requestUrl.append(AtFrame.M).append(urlId ? methodId : methodName);
                                    if (methodPrefix) {
                                        methodName = ctrlPrefix + StringUtils.capitalize(methodName);
                                    }
                                    requestUrl.append("/v").append(version);
                                    ua.add(root,ctrlId,methodId,ctrlName, methodName, desc, requestUrl.toString());
                                    requestUrl.setLength(index);
                                }
                            }
                        }
                    }
                    ua.saveInfo(autoCtrl,autoMethods);
                }
            }
        }else{
            condition = super.getCustomMethodCondition((Method)element);
            final AutoMethod autoMethod = AnnotatedElementUtils.findMergedAnnotation(element, AutoMethod.class);
            if(autoMethod != null){
                newRM = new RequestMapping(){
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return RequestMapping.class;
                    }

                    @Override
                    public String name() {
                        return "";
                    }

                    @Override
                    public String[] value() {
                        return new String[0];
                    }

                    @Override
                    public String[] path() {
                        if(urlId){
                            return new String[]{AtFrame.M + autoMethod.id() + "/v" + autoMethod.version()};
                        }else{
                            String[] names = autoMethod.name();
                            String[] paths = new String[names.length];
                            StringBuilder builder = new StringBuilder(AtFrame.M);
                            int len = AtFrame.M.length();
                            int version = autoMethod.version();
                            for(int  i = 0, l = names.length; i < l; i++){
                                paths[i] = builder.append(names[i]).append("/v").append(version).toString();
                                builder.setLength(len);
                            }
                            builder = null;
                            return paths;
                        }
                    }

                    @Override
                    public RequestMethod[] method() {
                        return autoMethod.method();
                    }

                    @Override
                    public String[] params() {
                        return new String[0];
                    }

                    @Override
                    public String[] headers() {
                        return new String[0];
                    }

                    @Override
                    public String[] consumes() {
                        return new String[0];
                    }

                    @Override
                    public String[] produces() {
                        return new String[0];
                    }
                };
            }


        }
        if(newRM == null){
            newRM = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        }
        return newRM == null ? null : super.createRequestMappingInfo(newRM, condition);
    }


    public static class UrlAssemInfo{
        public static Map<String,String> groupUrls = new HashMap<>();
        private static Map<String,int[]> urlIds;
        static {
            ua.toReqUrlConfig(groupUrls);
            urlIds = ua.urlIds;
        }
        public static int[] getIds(String url){
            return urlIds.get(url);
        }
        public static Map<AutoCtrl,List<AutoMethod>> getAutoInfos(){
            return ua.autoInfos;
        }
    }

    protected static class UrlAssem{
        Map<String,StringCache> urls = new HashMap<>();
        Map<Integer,Class> ctrlIds = new HashMap<>();
        Map<AutoCtrl,List<AutoMethod>> autoInfos = new HashMap<>();
        Map<String,int[]> urlIds = new HashMap<>();

        Class setCtrlId(Integer ctrlId,Class clazz){
            if(ctrlIds.containsKey(ctrlId)){
                return ctrlIds.get(ctrlId);
            }
            ctrlIds.put(ctrlId,clazz);
            return null;
        }

        void saveInfo(AutoCtrl autoCtrl,List<AutoMethod> autoInfos){
            this.autoInfos.put(autoCtrl,autoInfos);
        }


        void add(String root,int ctrlId,int methodId,String group,String val,String desc,String url){
            StringCache sc = urls.get(group);
            if(sc == null){
                sc = new StringCache("{");
                urls.put(group,sc);
            }
            sc.append("\"").append(val).append("\"")
                    .append(":\"").append(root).append(url).append("\"");
            sc.append(",");
            urlIds.put("/" + url,new int[]{ctrlId,methodId});
        }
        synchronized void toReqUrlConfig(Map<String,String> toUrl){
            if(urls == null) return ;
            Set<String> keys = urls.keySet();
            for(String key : keys){
                StringCache sc = urls.get(key);
                sc.deleteLast().append("}");
                toUrl.put(key,sc.toString());
            }
            if(urls != null) urls.clear();
            urls = null;

            if(ctrlIds != null) ctrlIds.clear();
            ctrlIds = null;
        }

        public void addReq(String root,Map<String, String> reqs) {
            if(reqs == null || reqs.size() < 1) return ;
            StringCache sc = new StringCache();
            reqs.forEach((k,v)->{
                urls.forEach((group,url)->{
                    url.append("\"").append(k).append("\":\"").append(root == null ? "" : root).append(v).append("\",");
                });
            });
        }
    }
}
