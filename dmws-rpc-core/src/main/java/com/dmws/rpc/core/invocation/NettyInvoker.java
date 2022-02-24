package com.dmws.rpc.core.invocation;


import com.dmws.rpc.common.url.URL;
import com.dmws.rpc.core.config.ProtocolConfig;
import com.dmws.rpc.core.domain.*;
import com.dmws.rpc.core.io.protocol.constans.MessageConstants;
import com.dmws.rpc.core.config.ConfigManager;
import com.dmws.rpc.core.exception.RpcException;
import com.dmws.rpc.core.io.client.NettyClient;
import com.dmws.rpc.core.io.client.UnprocessedRequests;
import com.dmws.rpc.core.io.protocol.constans.CompressType;
import com.dmws.rpc.core.io.protocol.constans.MessageType;
import com.dmws.rpc.core.io.protocol.constans.SerializeType;
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
            return new AsyncResult(resultFuture);
        }
        else{
            throw new RpcException("channel is not active. address=" + socketAddress);
        }

        /**
         * 空指针异常来源于这条之前写的没有删掉的return null语句
         */
        //return null;
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
