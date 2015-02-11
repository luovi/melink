package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.OpenComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public interface OpenCommentDao extends JpaRepository<OpenComment, Integer>, QueryDslPredicateExecutor<OpenComment> {
}

