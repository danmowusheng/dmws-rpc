package com.dmws.rpc.demo.server;

import com.dmws.rpc.core.annotation.RpcScan;
import com.dmws.rpc.core.io.server.NettyServerBootstrap;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-09 14:57
 * @description： 服务启动类
 * TODO:扫描路径需要注意
 **/
@SpringBootApplication
@RpcScan(basePackages = {"com.dmws.rpc.demo.server"})
public class ServiceBootstrap {
    public static void main(String[] args){
        /**
         * 在此过程通过SPI载入META-INF.dmws-rpc中所有配置的类文件
         */
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ServiceBootstrap.class);
        NettyServerBootstrap serverBootstrap =  (NettyServerBootstrap)applicationContext.getBean("nettyServerBootstrap");
        serverBootstrap.start();
    }
}
