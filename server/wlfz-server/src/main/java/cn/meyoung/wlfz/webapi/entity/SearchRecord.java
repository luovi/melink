package cn.meyoung.wlfz.webapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户搜索记录
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@Entity
@Table(name = "wlfz_searchrecord")
public class SearchRecord extends BaseEntity<Integer> {
    private Integer userId;
    private String keyword;
    private Date queryTime;

    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "keyword", length = 50)
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }
}
