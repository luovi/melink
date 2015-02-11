package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加车辆参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public class AddCarRequest extends ModifyCarRequest implements Serializable {
    private static final long serialVersionUID = 5809904383133274406L;

    @NotNull
    @Min(0)
    private Integer user_id;

    @NotBlank
    @Length(max = 50)
    private String plate_number;

    @NotBlank()
    @Length(max = 50)
    private String license;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
