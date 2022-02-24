package com.dmws.rpc.core.io.protocol.codec;


import com.dmws.rpc.common.extension.ExtensionLoader;
import com.dmws.rpc.core.domain.RpcMessage;
import com.dmws.rpc.core.io.protocol.constans.MessageConstants;
import com.dmws.rpc.core.io.serializer.Serializer;
import com.dmws.rpc.core.io.compress.Compressor;
import com.dmws.rpc.core.io.protocol.constans.CompressType;
import com.dmws.rpc.core.io.protocol.constans.MessageType;
import com.dmws.rpc.core.io.protocol.constans.SerializeType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-19 15:48
 * @description： 自定义协议编码器
 **/
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf out) throws Exception {
        // 2B 魔数
        out.writeBytes(MessageConstants.MAGIC_NUMBER);
        // 1B 版本
        out.writeByte(MessageConstants.VERSION);
        // 4B 消息长度,但是这里存在一个问题，我们还没有解析出message中的data大小，所以无法填上具体的值,后续要重写这个值
        out.writerIndex(out.writerIndex() + MessageConstants.FULL_LENGTH_LENGTH);
        // 1B 消息类型
        out.writeByte(rpcMessage.getMessageType());
        // 1B 序列化类型
        out.writeByte(rpcMessage.getSerializerType());
        // 1B 压缩类型
        out.writeByte(rpcMessage.getCompressType());
        // 8B 请求ID
        out.writeLong(rpcMessage.getMessageID());
        // 写body并返回其长度
        int bodyLength = writeBody(rpcMessage, out);

        //获取当前写指针
        int writerIndex = out.writerIndex();
        //让指针指向消息长度的起始位置
        out.writerIndex(MessageConstants.MAGIC_NUMBER_LENGTH + MessageConstants.VERSION_LENGTH);
        //写消息长度
        out.writeInt(bodyLength);
        //写指针复原
        out.writerIndex(writerIndex);
    }

    /**
     * 写body
     * @param rpcMessage
     * @param out
     * @return body的长度
     */
    public int writeBody(RpcMessage rpcMessage, ByteBuf out){
        byte messageType = rpcMessage.getMessageType();
        //如果是心跳类型的，那么没有body，返回头部长度即可
        if(messageType == MessageType.HEARTBEAT.getValue()){
            return 0;
        }

        //序列化器
        SerializeType serializeType = SerializeType.fromValue(rpcMessage.getSerializerType());
        if (serializeType == null){
            throw new IllegalArgumentException("codec type not found");
        }
        Serializer serializer = ExtensionLoader.getLoader(Serializer.class).getExtension(serializeType.getName());

        //压缩器
        CompressType compressType = CompressType.fromValue(rpcMessage.getCompressType());
        Compressor compressor = ExtensionLoader.getLoader(Compressor.class).getExtension(compressType.getName());

        //序列化
        byte[] serializedBytes = serializer.serialize(rpcMessage.getData());
        //压缩
        byte[] compressedBytes = compressor.compress(serializedBytes);

        //写body
        out.writeBytes(compressedBytes);
        return compressedBytes.length;
    }

}
