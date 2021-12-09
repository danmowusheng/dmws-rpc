package com.lj.rpc.demo.client;

import com.lj.rpc.core.annotation.RpcScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-07 20:58
 * @description： 启动类
 **/
@SpringBootApplication
@RpcScan(basePackages = {"com.lj.rpc.demo.client"})
public class ClientBootstrap {
    //TODO:basePackages后续可能需要更改
    public static void main(String[] args){
        SpringApplication.run(ClientBootstrap.class, args);
    }
}
