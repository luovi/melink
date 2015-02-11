package cn.meyoung.wlfz.webapi.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@Entity
@Table(name = "wlfz_users")
@DynamicUpdate
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)//多对1关系添加缓存
public class User extends BaseEntity<Integer> {

    /**
     * 用户状态：正常
     */
    public static final byte STATUS_ENABLE = 1;

    /**
     * 用户状态：停用
     */
    public static final byte STATUS_DISABLE = 0;

    /**
     * 管理员标识:管理员
     */
    public static final byte IS_ADMIN_YES = 1;

    /**
     * 管理员标示：普通用户
     */
    public static final byte IS_NOT_ADMIN = 0;

    /**
     * 客服确认：已确认
     */
    public static final byte IS_CHECKED = 1;

    /**
     * 客服确认：未确认
     */
    public static final byte IS_NOT_CHECKED = 0;


    private String userName;
    private String password;
    private String code;
    private String address;
    private String company;
    private String contactName;
    private String contactNumber;
    private Date registerTime;
    private Byte status;
    private Byte checked;
    private String memo;
    private String loginIP;
    private Date loginTime;
    private Byte isAdmin;


    @Column(name = "user_name", length = 50)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "password", length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "code", length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "address", length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "company", length = 200)
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Column(name = "contact_name", length = 50)
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Column(name = "contact_number", length = 50)
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Column(name = "register_time")
    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    @Column(name = "status")
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Column(name = "checked")
    public Byte getChecked() {
        return checked;
    }

    public void setChecked(Byte checked) {
        this.checked = checked;
    }

    @Column(name = "memo", length = 50)
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Column(name = "login_ip", length = 50)
    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    @Column(name = "login_time")
    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }


    @Column(name = "is_admin")
    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }
}
