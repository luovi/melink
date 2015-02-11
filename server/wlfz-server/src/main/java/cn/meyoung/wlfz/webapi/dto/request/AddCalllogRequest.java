package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by goushicai on 14-11-4.
 */
public class AddCalllogRequest implements Serializable {
    @NotNull
    @Min(1)
    private Integer from_user;

    @NotNull
    @Min(1)
    private Integer to_user;

    @NotNull
    @Min(1)
    private Integer cargo_id;

    @NotNull
    @Min(1)
    private Integer car_id;

    @NotNull
    @Min(1)
    private Integer holding_time;

    @Range(min = 0, max = 1)
    private Integer type;

    @Length(max = 50)
    private String  from_number;

    @Length(max = 50)
    private String  to_number;

    @Range(min = 0, max = 1)
    private Integer result;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getHolding_time() {
        return holding_time;
    }

    public void setHolding_time(Integer holding_time) {
        this.holding_time = holding_time;
    }
}
