package com.at.frame.plugin;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.at.frame.utils.Result;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */
@Component
public class WebMvcConfigurerAdapterCustom extends WebMvcConfigurerAdapter{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger2/**")
                .addResourceLocations("classpath:/com/at/frame/swagger2/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter(){
            @Override
            public void write(Object t, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                super.write(t, type, contentType, outputMessage);
            }

            @Override
            protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                Object data;
                if(BeanPostProcessorCustom.resultConfiguation != null){
                    data = BeanPostProcessorCustom.resultConfiguation.dataEncrypt(obj);
                }else{
                    data = obj;
                }
                super.writeInternal(data, outputMessage);
                if(obj instanceof Result){
                    ((Result)obj).close();
                }
            }
        };
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat
        );
        converters.add(converter);
    }

}
