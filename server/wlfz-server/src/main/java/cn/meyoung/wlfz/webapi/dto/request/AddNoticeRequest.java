package cn.meyoung.wlfz.webapi.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shaowie on 2015/1/4.
 * 需要添加发布时间
 */
public class AddNoticeRequest extends ModifyNoticeRequest implements Serializable {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publish_time;//默认当前时间


    public Date getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }
}
