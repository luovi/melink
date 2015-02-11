package cn.meyoung.wlfz.webapi.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加货物参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public class AddCargoRequest extends ModifyCargoRequest implements Serializable {
    private static final long serialVersionUID = 4738805506493426631L;

    @NotNull
    @Min(0)
    private Integer user_id;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
