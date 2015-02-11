package cn.meyoung.wlfz.webapi.dto.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 位置信息
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-24
 */
public class LocationResponse implements Serializable {
    private static final long serialVersionUID = 2886689063122849032L;

    @JSONField(name = "locate_time")
    private Date locateTime;

    @JSONField(name = "locate_type")
    private Byte locateType;
    private BigDecimal longitude;
    private BigDecimal latitude;

//    @JSONField(name = "car_id")
    private Integer car_Id;//对应entity的
    private String plate_number;//车牌号,,mapping.xml中添加映射car

    public Integer getCar_Id() {
        return car_Id;
    }

    public void setCar_Id(Integer car_Id) {
        this.car_Id = car_Id;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }


    public Date getLocateTime() {
        return locateTime;
    }

    public void setLocateTime(Date locateTime) {
        this.locateTime = locateTime;
    }

    public Byte getLocateType() {
        return locateType;
    }

    public void setLocateType(Byte locateType) {
        this.locateType = locateType;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}
