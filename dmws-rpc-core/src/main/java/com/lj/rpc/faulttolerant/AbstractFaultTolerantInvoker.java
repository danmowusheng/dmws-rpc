package com.lj.rpc.faulttolerant;

import com.lj.rpc.common.extension.ExtensionLoader;
import com.lj.rpc.config.ClusterConfig;
import com.lj.rpc.config.ConfigManager;
import com.lj.rpc.domain.RpcRequest;
import com.lj.rpc.domain.RpcResult;
import com.lj.rpc.exception.RpcException;
import com.lj.rpc.invocation.Invoker;
import com.lj.rpc.loadBalance.LoadBalance;

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
