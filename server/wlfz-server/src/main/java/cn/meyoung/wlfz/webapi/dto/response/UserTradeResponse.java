package cn.meyoung.wlfz.webapi.dto.response;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by goushicai on 14-11-25.
 */

public class UserTradeResponse implements Serializable {
    private Integer id;
    private Integer type;
    private Integer status;
    private Date trade_time;

    private Integer car_id;
    private String plate_number;
    private String car_locate_number;

    private Integer cargo_id;
    private Integer cargo_status;
    private String cargo_contact_number;
    private String cargo_contact_name;
    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(Date trade_time) {
        this.trade_time = trade_time;
    }

    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getCar_locate_number() {
        return car_locate_number;
    }

    public void setCar_locate_number(String car_locate_number) {
        this.car_locate_number = car_locate_number;
    }

    public Integer getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(Integer cargo_id) {
        this.cargo_id = cargo_id;
    }

    public Integer getCargo_status() {
        return cargo_status;
    }

    public void setCargo_status(Integer cargo_status) {
        this.cargo_status = cargo_status;
    }

    public String getCargo_contact_number() {
        return cargo_contact_number;
    }

    public void setCargo_contact_number(String cargo_contact_number) {
        this.cargo_contact_number = cargo_contact_number;
    }

    public String getCargo_contact_name() {
        return cargo_contact_name;
    }

    public void setCargo_contact_name(String cargo_contact_name) {
        this.cargo_contact_name = cargo_contact_name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
