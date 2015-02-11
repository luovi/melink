package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by goushicai on 14-12-16.
 */
public class AddNeedGoodsRequest implements Serializable {
    @NotNull
    @Min(0)
    private Integer user_id;

    @NotNull
    @Min(0)
    private Integer car_id;

    @NotNull
    @Length(max = 50)
    private String origin;
    @NotNull
    @Length(max = 50)
    private String destination;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publish_time;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date begin_time;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end_time;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expired_time;

    @Range(min = 0, max = 1)
    private Integer status;
    @Length(max = 200)
    private String memo;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    public Date getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Date expired_time) {
        this.expired_time = expired_time;
    }
}
