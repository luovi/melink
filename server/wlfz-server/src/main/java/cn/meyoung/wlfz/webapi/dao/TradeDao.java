package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by goushicai on 14-11-5.
 */
public interface TradeDao extends JpaRepository<Trade, Integer>, QueryDslPredicateExecutor<Trade> {

    @Query("select t from Trade t where t.car_user_id=?1")
    public Page<Trade> findByCar_user_id(Integer user_id, Pageable pageRequest);

    @Query("select t from Trade t where t.cargo_user_id=?1")
    public Page<Trade> findByCargo_user_id(Integer user_id, Pageable pageRequest);

}
