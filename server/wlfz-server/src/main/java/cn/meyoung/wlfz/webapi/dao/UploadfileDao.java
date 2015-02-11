package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.entity.Uploadfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by goushicai on 14-11-26.
 */
public interface UploadfileDao extends JpaRepository<Uploadfile, Integer>, QueryDslPredicateExecutor<Uploadfile> {
    @Query("select t from Uploadfile t where t.user.id=?1 and t.file_type=?2")
    public List<Uploadfile> findListByUserID(Integer user_id, String file_type);

    @Query("select t from Uploadfile t where t.car.id=?1 and t.file_type=?2")
    public List<Uploadfile> findListByCarID(Integer car_id, String file_type);
}
