package com.lj.rpc.core.spring;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.NetUtil;
import com.lj.rpc.core.annotation.RpcReference;
import com.lj.rpc.core.annotation.RpcService;
import com.lj.rpc.common.constants.URLKeyConstants;
import com.lj.rpc.common.extension.ExtensionLoader;
import com.lj.rpc.common.url.URL;
import com.lj.rpc.core.config.ConfigManager;
import com.lj.rpc.core.config.RegistryConfig;
import com.lj.rpc.core.config.ServiceConfig;
import com.lj.rpc.core.registry.Registry;
import com.lj.rpc.core.proxy.RpcClientProxy;
import com.lj.rpc.core.proxy.RpcServiceCache;
import com.lj.rpc.core.registry.RegistryFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-07 16:24
 * @description：实例化时。
 *  服务提供方：注册到注册中心
 *  服务调用方：生成 RPC 代理类
 **/
@Slf4j
@Component
public class ServiceBeanPostProcessor implements BeanPostProcessor {

    @SneakyThrows
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        //RPC服务需要发布到服务中心
        if (rpcService != null){
            RegistryFactory registryFactory = ExtensionLoader.getLoader(RegistryFactory.class).getAdaptiveExtension();
            RegistryConfig registryConfig = ConfigManager.getInstance().getRegistryConfig();
            Registry registry = registryFactory.getRegistry(registryConfig.toURL());
            registry.register(buildServiceURL(bean, rpcService));
            // 然后把服务放到缓存中，方便后续通过 rpcServiceName 获取服务
            RpcServiceCache.addService(rpcService.version(), bean);
        }

        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException{
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields){
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null){
                //生成代理对象
                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcReference);
                Object proxy = rpcClientProxy.getProxy(field.getType());
                field.setAccessible(true);
                try {
                    //设置字段
                    field.set(bean, beanName);
                }catch (IllegalAccessException e){
                    log.error("field.set error. com.lj.rpc.demo.client.api.bean={}, field={}", bean.getClass(), field.getName(), e);
                }
            }
        }
        return bean;
    }

    /**
     * 构建服务的URL
     * @param bean 类实例
     * @param rpcService 类上的 @RpcService 注解
     * @return 带有服务信息的URL
     */
    private URL buildServiceURL(Object bean, RpcService rpcService) {
        Map<String, String> param = MapUtil.<String, String>builder()
                .put(URLKeyConstants.INTERFACE, bean.getClass().getInterfaces()[0].getCanonicalName())
                .put(URLKeyConstants.VERSION, rpcService.version()).build();
        ServiceConfig serviceConfig = ConfigManager.getInstance().getServiceConfig();

        return URL.builder().protocol("ccx-rpc")
                .host(NetUtil.getLocalhostStr())
                .port(serviceConfig.getPort())
                .params(param).build();
    }
}
