package cn.meyoung.wlfz.webapi.entity;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

/**
 * 车货交易记录
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@Entity
@Table(name = "wlfz_trades")
@DynamicUpdate
public class Trade extends BaseEntity<Integer> {
    private Integer type;
    private Integer status;
    private Date trade_time;
    private Car carinfo;
    private Cargo cargoinfo;
    private Integer car_user_id;
    private Integer cargo_user_id;

    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "trade_time")
    public Date getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(Date trade_time) {
        this.trade_time = trade_time;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    public Car getCarinfo() {
        return carinfo;
    }

    public void setCarinfo(Car carinfo) {
        this.carinfo = carinfo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    public Cargo getCargoinfo() {
        return cargoinfo;
    }

    public void setCargoinfo(Cargo cargoinfo) {
        this.cargoinfo = cargoinfo;
    }


    @Column(name = "car_user_id")
    public Integer getCar_user_id() {
        return car_user_id;
    }

    public void setCar_user_id(Integer car_user_id) {
        this.car_user_id = car_user_id;
    }

    @Column(name = "cargo_user_id")
    public Integer getCargo_user_id() {
        return cargo_user_id;
    }

    public void setCargo_user_id(Integer cargo_user_id) {
        this.cargo_user_id = cargo_user_id;
    }

}
