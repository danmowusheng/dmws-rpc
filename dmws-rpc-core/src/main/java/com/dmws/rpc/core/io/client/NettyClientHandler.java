package com.dmws.rpc.core.io.client;

import com.dmws.rpc.core.domain.RpcMessage;
import com.dmws.rpc.core.domain.RpcResponse;
import com.dmws.rpc.core.io.protocol.constans.CompressType;
import com.dmws.rpc.core.io.protocol.constans.MessageType;
import com.dmws.rpc.core.io.protocol.constans.SerializeType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-26 16:32
 * @description：
 **/
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage requestMessage) throws Exception {
        try{
            log.info("client receive message:[{}]", requestMessage);
            if (requestMessage.getMessageType() == MessageType.RESPONSE.getValue()){
                RpcResponse<?> response = (RpcResponse<?>)requestMessage.getData();
                //异步处理
                UnprocessedRequests.complete(response);
            }
        }finally {
            ReferenceCountUtil.release(requestMessage);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            //心跳
            IdleState state = ((IdleStateEvent)evt).state();
            if (state == IdleState.WRITER_IDLE){
                log.info("writer idle happen [{}]", ctx.channel().remoteAddress());
                Channel channel = ctx.channel();
                //新建一个心跳数据
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setSerializerType(SerializeType.PROTOSTUFF.getValue());
                rpcMessage.setCompressType(CompressType.DUMMY.getValue());
                rpcMessage.setMessageType(MessageType.HEARTBEAT.getValue());
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * Client异常捕获
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error("client caught exception:", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
