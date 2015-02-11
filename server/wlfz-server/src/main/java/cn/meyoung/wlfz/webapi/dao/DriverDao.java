package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;


/**
 * Created by goushicai on 14-12-8.
 */
public interface DriverDao extends JpaRepository<Driver, Integer>, QueryDslPredicateExecutor<Driver> {

        @Query("select t from Driver t where t.plate_number=?1")
    public Page<Driver> findByPlateNumber(String plate_number, Pageable pageRequest);

}


