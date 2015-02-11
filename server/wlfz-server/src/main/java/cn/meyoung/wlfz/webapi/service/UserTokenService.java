package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.entity.User;

/**
 * UserToken服务
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-22
 */
public interface UserTokenService {

    /**
     * 创建UserToken
     *
     * @param user 用户信息
     * @return token字符串
     */
    String create(User user);

    /**
     * 从字符串解析UserToken
     *
     * @param token token字符串
     * @return UserToken
     */
    UserToken parse(String token);
}
