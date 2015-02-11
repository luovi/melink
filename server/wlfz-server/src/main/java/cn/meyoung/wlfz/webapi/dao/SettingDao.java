package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 配置Dao
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public interface SettingDao extends JpaRepository<Setting, Integer> {

    /**
     * 根据客户端类型获取配置
     *按照规范，自定义方法(不能随便定义)
     * @param client 客户端类型
     * @return 配置列表
     */
    List<Setting> findListByClient(String client);


    /**
     * 根据key获取配置
     *按照规范，自定义方法(不能随便定义)
     * @param key key
     * @return 配置
     */
    Setting findByClientAndKey(String client, String key);
}
