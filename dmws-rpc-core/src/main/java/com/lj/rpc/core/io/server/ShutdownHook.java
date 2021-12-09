package com.lj.rpc.core.io.server;

import com.lj.rpc.common.extension.ExtensionLoader;
import com.lj.rpc.core.registry.RegistryFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-28 14:48
 * @description： 关闭的钩子?这啥意思
 **/
@Slf4j
public class ShutdownHook {

    public static void addShutdownHook(){
        log.info("add ShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            RegistryFactory registryFactory = ExtensionLoader.getLoader(RegistryFactory.class).getAdaptiveExtension();
            /**
             * TODO: 后续需要另外的一些类才能完成
             */
        }));
    }

}
