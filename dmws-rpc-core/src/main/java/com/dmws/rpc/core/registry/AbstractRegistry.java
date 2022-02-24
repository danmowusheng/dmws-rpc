package com.dmws.rpc.core.registry;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.lang.Assert;
import com.dmws.rpc.common.constants.URLKeyConstants;
import com.dmws.rpc.common.url.URL;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.dmws.rpc.core.registry.RegistryEvent.Type.*;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-25 15:20
 * @description： 抽象的注册中心
 **/
@Slf4j
public abstract class AbstractRegistry implements Registry{

    /**
     * 已注册的服务的本地缓存。{serviceName: [URL]}
     * 两个目的：
     * 1.减少注册中心的压力，减少请求，一部分请求可以直接走缓存
     * 2.提高可用性，当注册中心挂掉了，本地缓存（Map）还能提供服务信息
     */
    private final Map<String, Set<String>> registered = new ConcurrentHashMap<>();

    /**
     * 本机已经注册的服务
     * TO DO：对于这个字段感到疑惑，不是有本地缓存了吗，这个用来干吗？
     */
    private static final Set<URL> myServiceURL = new ConcurrentHashSet<>();

    /**
     * 向注册中心注册服务
     * @param url 注册服务的信息
     */
    protected abstract void doRegister(URL url);

    /**
     * 查找服务
     * @param condition 查找条件
     * @return 所有服务查找条件的服务
     */
    protected abstract List<URL> doLookUp(URL condition);

    /**
     * 在注册中心中删除该服务
     * @param url 该删除服务信息
     */
    protected abstract void doUnregister(URL url);

    /**
     * 在注册中心注册服务
     * @param url 注册服务的信息
     */
    @Override
    public void register(URL url) {
        Assert.notNull(url);
        doRegister(url);
        addToLocalCache(url);
        myServiceURL.add(url);
        log.info("register:{}", url);
    }

    /**
     * 在注册中心删除该服务
     * @param url 要删除的服务信息
     */
    @Override
    public void unregister(URL url){
        Assert.notNull(url);
        doUnregister(url);
        removeFromLocalCache(url);
        myServiceURL.remove(url);
        log.info("unregister:{}", url);
    }

    /**
     * 在注册中心查找服务
     * @param condition 查询条件
     * @return
     */
    @Override
    public List<URL> lookUp(URL condition) {
        String serviceName = getServiceNameFromUrl(condition);
        //如果本地有，则直接从本地取出
        if(registered.containsKey(serviceName)){
            return registered.get(serviceName).stream().map(URL::valueOf).collect(Collectors.toList());
        }
        //若本地没有，则重置缓存
        List<URL> services = reset(condition);
        log.info("look up:{}", services);
        return services;
    }

    /**
     * 在本机关机时删除所有服务
     */
    @Override
    public void unregisterAllMyServices(){
        log.info("unregister all my services:{}", myServiceURL);
        for (URL service:myServiceURL){
            unregister(service);
        }
    }

    /**
     * 从 URL 中获取服务名
     */
    public String getServiceNameFromUrl(URL url) {
        return url.getParam(URLKeyConstants.INTERFACE, url.getPath());
    }

    /**
     * 重置,真实拿出注册信息，然后加到缓存中。
     * @param condition 更新的服务信息
     * @return 更新的服务列表信息
     */
    public List<URL> reset(URL condition){
        //获取服务名
        String serviceName = getServiceNameFromUrl(condition);
        //清除本地缓存
        registered.remove(serviceName);
        //从注册中心拿到服务信息
        List<URL> services = doLookUp(condition);
        for (URL service : services){
            addToLocalCache(service);
        }
        log.info("result:{}", services);
        return services;
    }

    /**
     * 触发事件
     *
     * @param event 事件
     */
    public final void triggerEvent(RegistryEvent event) {
        RegistryEvent.Type type = event.getType();
        String data = event.getData();
        String oldData = event.getOldData();
        log.info("triggerEvent. event={}", event);
        if (type == CREATED) {
            // 新增节点
            if (data != null) {
                addToLocalCache(URL.valueOf(data));
            }
        } else if (type == DELETED) {
            if (oldData != null) {
                // 删除节点
                removeFromLocalCache(URL.valueOf(oldData));
            }
        } else if (type == CHANGED) {
            // 修改节点
            if (oldData != null) {
                removeFromLocalCache(URL.valueOf(oldData));
            }
            if (data != null) {
                addToLocalCache(URL.valueOf(data));
            }
        }
    }

    /**
     * 添加服务到本地缓存
     * @param service
     */
    public void addToLocalCache(URL service){
        String serviceName = getServiceNameFromUrl(service);
        if(!registered.containsKey(serviceName)){
            registered.put(serviceName, new ConcurrentHashSet<>());
        }
        registered.get(serviceName).add(service.toFullString());
    }

    /**
     * 在本地缓存删除该服务
     * @param service
     */
    public void removeFromLocalCache(URL service){
        String serviceName = getServiceNameFromUrl(service);
        if (registered.containsKey(serviceName)){
            registered.get(serviceName).remove(service.toFullString());
        }
    }

}
