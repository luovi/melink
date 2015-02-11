package cn.meyoung.wlfz.webapi.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by goushicai on 14-11-4.
 */

@Entity
@Table(name = "wlfz_calllogs")
@DynamicUpdate
public class Calllog extends BaseEntity<Integer> {
    //车发起通话
    public static final Integer CALL_BY_CAR = 1;
    //货方发起通话，目前不会有这类数据，扩展用
    public static final Integer CALL_BY_CARGO = 2;
    //通话结果，达成车货匹配
    public static final Integer TRADE_SUCCESS = 1;
    //未达成
    public static final Integer TRADE_FAIL = 0;

    private Integer type;
    private Integer from_user;
    private Integer to_user;
    private Integer cargo_id;
    private Integer car_id;
    private Date start_time;
    private Date end_time;
    private Integer result;
    private String from_number;
    private String to_number;
    private User fromUserInfo;
    private User toUserInfo;
    private Car carInfo;
    private Cargo cargoInfo;

    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "from_user")
    public Integer getFrom_user() {
        return from_user;
    }

    public void setFrom_user(Integer from_user) {
        this.from_user = from_user;
    }

    @Column(name = "to_user")
    public Integer getTo_user() {
        return to_user;
    }

    public void setTo_user(Integer to_user) {
        this.to_user = to_user;
    }

    @Column(name = "cargo_id")
    public Integer getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(Integer cargo_id) {
        this.cargo_id = cargo_id;
    }

    @Column(name = "car_id")
    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    @Column(name = "start_time")
    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    @Column(name = "end_time")
    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    @Column(name = "result")
    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Column(name = "from_number", length = 50)
    public String getFrom_number() {
        return from_number;
    }

    public void setFrom_number(String from_number) {
        this.from_number = from_number;
    }

    @Column(name = "to_number", length = 50)
    public String getTo_number() {
        return to_number;
    }

    public void setTo_number(String to_number) {
        this.to_number = to_number;
    }

    @Transient
    public User getFromUserInfo() {
        return fromUserInfo;
    }

    public void setFromUserInfo(User fromUserInfo) {
        this.fromUserInfo = fromUserInfo;
    }

    @Transient
    public User getToUserInfo() {
        return toUserInfo;
    }

    public void setToUserInfo(User toUserInfo) {
        this.toUserInfo = toUserInfo;
    }

    @Transient
    public Car getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(Car carInfo) {
        this.carInfo = carInfo;
    }

    @Transient
    public Cargo getCargoInfo() {
        return cargoInfo;
    }

    public void setCargoInfo(Cargo cargoInfo) {
        this.cargoInfo = cargoInfo;
    }
}
