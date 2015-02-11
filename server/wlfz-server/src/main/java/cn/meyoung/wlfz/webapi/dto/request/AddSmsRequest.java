package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by goushicai on 14-12-3.
 */
public class AddSmsRequest implements Serializable {
    @NotNull
    private Integer user_id;

    @NotBlank
    @Length(max = 200)
    private String message;

    @NotBlank
    private String mobiles;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }


    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

