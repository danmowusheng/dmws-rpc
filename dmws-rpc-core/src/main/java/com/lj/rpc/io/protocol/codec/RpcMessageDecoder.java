package com.lj.rpc.io.protocol.codec;

import com.lj.rpc.domain.RpcMessage;
import com.lj.rpc.io.protocol.constans.CompressType;
import com.lj.rpc.io.protocol.constans.MessageType;
import com.lj.rpc.io.protocol.constans.SerializeType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static com.lj.rpc.io.protocol.constans.MessageConstants.*;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-24 15:02
 * @description： 自定义协议解码器
 **/
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {
    public RpcMessageDecoder() {
        super(
                // 最大的长度，如果超过，会直接丢弃
                MAX_FRAME_LENGTH,
                // 描述长度的字段[4B full length（消息长度）]在哪个位置：在 [2B magic（魔数）]、[1B version（版本）] 后面
                MAGIC_NUMBER_LENGTH + VERSION_LENGTH,
                // 描述长度的字段[4B full length（消息长度）]本身的长度，也就是 4B 啦
                FULL_LENGTH_LENGTH,
                // LengthFieldBasedFrameDecoder 拿到消息长度之后，还会加上 [4B full length（消息长度）] 字段前面的长度
                // 因为我们的消息长度包含了这部分了，所以需要减回去
                -(MAGIC_NUMBER_LENGTH + VERSION_LENGTH + FULL_LENGTH_LENGTH),
                // initialBytesToStrip: 去除哪个位置前面的数据。因为我们还需要检测 魔数 和 版本号，所以不能去除
                0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //先用父类方法解码
        Object decoded = super.decode(ctx, in);

        if(decoded instanceof ByteBuf){
            ByteBuf frame = (ByteBuf)decoded;
            //至少包含完整的头部信息才可以进行解析
            if(frame.readableBytes() >= HEADER_LENGTH){
                try {
                    return decodeFrame(frame);
                }catch (Exception e){
                    log.error("Decode frame error.", e);
                }finally {
                    //若解码报错，则需要使用release方法释放frame
                    frame.release();
                }
            }
        }
        return decoded;
    }

    private RpcMessage decodeFrame(ByteBuf in) {
        readAndCheckMagic(in);
        readAndCheckVersion(in);
        int fullLength = in.readInt();
        byte messageType = in.readByte();
        byte serialize = in.readByte();
        byte compress = in.readByte();
        long messageID = in.readLong();

        RpcMessage rpcMessage = RpcMessage.builder()
                                .serializerType(serialize)
                                .compressType(compress)
                                .messageID(messageID)
                                .messageType(messageType).build();
        //若消息体不含数据则直接返回
        if(messageType == MessageType.HEARTBEAT.getValue()){
            return rpcMessage;
        }

        int bodyLength = fullLength - HEADER_LENGTH;
        if(bodyLength <= 0)return rpcMessage;

        byte[] bodyBytes = new byte[bodyLength];
        in.readBytes(bodyBytes);
        /**
         * TO DO:实现真正的解压和反序列化
         */
        CompressType compressType = CompressType.fromValue(compress);
        //进行解压

        SerializeType serializeType = SerializeType.fromValue(serialize);
        //进行反序列化
        if(serializeType == null)throw new IllegalArgumentException("Unknown serializeType:"+serializeType.getName());

        //根据消息类型获取消息体结构


        return rpcMessage;
    }

    /**
     * 读取并检查版本
     * @param in
     */
    private void readAndCheckVersion(ByteBuf in){
        byte version = in.readByte();
        if(version != VERSION)throw new IllegalArgumentException("Unknown version:" + version);
    }

    /**
     * 读取并检查魔数
     * @param in
     */
    private void readAndCheckMagic(ByteBuf in){
        byte[] bytes = new byte[MAGIC_NUMBER_LENGTH];
        in.readBytes(bytes);
        for(int i=0;i<MAGIC_NUMBER_LENGTH;i++){
            if(bytes[i] != MAGIC_NUMBER[i])throw new IllegalArgumentException("Unknown magic:"+ Arrays.toString(bytes));
        }
    }


}
