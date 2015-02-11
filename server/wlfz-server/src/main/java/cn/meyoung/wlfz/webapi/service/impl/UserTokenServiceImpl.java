package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.Constants;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.service.SettingService;
import cn.meyoung.wlfz.webapi.service.UserTokenService;
import cn.meyoung.wlfz.webapi.utils.DesUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * UserToken服务实现
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-23
 */
@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    private SettingService settingService;

    @Override
    public String create(User user) {

        Assert.notNull(user);

        UserToken userToken = new UserToken();
        userToken.setUserId(user.getId());
        userToken.setIsAdmin(user.getIsAdmin());

        Integer tokenExpiredMinutes = settingService.findValueByClientAndKey(Constants.CLIENT_SYSTEM, Constants.TOKEN_EXPIRED_TIME_KEY, 1440, Integer.class);

        userToken.setExpiredTime(DateUtils.addMinutes(new Date(), tokenExpiredMinutes));

        try {
            String encode = DesUtils.encode(DesUtils.PASSWORD_CRYPT_KEY, JSON.toJSONString(userToken));
            return encode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public UserToken parse(String token) {

        if (StringUtils.isBlank(token)) {
            return null;
        }

        try {
            String decodeValue = DesUtils.decodeValue(DesUtils.PASSWORD_CRYPT_KEY, token);
            UserToken userToken = JSON.parseObject(decodeValue, UserToken.class);
            return userToken;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
