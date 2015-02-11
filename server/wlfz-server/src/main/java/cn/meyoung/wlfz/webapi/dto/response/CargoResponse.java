package cn.meyoung.wlfz.webapi.dto.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 货物信息
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
public class CargoResponse implements Serializable {
    private static final long serialVersionUID = 8378185910439483067L;

    private Integer id;
    private String origin;

    @JSONField(name = "origin_code")
    private String originCode;

    private String destination;

    @JSONField(name = "destination_code")
    private String destinationCode;

    @JSONField(name = "short_title")
    private String shortTitle;
    private Byte type;
    private BigDecimal weight;
    private BigDecimal volume;
    private BigDecimal carriage;

    @JSONField(name = "user_id")
    private Integer userId;

    @JSONField(name = "user_code")
    private String userCode;

    private String contact_name;
    private String contact_number;

    @JSONField(name = "publish_time")
    private Date publishTime;

    @JSONField(name = "expired_time")
    private Date expiredTime;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String tips;

    @JSONField(serialize = false, deserialize = false)
    private Double distance;

    private Integer trade_status;

    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    @JSONField(serialize = true, deserialize = false, name = "distance")
    public Double getDistanceForDisplay() {

        if (null != distance) {
            DecimalFormat df = new DecimalFormat("0.0");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return new Double(df.format(distance).toString());
        }

        return distance;
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
}
