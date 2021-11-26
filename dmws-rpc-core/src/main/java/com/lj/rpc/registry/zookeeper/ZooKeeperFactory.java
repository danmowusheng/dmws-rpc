package com.lj.rpc.registry.zookeeper;

import com.lj.rpc.common.url.URL;
import com.lj.rpc.registry.Registry;
import com.lj.rpc.registry.RegistryFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-25 16:25
 * @description： zookeeper注册中心工厂
 **/
public class ZooKeeperFactory implements RegistryFactory {

    private static final Map<URL, ZooKeeperRegistry> cache = new ConcurrentHashMap<>();

    /**
     * 获取注册中心实例
     * @param url 注册中心的配置，例如注册中心的地址。会自动根据协议获取注册中心实例
     * @return
     */
    @Override
    public Registry getRegistry(URL url) {
        if (cache.containsKey(url)){
            return cache.get(url);
        }
        //否则新建注册中心
        ZooKeeperRegistry zooKeeperRegistry = new ZooKeeperRegistry(url);
        cache.putIfAbsent(url, zooKeeperRegistry);
        return cache.get(url);
    }
}
