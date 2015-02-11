package cn.meyoung.wlfz.webapi.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shaowei on 2015/1/4.
 * 查询条件
 */
public class ListNoticeRequest extends PagedRequest implements Serializable {
    private String content;

    private String title;

    //send_time字段的范围。。。
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date send_time_from;//时间范围开始
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date send_time_to;//时间范围结束

    public Date getSend_time_from() {
        return send_time_from;
    }

    public void setSend_time_from(Date send_time_from) {
        this.send_time_from = send_time_from;
    }

    public Date getSend_time_to() {
        return send_time_to;
    }

    public void setSend_time_to(Date send_time_to) {
        this.send_time_to = send_time_to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
