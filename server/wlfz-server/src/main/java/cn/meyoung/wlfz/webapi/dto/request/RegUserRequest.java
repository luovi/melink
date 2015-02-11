package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 用户注册参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-24
 */
public class RegUserRequest implements Serializable {

    @NotBlank
    @Length(max = 50)
    private String username;

    @NotBlank
    @Length(max = 50)
    private String contact_number;

    @NotBlank
    @Length(max = 50)
    private String password;

    @NotBlank
    @Length(max = 200)
    private String company;

    @NotBlank
    private String captcha;

    @Length(max = 50)
    private String code;

    @Length(max = 200)
    private String address;

    @Length(max = 50)
    private String contact_name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }
}
