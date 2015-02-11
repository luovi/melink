package cn.meyoung.wlfz.webapi.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shaowei on 2015/1/4.
 */
public class ListLocationRequest extends PagedRequest implements Serializable {
    private Byte locate_type;//1 为基站定位,2为手机app定位

    private Integer car_id;

    private String plate_number;// 车牌

    public Date getLocate_time_from() {
        return locate_time_from;
    }

    public void setLocate_time_from(Date locate_time_from) {
        this.locate_time_from = locate_time_from;
    }

    public Date getLocate_time_to() {
        return locate_time_to;
    }

    public void setLocate_time_to(Date locate_time_to) {
        this.locate_time_to = locate_time_to;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    public Byte getLocate_type() {
        return locate_type;
    }

    public void setLocate_type(Byte locate_type) {
        this.locate_type = locate_type;
    }

    //send_time字段的范围。。。
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date locate_time_from;//时间范围开始
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date locate_time_to;//时间范围结束

}
