package cn.meyoung.wlfz.webapi.dto.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shaowei on 2015/1/4.
 * 小写和下划线
 */
public class NoticeResponse implements Serializable {

    private String content;//内容
    private Date publish_time;//默认当前时间
    private String title;//标题，短信推送 可以没有标题

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

    public Date getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }


}
