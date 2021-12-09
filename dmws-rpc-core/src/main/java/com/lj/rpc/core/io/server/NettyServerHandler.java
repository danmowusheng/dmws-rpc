package com.lj.rpc.core.io.server;

import com.lj.rpc.core.domain.RpcMessage;
import com.lj.rpc.core.domain.RpcRequest;
import com.lj.rpc.core.domain.RpcResponse;
import com.lj.rpc.core.exception.RpcException;
import com.lj.rpc.core.io.protocol.constans.MessageType;
import com.lj.rpc.core.proxy.RpcServiceCache;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-28 16:11
 * @description：
 **/
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage requestMessage) throws Exception {
        try{
            //不理心跳类型消息
            if (requestMessage.getMessageType() == MessageType.HEARTBEAT.getValue()){
                return;
            }
            RpcMessage.RpcMessageBuilder responseMsgBuilder = RpcMessage.builder()
                    .serializerType(requestMessage.getSerializerType())
                    .compressType(requestMessage.getCompressType())
                    .messageID(requestMessage.getMessageID());
            RpcRequest rpcRequest = (RpcRequest)requestMessage.getData();
            Object result;

            try{
                // 根据请求的接口名和版本，获取服务。这个服务是在bean初始化的时候加上的
                Object service = RpcServiceCache.getService(rpcRequest.getRpcServiceForCache());
                Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                //开始执行
                result = method.invoke(service, rpcRequest.getParams());
                log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            } catch (Exception e) {
                throw new RpcException(e.getMessage(), e);
            }

            responseMsgBuilder.messageType(MessageType.RESPONSE.getValue());
            if (channelHandlerContext.channel().isActive() && channelHandlerContext.channel().isWritable()){
                RpcResponse<Object> response = RpcResponse.success(result, requestMessage.getMessageID());
                responseMsgBuilder.data(result);
            }else {
                responseMsgBuilder.data(RpcResponse.fail());
                log.error("not writable now, message dropped");
            }
            channelHandlerContext.writeAndFlush(responseMsgBuilder.build()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }finally {
            ReferenceCountUtil.release(requestMessage);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object evt) throws Exception {
        // 处理空闲状态的
        if (evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent)evt).state();
            if (state == IdleState.READER_IDLE){
                log.info("idle check happen, so close the connection");
                channelHandlerContext.close();
            }
        }else {
            super.userEventTriggered(channelHandlerContext, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception", cause);
        ctx.close();
    }
}
