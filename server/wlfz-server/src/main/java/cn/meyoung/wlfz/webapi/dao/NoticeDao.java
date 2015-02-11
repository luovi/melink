package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by shaowei
 * on 2015/1/4.
 */
public interface NoticeDao  extends JpaRepository<Notice, Integer>, QueryDslPredicateExecutor<Notice> {
}
