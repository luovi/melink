package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public interface LocationDao extends JpaRepository<Location, Integer>, QueryDslPredicateExecutor<Location> {
}
