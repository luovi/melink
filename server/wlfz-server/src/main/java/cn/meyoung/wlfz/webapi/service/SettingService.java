package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.entity.Setting;

import java.util.List;

/**
 * 客户端配置服务
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
public interface SettingService {

    /**
     * 根据客户端类型获取配置
     *
     * @param client 客户端类型
     * @return 配置列表
     */
    List<Setting> findListByClient(String client);

    /**
     * 根据client和key获取参数值
     *
     * @param client 客户端类型
     * @param key    key
     * @param clazz  类型
     * @param <T>
     * @return 参数值
     */
    <T> T findValueByClientAndKey(String client, String key, Class<T> clazz);


    /**
     * 根据client和key获取参数值
     *
     * @param client       客户端类型
     * @param key          key
     * @param defaultValue 默认值
     * @param clazz        类型
     * @param <T>
     * @return 参数值
     */
    <T> T findValueByClientAndKey(String client, String key, T defaultValue, Class<T> clazz);
}
