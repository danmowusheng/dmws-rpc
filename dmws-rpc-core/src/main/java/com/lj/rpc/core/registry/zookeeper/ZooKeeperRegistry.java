package com.lj.rpc.core.registry.zookeeper;


import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.net.URLEncoder;
import com.lj.rpc.common.constants.RegistryConstants;
import com.lj.rpc.common.url.URL;
import com.lj.rpc.common.url.URLParser;
import com.lj.rpc.core.registry.AbstractRegistry;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-25 16:26
 * @description： zookeeper注册中心，引入了Curator客户端
 **/
@Slf4j
public class ZooKeeperRegistry extends AbstractRegistry {

    private final CuratorZkClient client;
    private static final URLEncoder urlEncoder = URLEncoder.createPathSegment();
    private static final Charset charset = Charset.defaultCharset();

    public ZooKeeperRegistry(URL url){
        this.client = new CuratorZkClient(url);
    }

    @Override
    protected void doRegister(URL url) {
        client.createEphemeralNode(toUrlPath(url));
        watch(url);
    }

    @Override
    protected List<URL> doLookUp(URL condition) {
        List<String> children = client.getChildren(toServicePath(condition));
        //流操作
        List<URL> services = children.stream()
                .map(s -> URLParser.toURL(URLDecoder.decode(s,charset)))
                .collect(Collectors.toList());
        //为获取到的每个URL添加监听
        for(URL service : services){
            watch(service);
        }
        return services;
    }

    @Override
    protected void doUnregister(URL url) {
        client.removeNode(toUrlPath(url));
        watch(url);
    }



    /**
     * 转成全路径，包括节点内容
     */
    private String toUrlPath(URL url) {
        return toServicePath(url) + "/" + urlEncoder.encode(url.toFullString(), charset);
    }

    /**
     * 转成服务的路径，例如：/ccx-rpc/com.ccx.rpc.demo.service.com.lj.rpc.demo.client.api.UserService/providers
     */
    private String toServicePath(URL url) {
        return getServiceNameFromUrl(url) + "/" + RegistryConstants.PROVIDERS_CATEGORY;
    }

    /**
     * 监听
     */
    private void watch(URL url) {
        String path = toServicePath(url);
        client.addListener(path, (type, oldData, data) -> {
            log.info("watch event. type={}, oldData={}, data={}", type, oldData, data);
            reset(url);
        });
    }
}
