package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.ListUsersRequest;
import cn.meyoung.wlfz.webapi.entity.User;
import org.springframework.data.domain.Page;

/**
 * 用户信息服务
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public interface UserService {

    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    User find(Integer id);

    /**
     * 用户是否存在
     *
     * @param id 用户id
     * @return true：存在 false：不存在
     */
    boolean exists(Integer id);

    /**
     * 用户名是否已经被使用
     *
     * @param userName 用户名
     * @return true:已经使用 false:未使用
     */
    boolean existsByUserName(String userName);


    /**
     * 用户手机号码是否被使用
     *
     * @param contactNumber 手机号码
     * @return true：已经使用 false：未使用
     */
    boolean existsByContactNumber(String contactNumber);

    /**
     * 根据用户名获取用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    User findByUserName(String userName);

    /**
     * 根据用户手机号获取用户信息
     * @param contactNumber 用户手机号
     * @return 用户信息
     */
    User findByContactNumber(String contactNumber);


    /**
     * 根据用户手机号或者用户名获取用户信息，优先匹配手机号
     *
     * @return 用户信息
     */
    User findByUserNameOrContactNumber(String loginName);

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     */
    void save(User user);

    /**
     * 获取最后的用户id
     *
     * @return 最后的用户id
     */
    Integer findLastUserId();


    /**
     * 获取用户列表
     *
     * @param request
     * @return 用户列表
     */
    Page<User> list(ListUsersRequest request);
}
