package com.at.frame.plugin;

import com.at.frame.annotation.AutoCtrl;
import com.at.frame.annotation.AutoMethod;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * Created by Administrator on 2017/5/16.
 */
@Component
@ConditionalOnProperty(prefix = "at.frame" ,name = "swagger" ,havingValue = "true")
public class Swagger2OperationBuilderPlugin implements OperationBuilderPlugin{

    @Override
    public void apply(OperationContext context) {

        Optional methodAnnotation = context.findAnnotation(AutoMethod.class);
        Optional apiAnnotaion = context.findAnnotation(ApiOperation.class);
        if(methodAnnotation.isPresent() && !apiAnnotaion.isPresent()) {
            AutoMethod autoMethod = (AutoMethod) methodAnnotation.get();
            Optional ctrlAnnotation = context.findControllerAnnotation(AutoCtrl.class);
            String prefix = "";
            if(ctrlAnnotation.isPresent()){
                AutoCtrl autoCtrl = (AutoCtrl) ctrlAnnotation.get();
                prefix = autoCtrl.prefix();
            }
            String[] values = autoMethod.value();
            StringBuilder summary = new StringBuilder("[ ");
            for(String val : values){
                if(val == null || val.length()== 0) continue;
                if(prefix != null && prefix.length() > 0){
                    val = prefix + StringUtils.capitalize(val);
                }
                summary.append(val).append(" , ");
            }
            summary.setLength(summary.length()-2);
            summary.append(" ] ").append(autoMethod.desc());
            context.operationBuilder().summary(summary.toString());
            summary.setLength(0);
            summary.append("方法Id：").append(autoMethod.id()).append("<br />");
            summary.append("版本号：").append(autoMethod.version()).append("<br />");
            String[] apis = autoMethod.api();
            if(apis != null && apis.length > 0){
                for(String api : apis){
                    summary.append(api).append("<br />");
                }
            }
            context.operationBuilder().notes(summary.toString());
        }

    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
