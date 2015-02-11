package cn.meyoung.wlfz.webapi.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户Token信息
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
public class UserToken implements Serializable {
    private static final long serialVersionUID = 329546375520545879L;

    private Integer userId;

    private Byte isAdmin;

    private Date expiredTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }
}
