package cn.meyoung.wlfz.webapi.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shaowei on 2015/1/5.
 * 查询条件
 */
public class ListSearchRecordRequest extends PagedRequest implements Serializable {

    private String keyword;

    private Integer user_id;

    //send_time字段的范围。。。
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date query_time_from;//时间范围开始
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date query_time_to;//时间范围结束

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Date getQuery_time_from() {
        return query_time_from;
    }

    public void setQuery_time_from(Date query_time_from) {
        this.query_time_from = query_time_from;
    }

    public Date getQuery_time_to() {
        return query_time_to;
    }

    public void setQuery_time_to(Date query_time_to) {
        this.query_time_to = query_time_to;
    }
}
