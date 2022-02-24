package com.dmws.rpc.demo.client.spi;

import cn.hutool.json.JSONUtil;


/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-09 15:12
 * @description：
 **/
public class JSONSerializer implements Serializer {
    @Override
    public byte[] serialize(Object object) {
        return JSONUtil.toJsonStr(object).getBytes();
    }
}
