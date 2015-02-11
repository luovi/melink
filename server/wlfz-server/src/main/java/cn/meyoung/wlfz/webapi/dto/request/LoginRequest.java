package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 用户登录参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 2037858507340965366L;

    @NotBlank
    private String login_name;

    @NotBlank
    private String password;

    @NotBlank
    private String client;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
