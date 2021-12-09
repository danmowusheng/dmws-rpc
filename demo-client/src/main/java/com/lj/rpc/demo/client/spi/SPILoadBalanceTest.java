package com.lj.rpc.demo.client.spi;

import com.lj.rpc.common.extension.ExtensionLoader;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-09 15:19
 * @description：
 **/
public class SPILoadBalanceTest {
    public static void main(String[] args){
        ServiceLoader<Serializer> serviceLoader = ServiceLoader.load(Serializer.class);
        Iterator<Serializer> iterator = serviceLoader.iterator();
        while (iterator.hasNext()){
            Serializer serializer = iterator.next();
            System.out.println(serializer.getClass().getName());
        }
    }

    public static void main2(String[] args){
        ExtensionLoader<Serializer> loader = ExtensionLoader.getLoader(Serializer.class);
        Serializer serializer = loader.getExtension("protostuff");
        System.out.println(serializer.getClass().getName());
    }
}
