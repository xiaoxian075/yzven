package com.at.frame.plugin;

import com.at.frame.annotation.AutoCtrl;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Tags;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.contexts.ApiListingContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;


/**
 * Created by Administrator on 2017/5/16.
 */
@Component
@ConditionalOnProperty(prefix = "at.frame" ,name = "swagger" ,havingValue = "true")
public class Swagger2DocumentPlugin implements ApiListingBuilderPlugin {

    @Override
    public void apply(ApiListingContext apiListingContext) {
        Class controllerClass = apiListingContext.getResourceGroup().getControllerClass();
        Optional apiAnnotation = Optional.fromNullable(AnnotationUtils.findAnnotation(controllerClass, Api.class));
        AutoCtrl autoCtrl;
        String description;
        if(!apiAnnotation.isPresent() && (autoCtrl = (AutoCtrl)controllerClass.getAnnotation(AutoCtrl.class)) != null){
            //没有配置Api，则使用AutoCtrl
            StringBuilder info = new StringBuilder("{ ");
            info.append("id ：").append(autoCtrl.id())
                    .append(" , name : [ ");
            String[] names = autoCtrl.name();
            if(names != null && names.length > 0){
                for(String name : names){
                    info.append("\"").append(name).append("\" , ");
                }
            }
            info.setLength(info.length()-2);
            info.append(" ] , prefix : \"").append(autoCtrl.prefix()).append("\"");
            info.append(" desc : \"").append(autoCtrl.desc()).append("\"");
            info.append(" version : ").append(autoCtrl.version()).append(" }");
            description = info.toString();
        }else{
            description = Strings.emptyToNull((String)apiAnnotation.transform(this.descriptionExtractor()).orNull());
        }
        Set tagSet = (Set)apiAnnotation.transform(this.tags()).or(Sets.newTreeSet());
        if(tagSet.isEmpty()) {
            tagSet.add(apiListingContext.getResourceGroup().getGroupName());
        }
        apiListingContext.apiListingBuilder().description(description).tagNames(tagSet);
    }


    private Function<Api, String> descriptionExtractor() {
        return new Function<Api,String>() {

            public String apply(Api input) {
                return input.description();
            }
        };
    }

    private Function<Api, Set<String>> tags() {
        return new Function<Api,Set<String>>() {
            public Set<String> apply(Api input) {
                return Sets.newTreeSet(FluentIterable.from(Lists.newArrayList(input.tags())).filter(Tags.emptyTags()).toSet());
            }
        };
    }

    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}

