package com.lj.rpc.invocation;


import com.lj.rpc.common.url.URL;
import com.lj.rpc.config.ConfigManager;
import com.lj.rpc.config.ProtocolConfig;
import com.lj.rpc.domain.RpcMessage;
import com.lj.rpc.domain.RpcRequest;
import com.lj.rpc.domain.RpcResponse;
import com.lj.rpc.domain.RpcResult;
import com.lj.rpc.exception.RpcException;
import com.lj.rpc.io.client.NettyClient;
import com.lj.rpc.io.client.UnprocessedRequests;
import com.lj.rpc.io.protocol.constans.CompressType;
import com.lj.rpc.io.protocol.constans.MessageConstants;
import com.lj.rpc.io.protocol.constans.MessageType;
import com.lj.rpc.io.protocol.constans.SerializeType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-03 16:13
 * @description： netty 执行者，相当于发请求
 **/
@Slf4j
public class NettyInvoker extends AbstractInvoker{

    private final NettyClient nettyClient = NettyClient.getInstance();

    @Override
    protected RpcResult doInvoke(RpcRequest rpcRequest, URL selected) throws RpcException {
        InetSocketAddress socketAddress = new InetSocketAddress(selected.getHost(), selected.getPort());
        Channel channel = nettyClient.getChannel(socketAddress);

        if (channel.isActive()){
            CompletableFuture<RpcResponse<?>> resultFuture = new CompletableFuture<>();
            // 构建 RPC 消息，此处会构建 requestId
            RpcMessage rpcMessage = buildMessage(rpcRequest);
            //放入未完成映射Map
            UnprocessedRequests.put(rpcMessage.getMessageID(), resultFuture);
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future ->{
                if (future.isSuccess()){
                    log.info("client send message:[{}]", rpcMessage);
                }
                else{
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.info("message send failed:", future.cause());
                }
            });
        }
        else{
            throw new RpcException("channel is not active. address=" + socketAddress);
        }


        return null;
    }

    /**
     * 根据请求数据，构建 Rpc 通用信息结构
     * @param rpcRequest 请求实体
     * @return RPC通用消息
     */
    private RpcMessage buildMessage(RpcRequest rpcRequest) {
        ProtocolConfig protocolConfig = ConfigManager.getInstance().getProtocolConfig();

        //压缩类型
        String compressTypeName = protocolConfig.getCompressType();
        CompressType compressType = CompressType.fromName(compressTypeName);

        //序列化类型
        String serializeTypeName = protocolConfig.getSerializeType();
        SerializeType serializeType = SerializeType.fromName(serializeTypeName);

        if (serializeType == null){
            throw new IllegalStateException("serializeType " + serializeTypeName + " not support.");
        }

        return RpcMessage.builder()
                .messageType(MessageType.REQUEST.getValue())
                .compressType(compressType.getValue())
                .serializerType(serializeType.getValue())
                .messageID(MessageConstants.MESSAGE_ID.getAndIncrement())
                .data(rpcRequest)
                .build();
    }
}
