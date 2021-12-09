package com.lj.rpc.core.registry;

import com.lj.rpc.common.constants.URLKeyConstants;
import com.lj.rpc.common.extension.Adaptive;
import com.lj.rpc.common.extension.SPI;
import com.lj.rpc.common.url.URL;

/**
 * 注册中心工厂
 * 注解后面为什么还要提供值？这个地方跟作者不太一样
 */
@SPI
public interface RegistryFactory {
    /**
     * 获取注册中心实例
     * @param url 注册中心的配置，例如注册中心的地址。会自动根据协议获取注册中心实例
     * @return 如果协议类型跟注册中心匹配上了，返回对应的配置中心实例
     */
    @Adaptive(URLKeyConstants.PROTOCOL)
    public Registry getRegistry(URL url);
}
