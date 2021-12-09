package com.lj.rpc.core.registry;

import com.lj.rpc.common.url.URL;

import java.util.List;

/**
 * 注册中心
 */
public interface Registry {
    /**
     * 向注册中心注册服务
     * @param url 注册服务的信息
     */
    void register(URL url);

    /**
     * 向服务中心删除该服务
     * @param url 要删除的服务信息
     */
    void unregister(URL url);

    /**
     * 查找注册的服务
     * @param condition 查询条件
     * @return 符合查询条件的所有注册者
     */
    List<URL> lookUp(URL condition);

    /**
     * 取消本机所有服务，用于关机时的操作
     */
    void unregisterAllMyServices();
}
