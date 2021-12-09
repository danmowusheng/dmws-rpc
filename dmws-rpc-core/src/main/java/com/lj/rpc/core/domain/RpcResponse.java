package com.lj.rpc.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-26 15:38
 * @description： RPC响应
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse<T> {
    /**
     * 请求id
     */
    private long requestId;
    /**
     * 响应码
     */
    private Integer responseCode;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    /**
     * 成功返回响应
     * @param data 响应数据
     * @param requestId 响应的请求id
     * @param <T>
     * @return
     */
    public static <T> RpcResponse<T> success(T data, long requestId){
        return RpcResponse.<T>builder()
                .responseCode(RpcResponseCode.SUCCESS.getCode())
                .message(RpcResponseCode.SUCCESS.getMessage())
                .requestId(requestId)
                .data(data)
                .build();
    }

    /**
     * 返回出错信息
     * @param <T>
     * @return
     */
    public static <T> RpcResponse<T> fail(){
        return RpcResponse.<T>builder()
                .responseCode(RpcResponseCode.FAIL.getCode())
                .message(RpcResponseCode.FAIL.getMessage())
                .build();
    }

    /**
     * 返回传入的出错信息
     * @param code 出错状态
     * @param <T>
     * @return
     */
    public static <T> RpcResponse<T> fail(RpcResponseCode code){
        return RpcResponse.<T>builder()
                .responseCode(code.getCode())
                .message(code.getMessage())
                .build();
    }
}
