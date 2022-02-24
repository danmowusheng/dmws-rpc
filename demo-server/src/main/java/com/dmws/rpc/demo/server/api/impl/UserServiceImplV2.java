package com.dmws.rpc.demo.server.api.impl;

import com.dmws.rpc.demo.server.bean.UserInfo;
import com.dmws.rpc.demo.server.api.UserService;
import com.dmws.rpc.core.annotation.RpcService;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-09 15:09
 * @description： 用户服务版本2
 **/
@RpcService(version = "v2")
public class UserServiceImplV2 implements UserService {
    @Override
    public UserInfo getUser(Long id) {
        return UserInfo.builder().userId(id).userName("v2-user"+id).build();
    }
}
