package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.KeyWords;
import cn.meyoung.wlfz.webapi.entity.SearchRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by shaowie on 2015/1/4.
 * 用户搜索记录
 * JpaRepository也可以换成CrudRepository 只有ｃｕｒｌ
 */
public interface SearchRecordDao extends JpaRepository<SearchRecord, Integer>,
        QueryDslPredicateExecutor<SearchRecord>,SearchRecordDaoCustomize {


    //jpql不支持 limit 语法；因为要兼容多数据库;支持join查询
    //只能先把所有的查询出来，然后在page分页;
    //在代码里面做offset处理。返回指定count数量的结果
  /* 也可以这么写 @Query("select u from User u where username like :un")
    public List<User> findByUsername(@Param("un") String username);.
    List<User> users = userDao.findByUsername("%a");*/


   @Query("select count(t),t.keyword from SearchRecord t group by t.keyword order by count(t) desc")
    public List getKeyWordByCount();
  /*  @Query("select count(t.id),t.keyword from SearchRecord t group by t.keyword order by count(t.id) desc")
    public Page<KeyWords> getKeyWordGroupList(Pageable pageable);*/



    //或者在定义扩展方法。。。类似Query query = em .createQuery()


}
