package cn.meyoung.wlfz.webapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 验证码记录
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@Entity
@Table(name = "wlfz_captcha")
public class Captcha extends BaseEntity<Integer> {
    private String contactNumber;
    private Byte type;
    private String captcha;
    private Date sendTime;
    private Integer expired;

    @Column(name = "contact_number", length = 50)
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Column(name = "type")
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Column(name = "captcha", length = 50)
    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Column(name = "send_time")
    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Column(name = "expired")
    public Integer getExpired() {
        return expired;
    }

    public void setExpired(Integer expired) {
        this.expired = expired;
    }
}
