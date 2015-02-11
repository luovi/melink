package cn.meyoung.wlfz.webapi.dto.request;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by goushicai on 14-11-5.
 */
public class ListTradeRequest extends PagedRequest implements Serializable {
    private Integer type;
    private Integer cargo_id;
    private Integer car_id;
    private Integer status;
    private Date from_time;
    private Date to_time;
    private Integer car_user_id;
    private Integer cargo_user_id;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(Integer cargo_id) {
        this.cargo_id = cargo_id;
    }

    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getFrom_time() {
        return from_time;
    }

    public void setFrom_time(Date from_time) {
        this.from_time = from_time;
    }

    public Date getTo_time() {
        return to_time;
    }

    public void setTo_time(Date to_time) {
        this.to_time = to_time;
    }


    public Integer getCar_user_id() {
        return car_user_id;
    }

    public void setCar_user_id(Integer car_user_id) {
        this.car_user_id = car_user_id;
    }

    public Integer getCargo_user_id() {
        return cargo_user_id;
    }

    public void setCargo_user_id(Integer cargo_user_id) {
        this.cargo_user_id = cargo_user_id;
    }
}
