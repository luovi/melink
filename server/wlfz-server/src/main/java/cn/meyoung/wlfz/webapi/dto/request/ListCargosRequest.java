package cn.meyoung.wlfz.webapi.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 货物列表查询参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
public class ListCargosRequest extends PagedRequest implements Serializable {
    private static final long serialVersionUID = -609413266524373414L;

    /**
     * 时间排序
     */
    public final static String ORDER_BY_TIME = "time";

    /**
     * 距离排序
     */
    public final static String ORDER_BY_DISTANCE = "distance";

    private String origin_code;
    private String destination_code;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private BigDecimal sw_lng;
    private BigDecimal sw_lat;
    private BigDecimal ne_lng;
    private BigDecimal ne_lat;
    private Integer user_id;
    private String orderby;
    private Integer status;
    private Integer trade_status;
    private Integer weight_from;
    private Integer weight_to;
    private Date publish_time;
    private String memo;
    private String tips;

    public Date getPublishTime() {
        return this.publish_time;
    }

    public void setPublishTime(Date dt) {
        this.publish_time = dt;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public ListCargosRequest() {
        this.status = 1;
    }


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

    public BigDecimal getSw_lng() {
        return sw_lng;
    }

    public void setSw_lng(BigDecimal sw_lng) {
        this.sw_lng = sw_lng;
    }

    public BigDecimal getSw_lat() {
        return sw_lat;
    }

    public void setSw_lat(BigDecimal sw_lat) {
        this.sw_lat = sw_lat;
    }

    public BigDecimal getNe_lng() {
        return ne_lng;
    }

    public void setNe_lng(BigDecimal ne_lng) {
        this.ne_lng = ne_lng;
    }

    public BigDecimal getNe_lat() {
        return ne_lat;
    }

    public void setNe_lat(BigDecimal ne_lat) {
        this.ne_lat = ne_lat;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(Integer trade_status) {
        this.trade_status = trade_status;
    }

    public Integer getWeight_from() {
        return weight_from;
    }

    public void setWeight_from(Integer weight_from) {
        this.weight_from = weight_from;
    }

    public Integer getWeight_to() {
        return weight_to;
    }

    public void setWeight_to(Integer weight_to) {
        this.weight_to = weight_to;
    }
}
