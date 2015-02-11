package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.NeedGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by goushicai on 14-12-15.
 */
public interface NeedGoodsDao extends JpaRepository<NeedGoods, Integer>, QueryDslPredicateExecutor<NeedGoods> {

}
