package com.at.frame;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.alibaba.fastjson.JSON;
//import com.at.entity.TSystemUser;
import com.at.frame.utils.Result;
import org.springframework.stereotype.Component;

@Component
public class Runtimes {

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(3);
    private static final RuntimeMXBean runtimeMXBean;
    private static final int runtimeId;

    static{
        runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String vmName = runtimeMXBean.getVmName();//虚拟机名称
        String vmVersion = runtimeMXBean.getVmVersion();//虚拟机版本
        String name = runtimeMXBean.getName();//虚拟机的运行名称
        //解析得到虚拟机运行ID
        runtimeId = Integer.valueOf(name.substring(0,name.indexOf("@")));

//        EXECUTOR.execute(()->{
//            for(;;){//用来获取GC信息
//                try {
//                    Thread.currentThread().sleep(5000);
//                } catch (InterruptedException e) {
//
//                }
//                List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
//                for(int i=0,s=garbageCollectorMXBeans.size();i<s;i++){
//                    GarbageCollectorMXBean gc = garbageCollectorMXBeans.get(i);
//                    System.out.println(runtimeId + "," + Arrays.toString(gc.getMemoryPoolNames()) + "," + gc.getCollectionCount() + "," + gc.getCollectionTime());
//                }
//            }
//        });

    }


}