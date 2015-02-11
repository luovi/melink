package cn.meyoung.wlfz.webapi.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆位置记录
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@Entity
@Table(name = "wlfz_location")
public class Location extends BaseEntity<Integer> {

    /**
     * 基站定位
     */
    public final static byte LOCATION_TYPE_STATION = 1;

    /**
     * 手机定位
     */
    public final static byte LOCATION_TYPE_PHONE = 2;

   // private Integer carId;
    private Car car;
    private Date locateTime;
    private Byte locateType;
    private BigDecimal longitude;
    private BigDecimal latitude;

   /*@Column(name = "car_id")
    public Integer getCarId() {
        return carId;
    }
    public void setCarId(Integer carId) {
        this.carId = carId;
    }*/



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    public Car getCar() {
        return car;
    }
    public void setCar(Car car) {
        this.car = car;
    }


    @Column(name = "locate_time")
    public Date getLocateTime() {
        return locateTime;
    }

    public void setLocateTime(Date locateTime) {
        this.locateTime = locateTime;
    }

    @Column(name = "locate_type")
    public Byte getLocateType() {
        return locateType;
    }

    public void setLocateType(Byte locateType) {
        this.locateType = locateType;
    }

    @Column(name = "longitude", precision = 15, scale = 10)
    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Column(name = "latitude", precision = 15, scale = 10)
    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

  /*  @Override
    public String toString()
    {
        return "AccountInfo{" +
                "accountId=" + accountId +
                ", userInfo=" + userInfo +
                ", balance=" + balance +
                '}';
    }*/
}
