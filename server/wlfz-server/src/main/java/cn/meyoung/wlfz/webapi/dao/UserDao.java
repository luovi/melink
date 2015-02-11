package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public interface UserDao extends JpaRepository<User, Integer>, QueryDslPredicateExecutor<User> {

    /**
     * 获取最后的用户id
     *
     * @return 最后的用户id
     */
    @Query("select max(u.id) from User u")
    Integer findLastUserId();
}
