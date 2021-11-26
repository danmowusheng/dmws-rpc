package com.lj.rpc.registry.local;

import com.lj.rpc.common.url.URL;
import com.lj.rpc.registry.Registry;
import com.lj.rpc.registry.RegistryFactory;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-25 19:22
 * @description： 本地注册中心工厂
 **/
public class LocalRegistryFactory implements RegistryFactory {
    @Override
    public Registry getRegistry(URL url) {
        return null;
    }
}
