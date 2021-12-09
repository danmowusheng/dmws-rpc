package com.lj.rpc.core.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.lj.rpc.core.annotation.Config;
import com.lj.rpc.common.extension.ExtensionLoader;
import com.lj.rpc.core.config.loader.ConfigLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-28 15:27
 * @description： 配置管理
 **/
public class ConfigManager {
    /**
     * 按照优先级排序的加载器
     */
    private final List<ConfigLoader> configLoaders;

    private final Map<Class<?>, Object> configCache = new ConcurrentHashMap<>();

    public ConfigManager() {
        // 按照优先级放好
        String[] configLoaderNames = new String[]{"system-property", "properties"};
        this.configLoaders = new ArrayList<>(configLoaderNames.length);

        for (String loaderName : configLoaderNames){
            ConfigLoader loader = ExtensionLoader.getLoader(ConfigLoader.class).getExtension(loaderName);
            configLoaders.add(loader);
        }
    }

    /**
     * 单例模式
     * TODO:这里后续还需要完善
     */
    private static final ConfigManager instance = new ConfigManager();

    public static ConfigManager getInstance(){
        return instance;
    }

    /**
     * 加载配置，有缓存
     * @param clazz 配置类型
     * @param <T> 类型
     * @return 配置实体
     */
    public <T> T loadConfig(Class<T> clazz){
        T config = (T)configCache.get(clazz);
        if (config == null){
            config = loadAndCreateConfig(clazz);
            configCache.put(clazz, config);
        }
        return config;
    }

    /**
     * 加载并创建配置
     * @param clazz 配置类型
     * @param <T> 类型
     * @return 配置类，load 不到的字段为 null
     */
    private <T> T loadAndCreateConfig(Class<T> clazz) {
        Config configAnnotation = clazz.getAnnotation(Config.class);
        if (configAnnotation == null){
            throw new IllegalStateException("config class " + clazz.getName() + " must has @Config annotation");
        }

        String prefix = configAnnotation.prefix();
        if (StrUtil.isBlank(prefix)){
            throw new IllegalStateException("config class " + clazz.getName() + "@Config annotation must has prefix");
        }

        try{
            T configObject = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()){
                //忽略掉静态的
                if(Modifier.isStatic(field.getModifiers())){
                    continue;
                }
                String configKey = prefix + "." + field.getName();
                String value = loadConfigItem(configKey);
                if (value == null){
                    continue;
                }
                /**
                 * TODO: 这一段还有点不太明白，后续还要回来继续看
                 */
                Object convertedValue = Convert.convert(field.getType(), value);
                field.setAccessible(true);
                field.set(configObject, convertedValue);
            }

            return configObject;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 获取配置项
     * @param configKey 配置项的 key
     * @return 如果获取不到，返回 null
     */
    private String loadConfigItem(String configKey) {
        // 按照优先级，先获取到就返回
        for(ConfigLoader configLoader : configLoaders){
            String value = configLoader.loadConfigItem(configKey);
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    /**
     * 获取注册中心的配置
     * @return
     */
    public RegistryConfig getRegistryConfig(){
        return loadConfig(RegistryConfig.class);
    }

    public ServiceConfig getServiceConfig(){
        return loadConfig(ServiceConfig.class);
    }

    public ProtocolConfig getProtocolConfig(){
        return loadConfig(ProtocolConfig.class);
    }

    public ClusterConfig getClusterConfig(){
        return loadConfig(ClusterConfig.class);
    }
}
