package com.lj.rpc.proxy;

import cn.hutool.core.util.StrUtil;
import com.lj.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-28 16:27
 * @description： 服务缓存，对于HashMap的封装
 **/
@Slf4j
public class RpcServiceCache {
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    /**
     * 增加服务
     * @param version 版本
     * @param service 服务
     * @throws RpcException
     */
    public static void addService(String version, Object service) throws RpcException {
        /**
         * TODO:下面这一段代码待进一步掌握
         */
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0){
            throw new RpcException("add service error. service not implements interface. service=" + service.getClass().getName());
        }
        String rpcServiceName;
        if (StrUtil.isBlank(version)){
            rpcServiceName = interfaces[0].getCanonicalName();
        }else {
            rpcServiceName = interfaces[0].getCanonicalName() + "_" + version;
        }
        serviceMap.putIfAbsent(rpcServiceName, service);
        log.info(StrUtil.format("add service. rpcServiceName={}, class={}", rpcServiceName, service.getClass()));
    }

    /**
     * 获取服务
     * @param rpcServiceName rpc服务名称
     * @return 对应服务
     * @throws RpcException
     */
    public static Object getService(String rpcServiceName) throws RpcException {
        Object service = serviceMap.get(rpcServiceName);
        if (service == null){
            throw new RpcException("rpcService not found." + rpcServiceName);
        }
        return service;
    }
}
