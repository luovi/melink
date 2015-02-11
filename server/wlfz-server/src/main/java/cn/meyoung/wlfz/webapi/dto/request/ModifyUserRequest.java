package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 修改用户信息参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
public class ModifyUserRequest implements Serializable {
    private static final long serialVersionUID = -5482162636741141186L;

    @Length(max = 50)
    private String password;

    @Length(max = 200)
    private String company;

    @Length(max = 50)
    private String contact_name;

    @Length(max = 200)
    private String address;

    @Length(max = 50)
    private String memo;

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

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
