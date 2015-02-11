package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by goushicai on 14-12-11.
 */
public class PushRequest implements Serializable {
    @NotNull
    private Integer user_id;

    @NotBlank
    private String mobiles;
    @NotBlank
    private String message;

    @NotNull
    private Integer cargo_id;

    private String title;

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(Integer cargo_id) {
        this.cargo_id = cargo_id;
    }
}

