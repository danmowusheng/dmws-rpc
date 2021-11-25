package com.lj.rpc.registry.local;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.lj.rpc.common.url.URL;
import com.lj.rpc.registry.AbstractRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-25 19:23
 * @description： 本地注册中心，用于测试
 **/
@Slf4j
public class LocalRegistry extends AbstractRegistry {

    private static final Set<URL> urls = new ConcurrentHashSet<>();

    /**
     * 具体注册实现，这里只打印信息
     * @param url 注册服务的信息
     */
    @Override
    protected void doRegister(URL url) {
        urls.add(url);
        log.info("register:{}",url);
    }

    @Override
    protected List<URL> doLookUp(URL condition) {
        log.info("do lookup:{}", urls);
        return new ArrayList<>(urls);
    }

    @Override
    protected void doUnregister(URL url) {
        urls.remove(url);
        log.info("unregister:{}", url);
    }
}
