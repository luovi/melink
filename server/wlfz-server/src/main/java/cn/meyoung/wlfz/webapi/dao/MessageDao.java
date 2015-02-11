package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.Car;
import cn.meyoung.wlfz.webapi.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * 处理wlfz_message表数据具体操作
 * @author shaowei
 * @version 1.0
 * @date 2014/12/26
 * JpaRepository提供了curd和一些分页排序的查询方法
 * QueryDslPredicateExecutor是Qsl的支持主要用于查询
 * 基本上继承了这2个接口，数据请求和操作基本不用写代码
 * 但是也可以自定义方法。。。通过Qsl查询语言来操作
 */
public interface MessageDao extends JpaRepository<Message, Integer>, QueryDslPredicateExecutor<Message> {


}
