package com.at.frame.plugin;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Administrator on 2017/5/15.
 */
@Component
@EnableSwagger2
@EnableWebMvc
@Configurable
@ConditionalOnProperty(prefix = "at.frame" ,name = "swagger" ,havingValue = "true")
public class Swagger2ApiConfig {


}
