package com.lj.rpc.core.spring;

import cn.hutool.core.util.StrUtil;
import com.lj.rpc.core.annotation.RpcScan;
import com.lj.rpc.core.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//通过添加依赖解决import问题
import javax.annotation.Resource;
import java.util.Map;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-03 15:18
 * @description： RPC扫描注册
 **/
@Slf4j
public class RpcScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    /**
     * 服务扫描的基础包，是 @RpcScan 的哪个属性
     */
    private static final String SERVER_SCANNER_BASE_PACKAGE_FIELD = "basePackages";

    /**
     * 内部扫描的基础包列表
     */
    private static final String[] INNER_SCANNER_BASE_PACKAGES = {"com.lj.rpc.common", "com.lj.rpc.core"};

    private ResourceLoader resourceLoader;


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //扫描注解
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(RpcScan.class.getName());

        RpcScanner serviceScanner = new RpcScanner(registry, RpcService.class);
        //TODO: ccx-rpc 内部的类，需要弄明白
        //存在问题的是这个Resource.class,为什么我找不到引用呢？
        RpcScanner innerScanner = new RpcScanner(registry, Component.class, Service.class, Resource.class);

        if (resourceLoader != null){
            serviceScanner.setResourceLoader(resourceLoader);
            innerScanner.setResourceLoader(resourceLoader);
        }

        String[] serviceBasePackages = (String[]) annotationAttributes.get(SERVER_SCANNER_BASE_PACKAGE_FIELD);
        int serviceCount = serviceScanner.scan(serviceBasePackages);
        log.info(StrUtil.format("serviceScanner. packages={}, count={}", serviceBasePackages, serviceCount));
        int innerCount = innerScanner.scan(INNER_SCANNER_BASE_PACKAGES);
        log.info(StrUtil.format("innerScanner. packages={}, count={}", INNER_SCANNER_BASE_PACKAGES, innerCount));
    }
}
