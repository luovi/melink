package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by goushicai on 14-12-16.
 */
public class ModifyNeedGoodsRequest implements Serializable {

    @Range(min = 0, max = 1)
    private Integer status;//0草稿，1已发布,默认1
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date begin_time;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end_time;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expired_time;
    @Length(max = 50)
    private String origin;
    @Length(max = 50)
    private String destination;
    @Length(max = 200)
    private String memo;

    public Date getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(Date begin_time) {
        this.begin_time = begin_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public Date getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Date expired_time) {
        this.expired_time = expired_time;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
