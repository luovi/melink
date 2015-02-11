package cn.meyoung.wlfz.webapi.dto.request;

import cn.meyoung.wlfz.webapi.validator.MefpModel1;
import cn.meyoung.wlfz.webapi.validator.MefpModel2;
import cn.meyoung.wlfz.webapi.validator.MefpModel3;
import cn.meyoung.wlfz.webapi.validator.MefpModel4;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 车辆列表参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public class ListCarsRequest extends PagedRequest implements Serializable {

    private static final long serialVersionUID = 1377950705096420269L;

    /**
     * 时间排序
     */
    public final static String ORDER_BY_TIME = "time";

    /**
     * 距离排序
     */
    public final static String ORDER_BY_DISTANCE = "distance";


    private String orderby;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private BigDecimal sw_lng;
    private BigDecimal sw_lat;
    private BigDecimal ne_lng;
    private BigDecimal ne_lat;
    private String keyword;

    @MefpModel1
    private String model_1;

    @MefpModel2
    private String model_2;

    @MefpModel3
    private String model_3;

    @MefpModel4
    private String model_4;
    private Integer user_id;
    private String original;
    private String original_code;
    private BigDecimal length_from;
    private BigDecimal length_to;
    private BigDecimal width_from;
    private BigDecimal width_to;
    private BigDecimal height_from;
    private BigDecimal height_to;
    private BigDecimal tonnage_from;
    private BigDecimal tonnage_to;
    private Integer app_dev_id;


    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
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

    public String getModel_1() {
        return model_1;
    }

    public void setModel_1(String model_1) {
        this.model_1 = model_1;
    }

    public String getModel_2() {
        return model_2;
    }

    public void setModel_2(String model_2) {
        this.model_2 = model_2;
    }

    public String getModel_3() {
        return model_3;
    }

    public void setModel_3(String model_3) {
        this.model_3 = model_3;
    }

    public String getModel_4() {
        return model_4;
    }

    public void setModel_4(String model_4) {
        this.model_4 = model_4;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getOriginal_code() {
        return original_code;
    }

    public void setOriginal_code(String original_code) {
        this.original_code = original_code;
    }

    public BigDecimal getLength_from() {
        return length_from;
    }

    public void setLength_from(BigDecimal length_from) {
        this.length_from = length_from;
    }

    public BigDecimal getLength_to() {
        return length_to;
    }

    public void setLength_to(BigDecimal length_to) {
        this.length_to = length_to;
    }

    public BigDecimal getWidth_from() {
        return width_from;
    }

    public void setWidth_from(BigDecimal width_from) {
        this.width_from = width_from;
    }

    public BigDecimal getWidth_to() {
        return width_to;
    }

    public void setWidth_to(BigDecimal width_to) {
        this.width_to = width_to;
    }

    public BigDecimal getHeight_from() {
        return height_from;
    }

    public void setHeight_from(BigDecimal height_from) {
        this.height_from = height_from;
    }

    public BigDecimal getHeight_to() {
        return height_to;
    }

    public void setHeight_to(BigDecimal height_to) {
        this.height_to = height_to;
    }

    public BigDecimal getTonnage_from() {
        return tonnage_from;
    }

    public void setTonnage_from(BigDecimal tonnage_from) {
        this.tonnage_from = tonnage_from;
    }

    public BigDecimal getTonnage_to() {
        return tonnage_to;
    }

    public void setTonnage_to(BigDecimal tonnage_to) {
        this.tonnage_to = tonnage_to;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getApp_dev_id() {
        return app_dev_id;
    }

    public void setApp_dev_id(Integer app_dev_id) {
        this.app_dev_id = app_dev_id;
    }
}
