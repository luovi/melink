package cn.meyoung.wlfz.webapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by shaowei
 * on 2015/1/4.
 * 公告信息。。
 */
@Entity
@Table(name = "wlfz_notice")
public class Notice extends BaseEntity<Integer> {
    //id字段已经在BaseEntity中定义了
    private String content;
    private Date publish_time;
    private String title;

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "publish_time")
    public Date getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
