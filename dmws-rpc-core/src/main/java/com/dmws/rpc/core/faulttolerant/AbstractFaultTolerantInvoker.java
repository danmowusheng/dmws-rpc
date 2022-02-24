package com.dmws.rpc.core.faulttolerant;

import com.dmws.rpc.common.extension.ExtensionLoader;
import com.dmws.rpc.core.config.ClusterConfig;
import com.dmws.rpc.core.config.ConfigManager;
import com.dmws.rpc.core.domain.RpcRequest;
import com.dmws.rpc.core.domain.RpcResult;
import com.dmws.rpc.core.exception.RpcException;
import com.dmws.rpc.core.invocation.Invoker;
import com.dmws.rpc.core.loadBalance.LoadBalance;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-04 11:50
 * @description： 容错执行者
 **/
public abstract class AbstractFaultTolerantInvoker implements FaultTolerantInvoker{

    protected final ClusterConfig clusterConfig = ConfigManager.getInstance().getClusterConfig();

    private final LoadBalance loadBalance = ExtensionLoader.getLoader(LoadBalance.class)
            .getExtension(ConfigManager.getInstance().getClusterConfig().getLoadBalance());
    private final Invoker invoker = ExtensionLoader.getLoader(Invoker.class).getDefaultExtension();

    @Override
    public RpcResult invoke(RpcRequest request) throws RpcException {
        return doInvoke(request, invoker, loadBalance);
    }

    /**
     *  执行
     * @param request 请求
     * @param invoker 具体执行者
     * @param loadBalance 负载均衡策略
     * @return 结果
     * @throws RpcException 请求失败抛出异常
     */
    protected abstract RpcResult doInvoke(RpcRequest request, Invoker invoker, LoadBalance loadBalance) throws RpcException;

}
