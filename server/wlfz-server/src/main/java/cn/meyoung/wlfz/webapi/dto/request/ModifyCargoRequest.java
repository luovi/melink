package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 修改货物信息参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public class ModifyCargoRequest implements Serializable {
    private static final long serialVersionUID = -86529943671786105L;

    @NotBlank
    @Length(max = 50)
    private String origin_code;

    @NotBlank
    @Length(max = 50)
    private String destination_code;

    @Length(max = 50)
    private String origin;

    @Length(max = 50)
    private String destination;

    @Length(max = 50)
    private String short_title;

    @Range(min = 0, max = 3)
    private Byte type;

    private BigDecimal longitude;
    private BigDecimal latitude;
    private BigDecimal weight;
    private BigDecimal volume;
    private BigDecimal carriage;

    @Length(max=200)
    private String tips;

    private String contact_name;
    private String contact_number;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expired_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publish_time;

    private Integer trade_status;

    @Length(max = 1000)
    private String memo;


    public String getOrigin_code() {
        return origin_code;
    }

    public void setOrigin_code(String origin_code) {
        this.origin_code = origin_code;
    }

    public String getDestination_code() {
        return destination_code;
    }

    public void setDestination_code(String destination_code) {
        this.destination_code = destination_code;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getShort_title() {
        return short_title;
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getCarriage() {
        return carriage;
    }

    public void setCarriage(BigDecimal carriage) {
        this.carriage = carriage;
    }

    public Date getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Date expired_time) {
        this.expired_time = expired_time;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public Integer getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(Integer trade_status) {
        this.trade_status = trade_status;
    }

    public Date getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }
}
