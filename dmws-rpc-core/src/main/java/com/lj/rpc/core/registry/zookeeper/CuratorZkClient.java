package com.lj.rpc.core.registry.zookeeper;


import com.lj.rpc.common.constants.URLKeyConstants;
import com.lj.rpc.common.url.URL;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-25 16:31
 * @description： curator:zookeeper客户端
 **/
@Slf4j
public class CuratorZkClient {
    /**
     * 默认连接超时毫秒数
     */
    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 5_000;
    /**
     * 默认 session 超时毫秒数
     */
    private static final int DEFAULT_SESSION_TIMEOUT_MS = 60_000;
    /**
     * session 超时时间
     */
    private static final String SESSION_TIMEOUT_KEY = "zk.sessionTimeoutMs";
    /**
     * 连接重试次数
     */
    private static final int RETRY_TIMES = 3;
    /**
     * 连接重试睡眠毫秒数
     */
    private static final int RETRY_SLEEP_MS = 1000;
    /**
     * 根目录
     */
    private static final String ROOT_PATH = "/dmws-rpc";
    /**
     * zk 客户端
     * TO DO：这里应该要是final修饰的，但是编译器一直识别不出我已经初始化报错，所以这里暂时先不用final修饰
     */
    private CuratorFramework client;
    /**
     * 监听器 {path: 监听器}
     */
    private static final Map<String, CuratorCache> LISTENER_MAP = new ConcurrentHashMap<>();

    public CuratorZkClient(URL url) {
        //设置连接超时时间
        int timeout = url.getIntParam(URLKeyConstants.TIMEOUT, DEFAULT_CONNECTION_TIMEOUT_MS);
        //设置会话超时时间
        int sessionTimeout = url.getIntParam(SESSION_TIMEOUT_KEY, DEFAULT_SESSION_TIMEOUT_MS);

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectionTimeoutMs(timeout)
                .sessionTimeoutMs(sessionTimeout)
                .connectString(url.getAddress())
                .retryPolicy(new RetryNTimes(RETRY_TIMES, RETRY_SLEEP_MS));
        //如果没有用户名和密码
        //使用一个默认的
        if(StringUtils.isNotEmpty(url.getUserName()) || StringUtils.isNotEmpty(url.getPassword())){
            String authority = StringUtils.defaultIfEmpty(url.getUserName(), "")
                    +":"+StringUtils.defaultIfEmpty(url.getPassword(), "");
            builder.authorization("digest",authority.getBytes());
            client = builder.build();
            client.start();
            try {
                client.blockUntilConnected(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException("Time out waiting to connect to zookeeper! Please check the zookeeper config.");
            }
        }
    }

    /**
     * 创建节点
     * 这个节点在zookeeper中有什么用呢？
     * @param path 路径，如果没有加上根目录，会自动加上根目录
     * @param createMode 节点模式
     */
    public void createNode(String path, CreateMode createMode){
        try {
            client.create().creatingParentsIfNeeded().withMode(createMode).forPath(buildPath(path));
        } catch (KeeperException e) {
            log.warn("ZooKeeper Node " + path + " already exists.");
        }catch (Exception e){
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 创建永久节点
     * @param path 路径
     */
    public void createPersistentNode(String path){
        createNode(path, CreateMode.PERSISTENT);
    }

    /**
     * 创建临时节点
     * @param path
     */
    public void createEphemeralNode(String path){
        createNode(path, CreateMode.EPHEMERAL);
    }

    /**
     * 删除节点
     * @param path
     */
    public void removeNode(String path){
        try{
            client.delete().forPath(buildPath(path));
        }catch (KeeperException.NoNodeException ignored) {
            //忽略zookeeper异常
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 获取路径下的所有子节点
     * @param path 搜索路径
     * @return 所有子节点信息，若不存在则返回空List
     */
    public List<String> getChildren(String path){
        try {
            return client.getChildren().forPath(buildPath(path));
        }catch (KeeperException.NoNodeException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 添加监听者
     * @param path 相对路径
     * @param listener  监听者
     */
    public void addListener(String path, CuratorCacheListener listener){
        String fullPath = buildPath(path);
        if(LISTENER_MAP.containsKey(fullPath)){
            return;
        }

        CuratorCache curatorCache = CuratorCache.build(client, fullPath);
        LISTENER_MAP.put(fullPath, curatorCache);
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }

    /**
     * 构建完整的路径，用于存zookeeper
     * @param path 相对路径或者路径
     * @return 完整路径
     */
    private String buildPath(String path){
        if(path.startsWith(ROOT_PATH)){
            return path;
        }
        if (path.startsWith("/")){
            return ROOT_PATH + path;
        }
        return ROOT_PATH + "/" + path;
    }
}
