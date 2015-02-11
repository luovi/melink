package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.dao.SettingDao;
import cn.meyoung.wlfz.webapi.entity.Setting;
import cn.meyoung.wlfz.webapi.service.SettingService;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户端配置服务实现
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingDao settingDao;

    @Override
    public List<Setting> findListByClient(String client) {
        return settingDao.findListByClient(client);
    }

    @Override
    public <T> T findValueByClientAndKey(String client, String key, Class<T> clazz) {

        Setting setting = settingDao.findByClientAndKey(client, key);

        if (null == setting || null == setting.getValue()) {
            return null;
        }

        try {
            return (T) ConvertUtils.convert(setting.getValue(), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> T findValueByClientAndKey(String client, String key, T defaultValue, Class<T> clazz) {
        Setting setting = settingDao.findByClientAndKey(client, key);

        if (null == setting || null == setting.getValue()) {
            return defaultValue;
        }

        try {
            return (T) ConvertUtils.convert(setting.getValue(), clazz);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
