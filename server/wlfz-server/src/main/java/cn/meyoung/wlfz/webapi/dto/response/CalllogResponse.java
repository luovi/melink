package cn.meyoung.wlfz.webapi.dto.response;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by goushicai on 14-11-4.
 */
public class CalllogResponse implements Serializable {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer from_user;
    private Integer to_user;
    private Integer type;
    private Integer car_id;
    private Integer cargo_id;

    private Date start_time;
    private Date end_time;
    private String from_number;
    private String to_number;
    private String fromUserContactName;
    private String toUserContactName;
    private String plateNumber;
    private String result;

    public Integer getFrom_user() {
        return from_user;
    }

    public void setFrom_user(Integer from_user) {
        this.from_user = from_user;
    }

    public Integer getTo_user() {
        return to_user;
    }

    public void setTo_user(Integer to_user) {
        this.to_user = to_user;
    }

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

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getFrom_number() {
        return from_number;
    }

    public void setFrom_number(String from_number) {
        this.from_number = from_number;
    }

    public String getTo_number() {
        return to_number;
    }

    public void setTo_number(String to_number) {
        this.to_number = to_number;
    }


    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFromUserContactName() {
        return fromUserContactName;
    }

    public void setFromUserContactName(String fromUserContactName) {
        this.fromUserContactName = fromUserContactName;
    }

    public String getToUserContactName() {
        return toUserContactName;
    }

    public void setToUserContactName(String toUserContactName) {
        this.toUserContactName = toUserContactName;
    }
}

