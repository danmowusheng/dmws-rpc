package com.dmws.rpc.demo.server.api;

import com.dmws.rpc.demo.server.bean.UserInfo;

/**
 * 用户服务
 */
public interface UserService {

    /**
     * 根据用户id获取用户信息
     * @param id 用户id
     * @return 用户信息，如果获取不到，返回null
     */
    UserInfo getUser(Long id);
}
