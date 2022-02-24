package com.dmws.rpc.core.registry;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2022-02-23 19:48
 * @description： 注册中心事件
 **/
@Data
@AllArgsConstructor
public class RegistryEvent {

    /**
     * 事件类型
     */
    public enum Type {
        /**
         * 节点已创建
         */
        CREATED,
        /**
         * 节点已删除
         */
        DELETED,
        /**
         * 节点已更改
         */
        CHANGED
    }

    /**
     * 事件类型
     */
    private Type type;

    /**
     * 旧数据
     */
    private String oldData;

    /**
     * 当前数据
     */
    private String data;
}

