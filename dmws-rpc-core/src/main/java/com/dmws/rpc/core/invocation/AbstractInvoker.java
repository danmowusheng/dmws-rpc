package com.dmws.rpc.core.invocation;

import cn.hutool.core.collection.CollectionUtil;
import com.dmws.rpc.common.constants.URLKeyConstants;
import com.dmws.rpc.common.extension.ExtensionLoader;
import com.dmws.rpc.common.url.URL;
import com.dmws.rpc.common.url.URLBuilder;
import com.dmws.rpc.core.loadBalance.LoadBalance;
import com.dmws.rpc.core.registry.Registry;
import com.dmws.rpc.core.config.ConfigManager;
import com.dmws.rpc.core.domain.RpcRequest;
import com.dmws.rpc.core.domain.RpcResult;
import com.dmws.rpc.core.exception.RpcException;
import com.dmws.rpc.core.registry.RegistryFactory;

import java.util.List;
import java.util.Map;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-03 16:01
 * @description： 抽象执行者
 **/
public abstract class AbstractInvoker implements Invoker {
    private final RegistryFactory registryFactory = ExtensionLoader.getLoader(RegistryFactory.class).getAdaptiveExtension();
    private final Registry registry = registryFactory.getRegistry(ConfigManager.getInstance().getRegistryConfig().toURL());
    private final LoadBalance loadBalance = ExtensionLoader.getLoader(LoadBalance.class)
            .getExtension(ConfigManager.getInstance().getClusterConfig().getLoadBalance());

    @Override
    public RpcResult invoke(RpcRequest request) throws RpcException {
        Map<String, String> serviceParam = URLBuilder.getServiceParam(request.getInterfaceName(), request.getVersion());
        //TODO:协议名称这里需要修改
        //DONE.
        URL url = URL.builder()
                .protocol(URLKeyConstants.DMWS_RPC_PROTOCOL)
                .host(URLKeyConstants.ANY_HOST)
                .params(serviceParam)
                .build();
        // 注册中心拿出所有服务的信息
        List<URL> urls = registry.lookUp(url);

        if(CollectionUtil.isEmpty(urls)){
            throw new RpcException("Not service Providers registered." + serviceParam);
        }

        URL selected = loadBalance.select(urls, request);
        return doInvoke(request, selected);
    }

    /**
     * 实际函数调用
     * @param rpcRequest
     * @param selected
     * @return
     * @throws RpcException
     */
    protected abstract RpcResult doInvoke(RpcRequest rpcRequest, URL selected) throws RpcException;
}