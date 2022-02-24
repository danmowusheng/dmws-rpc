package com.dmws.rpc.core.proxy;

import com.dmws.rpc.common.extension.ExtensionLoader;
import com.dmws.rpc.core.faulttolerant.FaultTolerantInvoker;
import com.dmws.rpc.core.annotation.RpcReference;
import com.dmws.rpc.core.config.ConfigManager;
import com.dmws.rpc.core.domain.RpcRequest;
import com.dmws.rpc.core.domain.RpcResponse;
import com.dmws.rpc.core.domain.RpcResult;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-03 15:45
 * @description： RRC调用方的代理
 **/
public class RpcClientProxy implements InvocationHandler {

    private final RpcReference rpcReference;

    public RpcClientProxy(RpcReference rpcReference) {
        this.rpcReference = rpcReference;
    }

    /**
     * 获取代理类
     * @param clazz 需要代理的接口类
     * @param <T> 类型
     * @return  代理类
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        //TODO: 缓存，不然会生成很多代理类
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }


    @Override
    @SneakyThrows   //这个注解是干嘛用的？
    public Object invoke(Object proxy, Method method, Object[] args){
        //调用netty发请求
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getCanonicalName())
                .methodName(method.getName())
                .params(args)
                .paramTypes(method.getParameterTypes())
                .version(rpcReference.version())
                .build();
        ExtensionLoader<FaultTolerantInvoker> loader = ExtensionLoader.getLoader(FaultTolerantInvoker.class);
        FaultTolerantInvoker invoker = loader.getExtension(ConfigManager.getInstance().getClusterConfig().getFaultTolerant());
        RpcResult rpcResult = invoker.invoke(request);
        return ((RpcResponse<?>)rpcResult.getData()).getData();
    }
}
