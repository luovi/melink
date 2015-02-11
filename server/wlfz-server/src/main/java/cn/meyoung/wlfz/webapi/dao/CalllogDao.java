package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.Calllog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by goushicai on 14-11-4.
 */
public interface CalllogDao extends JpaRepository<Calllog, Integer>, QueryDslPredicateExecutor<Calllog> {
}
