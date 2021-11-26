package com.lj.rpc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC响应状态码
 */
@Getter
@AllArgsConstructor
public enum RpcResponseCode {
    SUCCESS(200, "success"),
    FAIL(500, "fail");
    /**
     * 状态码
     */
    private final int code;
    /**
     * 消息
     */
    private final String message;
}
