package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public interface CarDao extends JpaRepository<Car, Integer>, QueryDslPredicateExecutor<Car> {

    /**
     * 获取用户的车辆信息
     *按照规范，自定义方法(不能随便定义)
     * @param userId 用户id
     * @return 车辆信息列表
     */
    List<Car> findListByUserId(Integer userId);


}
