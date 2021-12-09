package com.lj.rpc.core.io.client;

import cn.hutool.core.util.StrUtil;
import com.lj.rpc.core.exception.RpcException;
import com.lj.rpc.core.io.protocol.codec.RpcMessageDecoder;
import com.lj.rpc.core.io.protocol.codec.RpcMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-26 16:03
 * @description：
 **/
@Slf4j
public class NettyClient {
    private final Bootstrap bootstrap;

    /**
     * {地址：连接的channel}
     */
    private static final Map<SocketAddress, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * 使用单例
     */
    private static NettyClient instance = null;

    public static NettyClient getInstance(){
        if (instance == null){
            synchronized (NettyClient.class){
                if(instance == null){
                    instance = new NettyClient();
                }
            }
        }
        return instance;
    }

    /**
     * 设置为私有的构造方法，避免外界访问
     * 保证外界只能访问单例
     */
    private NettyClient(){
        bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel){
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // 设定 IdleStateHandler 心跳检测每 5 秒进行一次写检测
                        // write()方法超过 5 秒没调用，就调用 userEventTrigger
                        pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        pipeline.addLast(new RpcMessageEncoder());
                        pipeline.addLast(new RpcMessageDecoder());
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }

    /**
     * 获取和指定地址连接的 channel，如果获取不到，则连接
     * @param address
     * @return channel
     */
    public Channel getChannel(SocketAddress address) throws RpcException {
        Channel channel = CHANNEL_MAP.get(address);
        if (channel == null || !channel.isActive()){
            channel = connect(address);
            CHANNEL_MAP.put(address, channel);
        }
        return channel;
    }

    /**
     * 与指定地址连接
     * 这里的抛出异常是怎么回事？
     * @param address 指定地址
     * @return
     * @throws RpcException
     */
    private Channel connect(SocketAddress address) throws RpcException {
        try {
            log.info("try to connect server [{}]", address);
            CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
            ChannelFuture connect = bootstrap.connect(address);
            connect.addListener((ChannelFutureListener)future -> {
                if(future.isSuccess()){
                    completableFuture.complete(future.channel());
                }else{
                    throw new IllegalStateException(StrUtil.format("connect failed. address:",address));
                }
            });
            return completableFuture.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RpcException(address + "connect failed.", e);
        }
    }


}
