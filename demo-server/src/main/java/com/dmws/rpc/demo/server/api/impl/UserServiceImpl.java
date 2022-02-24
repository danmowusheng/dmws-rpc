package com.dmws.rpc.demo.server.api.impl;

import com.dmws.rpc.demo.server.bean.UserInfo;
import com.dmws.rpc.demo.server.api.UserService;
import com.dmws.rpc.core.annotation.RpcService;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-09 15:07
 * @description： 用户服务
 **/
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public UserInfo getUser(Long id) {
        return UserInfo.builder().userId(id).userName("user"+id).build();
    }
}
