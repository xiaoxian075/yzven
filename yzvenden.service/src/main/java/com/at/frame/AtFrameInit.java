package com.at.frame;

import com.at.frame.annotation.AutoCtrl;
import com.at.frame.annotation.AutoMethod;
import com.at.frame.plugin.BeanPostProcessorCustom;
import com.at.frame.plugin.RequestMappingHandlerMappingCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/26.
 */
@Component
class AtFrameInit implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtFrameInit.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final Api2DB api2DB = BeanPostProcessorCustom.getApi2DB();
        if(api2DB != null){
            new Thread(()->{
                long s = System.currentTimeMillis();
                api2DB.beforeSave();
                Map<AutoCtrl, List<AutoMethod>> autoInfos = RequestMappingHandlerMappingCustom.UrlAssemInfo.getAutoInfos();
                Set<AutoCtrl> ctrls = autoInfos.keySet();
                for (AutoCtrl ctrl : ctrls){
                    List<AutoMethod> methods = autoInfos.get(ctrl);
                    if(!ctrl.api2Db()) continue;
                    api2DB.save(ctrl,methods);
                }
                long e = System.currentTimeMillis();
                LOGGER.info("api2db finished. used time : {}ms",(e-s));
            }).start();
        }
    }
}
